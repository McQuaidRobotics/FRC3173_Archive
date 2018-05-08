package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStationLCD.Line;
import edu.wpi.first.wpilibj.Joystick;

/**
 * McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * Primary control class for the autonomous mode
 *
 * @author Nick Brown, Tom Piersall, Ben Pylko, Colton Surdyk, and Josh Kwiatkowski
 */
public class RobotAutonomous {

    /**
     * Variable for keeping track of the line configuration being followed
     */
    public static class LineConfiguration {

        public boolean lineConfiguration;
        static final boolean kStraightLine_val = true;
        static final boolean kYLine_val = false;
        public static final LineConfiguration kStraightLine = new LineConfiguration(kStraightLine_val);
        public static final LineConfiguration kYLine = new LineConfiguration(kYLine_val);

        private LineConfiguration(boolean lineConfiguration) {
            this.lineConfiguration = lineConfiguration;
        }
    }
    // Variables from IgKnightersBot (all required robot components)
    private RobotDriveMecanum m_robotDriveMecanum;
    private RobotAcquisition m_robotAcquisition;
    private RobotElevation m_robotElevation;
    private RobotSignalLighting m_robotSignalLighting;
    private ProximitySensor m_proximitySensor;
    private DigitalInput m_leftTapeSensor;
    private DigitalInput m_rightTapeSensor;
    private DigitalInput m_bendTapeSensor;
    private Gyro m_gyro;
    private Joystick m_rightJoystick;
    // Solenoids for powering the tape sensors
    private Solenoid leftTapeSensorPower;
    private Solenoid rightTapeSensorPower;
    private Solenoid bendTapeSensorPower;
    // RobotAutonomous variables
    private boolean isAwaitingY = true; // Set to true when tracking Y line, set to false when tracking the straight line
    private int confirmationCounter = 0; // Counter for keep track of the number of consecutive times the robot has been within the desired distance
    private int stoppingDistance; // Distance (in inches) before the wall to stop
    private final double ROTATION_CORRECTION_SPEED = 0.05; // Speed of rotation when correction orientation
    private final int CONFIRMATION_TOLERANCE = 4; // Number of times the robot needs to be at the desired distance for the loop to exit
    private final int Y_LINE_STOP_DISTANCE = 30; // Distance (in inches) the robot will stop moving forward while strafing the Y line
    private final double Y_LINE_FAST_STRAFE_SPEED = -0.5; // Moves left, make positive to move right
    private final double Y_LINE_SLOW_STRAFE_SPEED = -0.2; // Moves left, make positive to move right
    private final int Y_LINE_GYRO_TOLERANCE = 5; // Amount that the robot is allowed to rotate when strafing before it is corrected

    /**
     * Main method for initializing the autonomous mode controller class
     */
    public RobotAutonomous() {
        m_robotDriveMecanum = IgKnightersBot.robotDriveMecanum;
        m_robotAcquisition = IgKnightersBot.robotAcquisition;
        m_robotElevation = IgKnightersBot.robotElevation;
        m_robotSignalLighting = IgKnightersBot.robotSignalLighting;
        m_proximitySensor = IgKnightersBot.proximitySensor;
        m_leftTapeSensor = IgKnightersBot.leftTapeSensor;
        m_rightTapeSensor = IgKnightersBot.rightTapeSensor;
        m_bendTapeSensor = IgKnightersBot.bendTapeSensor;
        m_gyro = IgKnightersBot.gyro;
        m_rightJoystick = IgKnightersBot.rightJoystick;
        powerTapeSensors();
    }

