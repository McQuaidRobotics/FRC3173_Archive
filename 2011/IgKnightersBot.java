/**----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

/**
 * McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * Primary robot control class implementation
 *
 * @author Nick Brown, Tom Piersall, Ben Pylko, Colton Surdyk, and Josh Kwiatkowski
 */
public class IgKnightersBot extends SimpleRobot {

    /**
     * Object Declarations
     */
    // Robot Driver Station
    static RobotDriverStation robotDriverStation;
    // RobotDriveMecanum
    static RobotDriveMecanum robotDriveMecanum;
    // RobotAcquisition
    static RobotAcquisition robotAcquisition;
    // RobotLighting
    static RobotSignalLighting robotSignalLighting;
    // RobotElevation
    static RobotElevation robotElevation;
    //Robot Deployment
    static RobotDeployment robotDeployment;
    // Joysticks
    static Joystick leftJoystick; // Left joystick
    static Joystick rightJoystick; // Right joystick
    // Gamepad
    static Gamepad gamepad;
    // Compressor
    static Compressor compressor;
    // Gyro
    static Gyro gyro;
    // Proximity Sensor
    static ProximitySensor proximitySensor;
    // Tape sensors (for autonomous)
    static DigitalInput leftTapeSensor; // Left tape sensor
    static DigitalInput rightTapeSensor; // Bend tape sensor (should only trip when a bend has been reached)
    static DigitalInput bendTapeSensor; // Right tape sensor
    // Autonomous Switches
    static DigitalInput autonomousElevationSwitch; // True is low row, false is middle row
    static DigitalInput autonomousLineSwitch; // True is straight line, false is Y line
    static DigitalInput autonomousEnabledSwitch; // True is enabled, false is disabled
    // RobotAutonomous
    static RobotAutonomous robotAutonomous;

    /**
     * This function is called when the robot is initialized (before autonomous begins).
     * Used to initialize the declared objects
     */
    public void robotInit() {

        // RobotDriverStation
        robotDriverStation = new RobotDriverStation();
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.update();

        // RobotDriveMecanum
        robotDriveMecanum = new RobotDriveMecanum(ElectricalIDs.CANJaguars.kFLDriveCANJaguar, ElectricalIDs.CANJaguars.kFRDriveCANJaguar, ElectricalIDs.CANJaguars.kRLDriveCANJaguar, ElectricalIDs.CANJaguars.kRRDriveCANJaguar);
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "RobotDriveMecanum");
        robotDriverStation.update();

