package org.usfirst.frc3173.IgKnighters2018.utilities;

import org.usfirst.frc3173.IgKnighters2018.subsystems.Drives;

public class Constants {
	public static final float DRIVES_DEFAULT_RAMP_TIME = 0.45f; // in seconds .35
	public static final float DRIVES_HIGH_SPEED = 1f;
	public static final float DRIVES_LOW_SPEED = 0.62f;
	
	public static final float DRIVES_P_VALUE = 0.1f;
	public static final float DRIVES_I_VALUE = 0f;
	public static final float DRIVES_D_VALUE = 0f;
	public static final float DRIVES_F_VALUE = 0.01f;
	
	public static final int LEFT_DRIVES_TICKS_PER_REV = 96598; //115000;
	public static final int RIGHT_DRIVES_TICKS_PER_REV = 96598; //115000;
	public static final float DRIVES_WHEEL_DIAMETER_INCHES = 4f;
	public static final double DRIVES_TICKS_PER_IN = (LEFT_DRIVES_TICKS_PER_REV / (DRIVES_WHEEL_DIAMETER_INCHES * Math.PI));
	
	public static final float ACQUISITION_DEFAULT_INTAKE_RAMP_TIME = 0.1f; // in seconds
	public static final float ACQUISITION_DEFAULT_INTAKE_SPEED = 0.8f; // in percentage, ex. 0.5 = 50% speed
	public static final float ACQUISITION_DEFAULT_REVERSE_SPEED = 1f; // in percentage, ex. 0.5 = 50% speed
	
	public static final float ACQUISITION_P_VALUE = 10f;
	public static final float ACQUISITION_I_VALUE = 0f;
	public static final float ACQUISITION_D_VALUE = 0f;
	public static final float ACQUISITION_F_VALUE = 0.1f;
	
	public static final int ACQUISITION_PIVOT_RESET_POS = -26; //-197
	public static final int ACQUISITION_PIVOT_DEFAULT_POS = -551; //-550 ticks;
	public static final int ACQUISITION_PIVOT_CLIMB_POS = -270;
	
	public static final int ELEVATOR_SCALE_HEIGHT_TICKS = 14000; //850000;
	public static final int ELEVATOR_SWITCH_HEIGHT_TICKS = 10000; //200000
	public static final int ELEVATOR_VAULT_HEIGHT_TICKS = 1000;
	public static final int ELEVATOR_FLOOR_HEIGHT_TICKS = 0;
	
	public static final Drives.OperativeState DEFAULT_OPERATIVE_STATE = Drives.OperativeState.ARCADE_DRIVE;
	
	public static final String DRIVERSTATION_ONE_STRING = "ds_one";
	public static final String DRIVERSTATION_TWO_STRING = "ds_two";
	public static final String DRIVERSTATION_THREE_STRING = "ds_three";
	
	public static final String AUTONOMOUS_PROTOCOL_PASS_LINE = "pass_line";
	public static final String AUTONOMOUS_PROTOCOL_SWITCH = "switch";
	public static final String AUTONOMOUS_PROTOCOL_SCALE = "scale";
	
	public static final long PIXELS_TO_METERS = 0;
}