    /**
     * Runs the autonomous mode for the specified line configuration and elevation
     *
     * Consists of Pre-movement Setup, Elevations Adjustment, Pre-adjusted Drive Movement, Adjusted Drive Movement, and Post-Drive Movement Actions
     *
     * @param lineConfiguration Line configuration of the tape line being followed
     * @param height The  elevation to place the ubertube at
     */
    public void run(LineConfiguration lineConfiguration, RobotElevation.Height height) {

        /**
         * PRE-MOVEMENT SETUP
         *
         * Resets gyro
         * Configures Y-line tracking mode based on switch input
         * Configures stopping distance based on the inputted elevation
         */

        m_gyro.reset(); // Zeros the gyro while the robot is perfectly aligned

        System.out.println(isAwaitingY);

        /**
         * @todo Need to fix the autonomous switch? This configuration code would not work during the competition
         */

        /*
        // Configure the values that control which type of line tracking method will be used
        if (lineConfiguration.lineConfiguration == LineConfiguration.kYLine_val) {
        isAwaitingY = true; System.out.println("awaiting y");
        } else if (lineConfiguration.lineConfiguration == LineConfiguration.kStraightLine_val) {
        isAwaitingY = false;
        } else {
        IgKnightersBot.robotDriverStation.outputLine(Line.kUser6, "ERROR: Invalid line configuration input for autonomous mode");
        IgKnightersBot.robotDriverStation.update();
        }
        */

        // Configure the stopping distance based on the desire elevation height
        if (height.height == RobotElevation.Height.kAutonomousMiddleColumn_LowerRow_val || height.height == RobotElevation.Height.kAutonomousSideColumn_LowerRow_val) {
            stoppingDistance = 19;
        } else if (height.height == RobotElevation.Height.kAutonomousMiddleColumn_MiddleRow_val || height.height == RobotElevation.Height.kAutonomousSideColumn_MiddleRow_val) {
            stoppingDistance = 17;
        } else {
            System.out.println("ERROR: Invalid elevation configuration input for autonomous mode");
            IgKnightersBot.robotDriverStation.outputLine(Line.kUser6, "ERROR: Invalid elevation configuration input for autonomous mode");
            IgKnightersBot.robotDriverStation.update();
        }

        
        /**
         * ELEVATIONS ADJUSTMENT
         * 
         * Sets the elevation to the desired height before the robot moves into position (the robot will be immobile during this process)
         */
        configureElevation(height);


        /**
         * PRE-ADJUSTED DRIVE MOVEMENT (WHILE LOOP #1)
         *
         * Drive straight forward until one of the sensors trips
         *
         * Right joystick trigger may be used to exit this loop (necessary when switching into tele-operated before the code has completed)
         */
        while ((!m_rightTapeSensor.get() && !m_leftTapeSensor.get() && !m_bendTapeSensor.get()) && (confirmationCounter < CONFIRMATION_TOLERANCE) && checkOverride()) {
            
            // Moves the robot forward until it reaches the desired distance
            m_robotDriveMecanum.mecanumDrive(0, getAdjustedSpeed(stoppingDistance), 0);

            /**
             * Checks to see if the robot has reached the desired position
             */
            if (m_proximitySensor.getDistance() < stoppingDistance) {
                confirmationCounter++;
            }
        }

        /**
         * ADJUSTED DRIVE MOVEMENT BASED ON SENSOR INPUT (WHILE LOOP #2)
         *
         * Drive with a slight correction turn as sensors are tripped until the desired distance is reached
         *
         * Right joystick trigger may be used to exit this loop (necessary when switching into teloperated before the code has completed)
         */
        while (confirmationCounter < CONFIRMATION_TOLERANCE && checkOverride()) {

            /**
             * When the robot reaches the Y line (This code is only applicable when Y-line tracking mode is turned on)
             */
            if (m_bendTapeSensor.get() && isAwaitingY) {

                // Signal lighting
                m_robotSignalLighting.setAll(RobotSignalLighting.State.kOff);
                m_robotSignalLighting.set(RobotSignalLighting.Light.kRedLight, RobotSignalLighting.State.kOn);
                m_robotSignalLighting.set(RobotSignalLighting.Light.kBlueLight, RobotSignalLighting.State.kOn);

                // Move the robot forward slightly until the sensors are off the Y
                m_robotDriveMecanum.mecanumDrive(0, 0.3, 0);
                Timer.delay(0.8);
                m_robotDriveMecanum.mecanumDrive(0, 0, 0);


                // Move the robot diagonally until it has realigned with the tape
                while (!m_bendTapeSensor.get() && checkOverride()) {
                    if (Math.abs(m_gyro.getAngle()) < Y_LINE_GYRO_TOLERANCE) {
                        m_robotDriveMecanum.mecanumDrive(Y_LINE_FAST_STRAFE_SPEED, getAdjustedSpeed(Y_LINE_STOP_DISTANCE), 0); // Uses Y-line stop distance to slow forward speed once the desired distance has been reached; This leads to only sideways movement once the robot has move forward far enough
                    } else {
                        correctRotation(); // Adjusts the robots orientation when it gets thrown off
                    }
                }

                // Move the robot diagonally until it has realigned with the tape
                while (!m_leftTapeSensor.get() && checkOverride()) {
                    if (Math.abs(m_gyro.getAngle()) < Y_LINE_GYRO_TOLERANCE) {
                        m_robotDriveMecanum.mecanumDrive(Y_LINE_SLOW_STRAFE_SPEED, getAdjustedSpeed(Y_LINE_STOP_DISTANCE), 0); // Uses Y-line stop distance to slow forward speed once the desired distance has been reached; This leads to only sideways movement once the robot has move forward far enough
                    } else {
                        correctRotation(); // Adjusts the robots orientation when it gets thrown off
                    }
                }

                // Move the robot diagonally until it has realigned with the tape
                while (m_leftTapeSensor.get() && checkOverride()) {
                    if (Math.abs(m_gyro.getAngle()) < Y_LINE_GYRO_TOLERANCE) {
                        m_robotDriveMecanum.mecanumDrive(Y_LINE_SLOW_STRAFE_SPEED, getAdjustedSpeed(Y_LINE_STOP_DISTANCE), 0); // Uses Y-line stop distance to slow forward speed once the desired distance has been reached; This leads to only sideways movement once the robot has move forward far enough
                    } else {
                        correctRotation(); // Adjusts the robots orientation when it gets thrown off
                    }
                }

                // Move foward slightly to align the tube with the scoring peg
                m_robotDriveMecanum.mecanumDrive(0, 0.3, 0);
                Timer.delay(0.4);
                m_robotDriveMecanum.mecanumDrive(0, 0, 0); // Stop the robot once it has been aligned

                confirmationCounter = CONFIRMATION_TOLERANCE + 1; // So the while loop will exit

                isAwaitingY = false; // Turn Y-line tracking mode off (The robot will no longer be looking for the Y)

                /**
                 * Correct drive movement right
                 */
            } else if (m_rightTapeSensor.get()) {
                m_robotDriveMecanum.mecanumDrive(0, getAdjustedSpeed(stoppingDistance), ROTATION_CORRECTION_SPEED);
                m_robotSignalLighting.setOnly(RobotSignalLighting.Light.kRedLight, RobotSignalLighting.State.kOn); // Turns on the red light to signal clockwise correction

                /**
                 * Correct drive movement left
                 */
            } else if (m_leftTapeSensor.get()) {
                m_robotDriveMecanum.mecanumDrive(0, getAdjustedSpeed(stoppingDistance), -ROTATION_CORRECTION_SPEED);
                m_robotSignalLighting.setOnly(RobotSignalLighting.Light.kBlueLight, RobotSignalLighting.State.kOn); // Turns on the blue light to signal counter-clockwise correction

            }

            /**
             * Checks to see if the robot has reached the desired position
             */
            if (m_proximitySensor.getDistance() < stoppingDistance) {
                confirmationCounter++;
            }

            Timer.delay(0.005);
        }

        m_robotSignalLighting.setOnly(RobotSignalLighting.Light.kWhiteLight, RobotSignalLighting.State.kOn); // Turns on white light to signal completion of the line tracking


        /**
         * POST-DRIVE MOVEMENT ACTIONS
         *
         * Eject the ubertube
         *
         * Right joystick trigger may be used to skip this code (necessary when switching into teloperated before the code has completed)
         */
        if (checkOverride()) {

            m_robotAcquisition.eject(); // Ejects the ubertube

        }

        m_robotSignalLighting.setAll(RobotSignalLighting.State.kOn); // Turns all lights on to signal completion of autonomous

    }