        // RobotAcquisition
        robotAcquisition = new RobotAcquisition(ElectricalIDs.CANJaguars.kTopAcquisitionCANJaguar, ElectricalIDs.CANJaguars.kBottomAcquisitionCANJaguar, ElectricalIDs.DigitalInputs.kAcquisitionContactSwitch);
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "RobotAcquisition");
        robotDriverStation.update();

        // RobotLighting
        robotSignalLighting = new RobotSignalLighting(ElectricalIDs.Relays.kRedLight, ElectricalIDs.Relays.kWhiteLight, ElectricalIDs.Relays.kBlueLight);
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "RobotSignalLighting");
        robotDriverStation.update();

        // RobotElevation
        robotElevation = new RobotElevation(ElectricalIDs.CANJaguars.kPrimaryElevationCANJaguar, ElectricalIDs.CANJaguars.kAuxiliaryElevationCANJaguar, ElectricalIDs.DigitalInputs.kEncoderChannelA, ElectricalIDs.DigitalInputs.kEncoderChannelB);
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "RobotElevation");
        robotDriverStation.update();

        // RobotDeployment
        robotDeployment = new RobotDeployment(ElectricalIDs.PneumaticPower.kDeploymentSolenoidStageOneIn, ElectricalIDs.PneumaticPower.kDeploymentSolenoidStageOneOut, ElectricalIDs.PneumaticPower.kDeploymentSolenoidStageTwoIn, ElectricalIDs.PneumaticPower.kDeploymentSolenoidStageTwoOut);
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "RobotDeployment");
        robotDriverStation.update();

        // Joysticks
        leftJoystick = new Joystick(ElectricalIDs.HIDDevices.kLeftJoystick); // Left joystick
        rightJoystick = new Joystick(ElectricalIDs.HIDDevices.kRightJoystick); // Right joystick
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "Joysticks");
        robotDriverStation.update();

        // Gamepad
        gamepad = new Gamepad(ElectricalIDs.HIDDevices.kGamepad);
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "Gamepad");
        robotDriverStation.update();

        // Compressor
        compressor = new Compressor(ElectricalIDs.DigitalInputs.kCompressorPressureSensor, ElectricalIDs.Relays.kCompressor);
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "Compressor");
        robotDriverStation.update();

        // Gyro
        gyro = new Gyro(ElectricalIDs.AnalogChannels.kGyro);
        gyro.reset();// Resets gyro to zero degrees
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "Gyro");
        robotDriverStation.update();

        // Proximity Sensor
        proximitySensor = new ProximitySensor(ElectricalIDs.AnalogChannels.kProximitySensor);
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "ProximitySensor");
        robotDriverStation.update();

        // Tape sensors (for autonomous)
        leftTapeSensor = new DigitalInput(ElectricalIDs.DigitalInputs.kLeftLineSensor);
        rightTapeSensor = new DigitalInput(ElectricalIDs.DigitalInputs.kRightLineSensor);
        bendTapeSensor = new DigitalInput(ElectricalIDs.DigitalInputs.kBendLineSensor);
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "TapeSensors");
        robotDriverStation.update();

        // Autonomous Switches
        autonomousElevationSwitch = new DigitalInput(ElectricalIDs.DigitalInputs.kAutonomousElevationSwitch);
        autonomousLineSwitch = new DigitalInput(ElectricalIDs.DigitalInputs.kAutonomousLineSwitch);
        autonomousEnabledSwitch = new DigitalInput(ElectricalIDs.DigitalInputs.kAutonomousEnabledSwitch);
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "AutonomousSwitches");
        robotDriverStation.update();

        // RobotAutonomous
        robotAutonomous = new RobotAutonomous();
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Initializing...");
        robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "RobotAutonomous");
        robotDriverStation.update();

    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        if (autonomousEnabledSwitch.get()) {
            if (autonomousLineSwitch.get()) {
                if (autonomousElevationSwitch.get()) {
                    robotAutonomous.run(RobotAutonomous.LineConfiguration.kStraightLine, RobotElevation.Height.kAutonomousMiddleColumn_LowerRow);
                    robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "| line, Low row");
                    robotDriverStation.update();
                } else {
                    robotAutonomous.run(RobotAutonomous.LineConfiguration.kStraightLine, RobotElevation.Height.kAutonomousMiddleColumn_MiddleRow);
                    robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "| line, Mid row");
                    robotDriverStation.update();
                }
            } else {
                if (autonomousElevationSwitch.get()) {
                    robotAutonomous.run(RobotAutonomous.LineConfiguration.kYLine, RobotElevation.Height.kAutonomousSideColumn_LowerRow);
                    robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Y line, Low row");
                    robotDriverStation.update();
                } else {
                    robotAutonomous.run(RobotAutonomous.LineConfiguration.kYLine, RobotElevation.Height.kAutonomousSideColumn_MiddleRow);
                    robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Y line, Mid row");
                    robotDriverStation.update();
                }
            }
        } else {
            robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Autonomous disabled");
            robotDriverStation.update();
        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        // Loops the operator code while it is enabled
        while (isOperatorControl() && isEnabled()) {

            // Drive subsystem
            if (!leftJoystick.getTrigger()) {
                robotDriveMecanum.mecanumDrive(leftJoystick.getX(), -leftJoystick.getY(), rightJoystick.getX()); // Set the speed of the drive wheels for the mecanum system when the left trigger is pressed
            } else {
                robotDriveMecanum.mecanumDriveAbsolute(leftJoystick.getX(), -leftJoystick.getY(), rightJoystick.getX(), gyro.getAngle()); // Set the speed of the drive wheels for the mecanum system when the left trigger is not pressed (normal state)
            }

            // Resets the gyro to zero when right trigger is pressed
            if (rightJoystick.getTrigger()) {
                gyro.reset();
            }

            // Acquisition subsytem
            if (gamepad.getButton(Gamepad.Button.kAButton) && gamepad.getButton(Gamepad.Button.kRightFrontTrigger)) {
                robotAcquisition.eject(); // Eject the tube
            } else if (gamepad.getButton(Gamepad.Button.kAButton)) {
                robotAcquisition.acquire(); // Acquire the tube
            } else if (gamepad.getAxis(Gamepad.Axis.kRightJoystickY) > 0.15) {
                robotAcquisition.rotate(RobotAcquisition.Direction.kUp); // Rotates the tube up
            } else if (gamepad.getAxis(Gamepad.Axis.kRightJoystickY) < -0.15) {
                robotAcquisition.rotate(RobotAcquisition.Direction.kDown); // Rotates the tube down
            } else {
                robotAcquisition.stop(); // Stops the motors when there are no inputs
            }

            // Lighting subsystem
            if (robotAcquisition.isAcquired()) {
                robotSignalLighting.setAll(RobotSignalLighting.State.kOn);
            } else {
                robotSignalLighting.set(RobotSignalLighting.Light.kRedLight, gamepad.getButton(Gamepad.Button.kBButton));
                robotSignalLighting.set(RobotSignalLighting.Light.kWhiteLight, gamepad.getButton(Gamepad.Button.kYButton));
                robotSignalLighting.set(RobotSignalLighting.Light.kBlueLight, gamepad.getButton(Gamepad.Button.kXButton));
            }

            // Elevation subsystem
            if (gamepad.getDPadButton(Gamepad.DPadButton.kLeft) && !gamepad.getButton(Gamepad.Button.kLeftFrontTrigger)) {
                robotElevation.moveTo(RobotElevation.Height.kGround);
            } else if (gamepad.getDPadButton(Gamepad.DPadButton.kDown) && !gamepad.getButton(Gamepad.Button.kLeftFrontTrigger)) {
                robotElevation.moveTo(RobotElevation.Height.kSideColumn_LowerRow);
            } else if (gamepad.getDPadButton(Gamepad.DPadButton.kRight) && !gamepad.getButton(Gamepad.Button.kLeftFrontTrigger)) {
                robotElevation.moveTo(RobotElevation.Height.kSideColumn_MiddleRow);
            } else if (gamepad.getDPadButton(Gamepad.DPadButton.kUp) && !gamepad.getButton(Gamepad.Button.kLeftFrontTrigger)) {
                robotElevation.moveTo(RobotElevation.Height.kSideColumn_UpperRow);
            } else if (gamepad.getDPadButton(Gamepad.DPadButton.kLeft) && gamepad.getButton(Gamepad.Button.kLeftFrontTrigger)) {
                robotElevation.moveTo(RobotElevation.Height.kFeeder);
            } else if (gamepad.getDPadButton(Gamepad.DPadButton.kDown) && gamepad.getButton(Gamepad.Button.kLeftFrontTrigger)) {
                robotElevation.moveTo(RobotElevation.Height.kMiddleColumn_LowerRow);
            } else if (gamepad.getDPadButton(Gamepad.DPadButton.kRight) && gamepad.getButton(Gamepad.Button.kLeftFrontTrigger)) {
                robotElevation.moveTo(RobotElevation.Height.kMiddleColumn_MiddleRow);
            } else if (gamepad.getDPadButton(Gamepad.DPadButton.kUp) && gamepad.getButton(Gamepad.Button.kLeftFrontTrigger)) {
                robotElevation.moveTo(RobotElevation.Height.kMiddleColumn_UpperRow);
            } else if (gamepad.getAxis(Gamepad.Axis.kLeftJoystickY) != 0) {
                robotElevation.move(-(gamepad.getAxis(Gamepad.Axis.kLeftJoystickY)) / 1.5); // Left stick manually adjusts the elevation
            } else {
                robotElevation.stop();
            }

            // Deployment subsystem
            robotDeployment.setStageOne(gamepad.getButton(Gamepad.Button.kLeftFrontTrigger) && gamepad.getButton(Gamepad.Button.kLeftBackTrigger));
            robotDeployment.setStageTwo(gamepad.getButton(Gamepad.Button.kLeftFrontTrigger) && gamepad.getButton(Gamepad.Button.kLeftBackTrigger) && gamepad.getButton(Gamepad.Button.kRightFrontTrigger) && gamepad.getButton(Gamepad.Button.kRightBackTrigger));

            // Compressor
            compressor.start();

            // Refresh the Driver Station output
            robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Gyro: " + gyro.getAngle());
            robotDriverStation.outputLine(DriverStationLCD.Line.kUser2, "Encoder: " + robotElevation.getPosition());
            robotDriverStation.outputLine(DriverStationLCD.Line.kUser3, "Prox. Sensor: " + proximitySensor.getDistance());
            robotDriverStation.outputLine(DriverStationLCD.Line.kUser4, "Acq. Sensor: " + robotAcquisition.isAcquired());
            robotDriverStation.outputLine(DriverStationLCD.Line.kUser5, "L: " + leftTapeSensor.get() + "|R: " + rightTapeSensor.get() + "|B:" + bendTapeSensor.get());

            robotDriverStation.update();

            Timer.delay(0.005); // Delay before re-running operatorControl code (in seconds)
        }
    }

    /**
     * This function is called once the robot has been disabled
     */
    public void disabled() {
        robotDriverStation.outputLine(DriverStationLCD.Line.kMain6, "Ready to Enable!"); // Outputs to the Driver Station that the robot is ready to enable
        robotDriverStation.update();
    }
}
