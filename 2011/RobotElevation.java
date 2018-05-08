package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Encoder;

/**
 * McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * Primary class for controlling the robot's elevation subsystem
 *
 * @author Nick Brown
 */
public class RobotElevation {

    /**
     * Preset, calibrated values for each elevation level (all values are measured in inches by the encoder)
     */
    public static class Height {

        public int height;
        static final int CORRECTION_VAL = 18; // Number of inches from the bottom of the lower acquistion wheel to the ideal placement height [This is not initialized as a Height]
        static final int STARTING_CONFIGURATION_VAL = 44; // Number of inches from the bottom of the lower acquisition wheel to the ground in the starting configuration; This is for before the elevation encoder is zeroed [This is not initialized as a Height]
        static final int kGround_val = 0;
        static final int kFeeder_val = 42;
        static final int kSideColumn_LowerRow_val = 30 - CORRECTION_VAL;
        static final int kSideColumn_MiddleRow_val = 67 - CORRECTION_VAL;
        static final int kSideColumn_UpperRow_val = 104 - CORRECTION_VAL;
        static final int kMiddleColumn_LowerRow_val = 38 - CORRECTION_VAL;
        static final int kMiddleColumn_MiddleRow_val = 74 - CORRECTION_VAL;
        static final int kMiddleColumn_UpperRow_val = 110 - CORRECTION_VAL;
        static final int kAutonomousSideColumn_LowerRow_val = (29 - STARTING_CONFIGURATION_VAL) - CORRECTION_VAL;
        static final int kAutonomousSideColumn_MiddleRow_val = (66 - STARTING_CONFIGURATION_VAL) - CORRECTION_VAL;
        static final int kAutonomousMiddleColumn_LowerRow_val = (37 - STARTING_CONFIGURATION_VAL) - CORRECTION_VAL;
        static final int kAutonomousMiddleColumn_MiddleRow_val = (78 - STARTING_CONFIGURATION_VAL) - CORRECTION_VAL;
        public static final Height kGround = new Height(kGround_val);
        public static final Height kFeeder = new Height(kFeeder_val);
        public static final Height kSideColumn_LowerRow = new Height(kSideColumn_LowerRow_val);
        public static final Height kSideColumn_MiddleRow = new Height(kSideColumn_MiddleRow_val);
        public static final Height kSideColumn_UpperRow = new Height(kSideColumn_UpperRow_val);
        public static final Height kMiddleColumn_LowerRow = new Height(kMiddleColumn_LowerRow_val);
        public static final Height kMiddleColumn_MiddleRow = new Height(kMiddleColumn_MiddleRow_val);
        public static final Height kMiddleColumn_UpperRow = new Height(kMiddleColumn_UpperRow_val);
        public static final Height kAutonomousSideColumn_LowerRow = new Height(kAutonomousSideColumn_LowerRow_val);
        public static final Height kAutonomousSideColumn_MiddleRow = new Height(kAutonomousSideColumn_MiddleRow_val);
        public static final Height kAutonomousMiddleColumn_LowerRow = new Height(kAutonomousMiddleColumn_LowerRow_val);
        public static final Height kAutonomousMiddleColumn_MiddleRow = new Height(kAutonomousMiddleColumn_MiddleRow_val);

        private Height(int height) {
            this.height = height;
        }
    }
    private CANJaguar m_primaryCANJaguar;
    private CANJaguar m_auxiliaryCANJaguar;
    private Encoder m_encoder;
    // Encoder Calibration Values
    private final double ENCODER_DISTANCE_PER_PULSE_VAL = (double) (6 * Math.cos((29.4 * Math.PI) / 180)) / 360; // 6 is the circumference of the gear the encoder is attached to; 29.4 is the number of degrees of offset on the elevation system
    private final double MAX_MOTOR_SPEED = 0.75; // Maximum speed to adjust the elevation motors