    /**
     * Set the elevation to the correct height
     * 
     * @param height Height to set the elevation to
     */
    public void configureElevation(RobotElevation.Height height) {
        m_robotElevation.moveTo(height); // Begins moving the elevation so isStopped won't return true

        while (!m_robotElevation.getStopped() && checkOverride()) {
            m_robotElevation.moveTo(height);
        }
    }

    /**
     * Gets the adjusted forward speed for the robot. Decreases as the robot nears the desired stopping distance
     * 
     * @param stopDistance Desired stopping distance from the wall (in inches)
     * @return Double: Value for the adjusted speed [Range: (-1, 1)]
     */
    private double getAdjustedSpeed(int stopDistance) {
        double speed = (m_proximitySensor.getDistance() - stopDistance) * (0.01); // The multiplier is a ratio that can be adjusted, balances speed and stopping accuracy (NOTE: Changingt this will change the distance correction variable)

        if (speed > 0.4) {
            return 0.4;
        } else if (speed < 0.1) {
            return 0.15;
        } else {
            return speed;
        }
    }

    /**
     * Corrects the rotation of the robot
     *
     * Should be parallel to the tracking line when complete
     */
    private void correctRotation() {
        if (m_gyro.getAngle() > 0) {
            while (m_gyro.getAngle() > 0 && checkOverride()) {
                m_robotDriveMecanum.mecanumDrive(0, 0, -0.2);
            }
        } else if (m_gyro.getAngle() < 0) {
            while (m_gyro.getAngle() < 0 && checkOverride()) {
                m_robotDriveMecanum.mecanumDrive(0, 0, 0.2);
            }
        }

        m_robotDriveMecanum.mecanumDrive(0, 0, 0); // Stop driving when the gyro reaches zero

    }

    /**
     * Power the tape sensors using their solenoid connections
     */
    private void powerTapeSensors() {
        leftTapeSensorPower = new Solenoid(ElectricalIDs.PneumaticPower.kLeftLineSensorPower);
        rightTapeSensorPower = new Solenoid(ElectricalIDs.PneumaticPower.kRightLineSensorPower);
        bendTapeSensorPower = new Solenoid(ElectricalIDs.PneumaticPower.kBendLineSensorPower);

        leftTapeSensorPower.set(true);
        rightTapeSensorPower.set(true);
        bendTapeSensorPower.set(true);

    }

    /**
     * Check the override input
     *
     * @return Whether the override trigger is being pressed
     */
    private boolean checkOverride() {
        return !m_rightJoystick.getTrigger();
    }
}
