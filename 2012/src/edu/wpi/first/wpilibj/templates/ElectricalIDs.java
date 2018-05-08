
package edu.wpi.first.wpilibj.templates;
/**
 * Contains all the information needed to instantiate robot and drive station components.
 * @author IgKnighters
 */
//WARNING: all variables are subject to change.
public class ElectricalIDs {
	/**
	 * Whether to use solenoids or relays for the pneumatics.
	 */
	public static final boolean pneumatic_solenoid = false;
    /**
     * Contains all the information needed to instantiate any Human Input Devices.
     */
    public static class HIDs{
        public static final int LEFT_JOYSTICK      = 1;
        public static final int RIGHT_JOYSTICK     = 2;
        public static final int GAMEPAD            = 3;
        public static final int LEFT_KINECT_STICK  = 1;
        public static final int RIGHT_KINECT_STICK = 2;
    }
    /**
     * Contains all of the Jaguar IDs.
     */
    public static class CANJaguars{
        public static final int FORWARD_LEFT_DRIVE_JAGUAR  = 10;
        public static final int FORWARD_RIGHT_DRIVE_JAGUAR = 8;
        public static final int BACK_LEFT_DRIVE_JAGUAR     = 2;
        public static final int BACK_RIGHT_DRIVE_JAGUAR    = 6;
        public static final int RIGHT_ACQUISITION_JAGUAR =9;
        public static final int LEFT_ACQUISITION_JAGUAR =11;
        public static final int STORAGE_JAGUAR =12;
        //public static final int TURRET_ROTATION_JAGUAR =7; // No turret ball bearings!
        public static final int SECOND_SHOOTER_JAGUAR =3;
        public static final int FIRST_SHOOTER_JAGUAR =4;
        public static final int TILTING_JAGUAR=5;
    }
	/**
	 * Contains all of the IDs for digital inputs.
	 */
    public static class DigitalIn{
        public static final int ACQUISITION_IN = 1;
        public static final int LOWER_STORAGE_IN = 2;
        public static final int UPPER_STORAGE_IN = 3;
        public static final int AUTONOMOUS_IS_ENABLED=5;
        public static final int AUTONOMOUS_IS_HYBRID=6;
        public static final int HYBRID_MODE=7;
		public static final int COMPRESSOR_PRESSURE = 10;
    }
	/**
	 * Contains all of the IDs for analog inputs.
	 */
    public static class AnalogIn{
        public static final int GYRO_TEMPERATURE=3;
        public static final int PROXIMITY_SENSOR=5;
		public static final int HORIZONTAL_GYRO=1;
		public static final int VERTICAL_GYRO=2;
    }
	/**
	 * Contains all of the IDs for relays.
	 */
    public static class Relay{
        public static final int CAMERA_LIGHT = 1;
        public static final int LIGHT_SENSORS= 2;
        public static final int SIGNAL_LIGHT_ONE=3;
        public static final int SIGNAL_LIGHT_TWO=4;
		public static final int COMPRESSOR = 5;
		public static final int TILTER_CLAMP=6;
		public static final int PNEUMATIC_BRAKES_ON=7;
		public static final int PNEUMATIC_BRAKES_OFF=8;
	}
	/**
	 * Contains all of the IDs for the solenoids.
	 */
	public static class Solenoid {
		public static final int PNEUMATIC_BRAKES_ON=1;
		public static final int PNEUMATIC_BRAKES_OFF=2;
		public static final int TILTER_CLAMP = 5;
    }
	/**
	 * Contains all of the IDs for Pulse Width Modulation devices.
	 */
    public static class PWM{
        public static final int PROXIMITY_SENSOR_SERVO=1;
		public static final int TILTER_CLAMP_SERVO = 9;
    }
	/**
	 * Contains the IP addresses for any networked devices the cRIO needs to
	 * communicate with directly.
	 */
	public static class Network{
		public static final String AXIS_CAMERA = "10.31.73.11";
	}
}
