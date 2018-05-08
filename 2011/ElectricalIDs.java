package edu.wpi.first.wpilibj.templates;

/**
 * McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * Primary class for storing the IDs for all major electrical components and interfaces
 *
 * @author Nick Brown and Ben Pylko
 */
public class ElectricalIDs {

    public static class CANJaguars {

        static final int kFLDriveCANJaguar = 2;
        static final int kFRDriveCANJaguar = 5;
        static final int kRLDriveCANJaguar = 3;
        static final int kRRDriveCANJaguar = 6;
        static final int kTopAcquisitionCANJaguar = 7;
        static final int kBottomAcquisitionCANJaguar = 8;
        static final int kPrimaryElevationCANJaguar = 9;
        static final int kAuxiliaryElevationCANJaguar = 4;
    }

    public static class Relays {

        static final int kCompressor = 1;
        static final int kRedLight = 2;
        static final int kWhiteLight = 3;
        static final int kBlueLight = 4;
    }

    public static class DigitalInputs {

        static final int kLeftLineSensor = 1; // Plugged into 1 and 2
        static final int kBendLineSensor = 3; // Plugged into 3 and 4
        static final int kRightLineSensor = 5; // Plugged into 5 and 6
        static final int kAcquisitionContactSwitch = 9;
        static final int kCompressorPressureSensor = 10;
        static final int kEncoderChannelA = 13;
        static final int kEncoderChannelB = 14;
        static final int kAutonomousEnabledSwitch = 7;
        static final int kAutonomousElevationSwitch = 11;
        static final int kAutonomousLineSwitch = 12;
    }

    public static class HIDDevices {

        static final int kLeftJoystick = 1;
        static final int kRightJoystick = 2;
        static final int kGamepad = 3;
    }

    public static class AnalogChannels {

        static final int kGyro = 1; // Plugged into 1 and 2
        static final int kProximitySensor = 6;
    }

    public static class PneumaticPower {
 static final int kLeftLineSensorPower = 1;
        static final int kRightLineSensorPower = 2;
        static final int kBendLineSensorPower = 3;
        static final int kDeploymentSolenoidStageOneOut = 5;
        static final int kDeploymentSolenoidStageOneIn = 6;
        static final int kDeploymentSolenoidStageTwoOut = 7;
        static final int kDeploymentSolenoidStageTwoIn = 8;
    }
}