    /**
     * Creates object using pre-initialized components
     *
     * @param primaryCANJaguar First elevation jaguar
     * @param auxiliaryCANJaguar Second elevation jaguar
     * @param encoder Encoder
     */
    public RobotElevation(CANJaguar primaryCANJaguar, CANJaguar auxiliaryCANJaguar, Encoder encoder) {
        m_primaryCANJaguar = primaryCANJaguar;
        m_auxiliaryCANJaguar = auxiliaryCANJaguar;
        m_encoder = encoder;

        setupEncoder();
    }

    /**
     * Creates object with inputted component IDs
     *
     * @param primaryCANJaguarID First elevation jaguar ID
     * @param auxiliaryCANJaguarID Second elevation jaguar ID
     * @param encoderChannelA ID for channel A of the encoder
     * @param encoderChannelB ID for channel B of the encoder
     */
    public RobotElevation(int primaryCANJaguarID, int auxiliaryCANJaguarID, int encoderChannelA, int encoderChannelB) {
        m_primaryCANJaguar = RobotCANJaguar.initializeCANJaguar(primaryCANJaguarID);
        m_auxiliaryCANJaguar = RobotCANJaguar.initializeCANJaguar(auxiliaryCANJaguarID);
        m_encoder = new Encoder(encoderChannelA, encoderChannelB);

        setupEncoder();
    }

    /**
     * Moves elevator towards a particular height point if it not already there
     * @param height Desired height level
     */
    public void moveTo(Height desiredHeight) {
        RobotCANJaguar.setX(m_primaryCANJaguar, calculateSpeed(desiredHeight.height));
        synchronizeMotors();

        testZero();
    }

    /**
     * Manually set the speed of the elevation motors
     * 
     * @param speed Speed to set the elevation motors [Range: (-1,1)]
     */
    public void move(double speed) {
        RobotCANJaguar.setX(m_primaryCANJaguar, speed);
        synchronizeMotors();

        testZero();
    }

    /**
     * Stops the elevation system
     */
    public void stop() {
        move(0.0);
    }

    /**
     * Get the position of the elevation from the encoder
     * 
     * @return Double: The position of the elevation
     */
    public double getPosition() {
        return m_encoder.getDistance();
    }

    /**
     * Synchronizes the speed of the primary and auxiliary elevation motors
     *
     * *Auxiliary motor should be set to PercentVBus mode
     */
    public void synchronizeMotors() {
        RobotCANJaguar.setX(m_auxiliaryCANJaguar, RobotCANJaguar.getOutputVoltage(m_primaryCANJaguar) / RobotCANJaguar.getBusVoltage(m_primaryCANJaguar));
        // m_rightCANJaguar.set(m_leftCANJaguar.getOutputCurrent()); //For current control mode
    }

    /**
     * Configures and starts the encoders
     */
    private void setupEncoder() {
        m_encoder.setDistancePerPulse(ENCODER_DISTANCE_PER_PULSE_VAL);
        m_encoder.start();
    }

    /**
     * Calculate the speed to set the elevation system based on its position relative to the desired position
     *
     * @param desiredHeight Desired height value to calculate the speed for
     * @return The speed to set the motors to move towards the desired height
     */
    private double calculateSpeed(int desiredHeight) {
        double speed = (desiredHeight - getPosition()) / 3;

        if (speed >= MAX_MOTOR_SPEED) {
            return MAX_MOTOR_SPEED;
        } else if (speed <= -MAX_MOTOR_SPEED) {
            return -MAX_MOTOR_SPEED;
        } else {
            return speed;
        }
    }

    /**
     * Tests whether the elevation unit is moving
     * 
     * @return Returns true when the elevation unit is not moving, false otherwise
     */
    public boolean getStopped() {
        return m_encoder.getStopped();
    }

    /**
     * Zeros the encoder when the lower limit switch is tripped
     */
    private void testZero() {
        if (isAtBottom()) {
            m_encoder.reset();
        }
    }

    public boolean isAtTop() {
        return !RobotCANJaguar.getForwardLimitOK(m_primaryCANJaguar);
    }

    public boolean isAtBottom() {
        return !RobotCANJaguar.getReverseLimitOK(m_primaryCANJaguar);

    }
}
