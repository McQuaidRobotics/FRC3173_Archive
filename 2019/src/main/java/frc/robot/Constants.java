    package frc.robot;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class Constants {
    public static boolean SMARTDASHBOARD_ENABLE = false;  // bane of my existance
    public static boolean SMARTDASHBOARD_ENABLE_DEBUGINFO = true;  // possibly used to draw debug stuff

    // Drives
    public static int DRIVES_TICKS_PER_REV = 1024 * 4; //cycles per rev, four ticks per cycle
    public static double DRIVES_WHEEL_DIAM_IN = 5.875;
    public static double DRIVES_WHEELBASE_FT = 20.75 / 12;
    public static double DRIVES_TICKS_PER_IN = DRIVES_TICKS_PER_REV / (DRIVES_WHEEL_DIAM_IN * Math.PI);
    public static double DRIVES_MAX_VEL = 12.51; // in feet  // calculated max is 13.51, but 12.51 just to be safe
    public static double DRIVES_MAX_ACC = 1.76; //in feet per s per s
    public static double DRIVES_ARCADE_SPIN_MULTIPLIER = 0.765;

    // Hoist auto-climb
    public static double AUTOCLIMB_HOIST_REVS_TO_SET = 1.0;

    // Auto-Climb
    public static boolean AUTOCLIMB_IS_ACTIVE = false;
    //public static boolean DEBUG_WAIT = false;  // TODO: FOR BUDDY CLIMB (3/27), CHANGE TO TRUE ON SHUFFLEBOARD
    public static boolean DEBUG_WAIT = true;
    //public static final double REV_NEO_MAX_RPMS = 5700;  // TODO: FOR BUDDY CLIMB (3/27), CHANGE TO 1500 ON SHUFFLEBOARD
    public static final double REV_NEO_MAX_RPMS = 1500 * 2;
    public static final double ARMS_WINCH_DIAMETER = 2.439;  // inches
    public static final double PANTS_WINCH_DIAMETER = 2.418; // inches

    public static double PANTS_DELTA = (ARMS_WINCH_DIAMETER - PANTS_WINCH_DIAMETER) / PANTS_WINCH_DIAMETER; // % faster pants will move
    public static double PANTS_RPMS = REV_NEO_MAX_RPMS;
    public static double ARMS_RPMS = PANTS_RPMS / (1 + PANTS_DELTA);
    public static double URGE_RPMS = -REV_NEO_MAX_RPMS; // Make sure this is a negative # to go foward
    public static double URGE_REVOLUTIONS = -2.4;  // Make sure this is a negative # to go foward

    // Drive Current limits
    public static int DRIVE_PEAK_CURRENT_LIMIT = 40;  //amps
    public static int DRIVE_PEAK_CURRENT_DURATION = 100;  //ms
    public static int DRIVE_CONTINUOUS_CURRENT_LIMIT = 30; //amps

    public static double ELEVATOR_MINVEL = 0;
    public static double ELEVATOR_MAXVEL = 5700;
    public static double ELEVATOR_MAXACC = 2500;

    public static boolean ELEVATOR_MANUAL_USE_PID = true;
    public static double ELEVATOR_MAX_MANUAL_WITHOUT_PID = 3000;
    
    /*
    // Climb (Pants and Arms)
    // Not implemented - would be used to gyro stablize climb
    public static double CLIMB_TILT_TARGET_DEG = 5; //placeholder, how much we want the robot to tip in teh roll dimension when its climbing!!
    */

    // Climb Encoder values
    public static double NEO_TICKS_PER_REVOLUTION =  42;
    public static double URGERS_GEAR_REDUCTION    =  64;

    /*
    // Not implemented - would be used to encoder failsafes for climb appendages
    public static double CLIMB_PANTS_FLOOR_ENGAGED = 65;  // ESTIMATED (Chuck).  Pants range should be ~ arms range.  It's 65 ticks more.
    public static double CLIMB_PANTS_END =          294;  // Measured when pants fully dropped 
    public static double CLIMB_ARMS_END =           229;  // Measured when arms at Bottom 
    */

    // Climb current 
    public static double PANTS_SURFACE_OUTPUT = 13;  // amps for when pants hitting surface
    public static double ARMS_SURFACE_OUTPUT = 8;  // amps for when arms hitting surface
    public static int CLIMB_PANTS_STALL_CURRENT =    28;  // Max measured current (triple climb) = 24.6A
    public static int CLIMB_ARMS_STALL_CURRENT =     15;  // Max measured current (triple climb) = 13.1A

    // Elevator
    // 15 motor rotations = 1 winch rotation
    // 1 winch rotation = 7.2884 inches
    // therefore, 15 motor rotations = 7.2884 inches
    // THEREFORE, 1 motor rotation = 0.48589333333333334 inches
    public static double ELEVATOR_INCH_PER_WINCH_REV = 7.28828;  // maybe don't use this, i messed this up bad
    public static double ELEVATOR_MOTOR_ROTATIONS_TO_WINCH_REV = 10;  // GEAR REDUCTION, how many rotations the motor goes before the winch makes a revolution
    public static double ELEVATOR_WINCH_DIAMETER = 2.32;
    public static double ELEVATOR_WINCH_CIRCUMFERENCE = ELEVATOR_WINCH_DIAMETER * Math.PI;
    public static double ELEVATOR_TICKS_TOP = 999999;  // placeholder value for the tick count at the top of elevator
    public static double ELEVATOR_WINCH_INCHES_FLOOR = 0;  // 0 inches
    public static double ELEVATOR_WINCH_INCHES_MIDDLE = (2 * 12) + 4;  // 2 feet 4 in.
    public static double ELEVATOR_WINCH_INCHES_TOP = ELEVATOR_WINCH_INCHES_MIDDLE + 19;  // one foot above middle

    public static double ELEVATOR_MOTOR_REV_TO_INCHES = ELEVATOR_WINCH_CIRCUMFERENCE / ELEVATOR_MOTOR_ROTATIONS_TO_WINCH_REV;

    public static boolean ELEVATOR_DEBUG_PRINTS = true;

    public static double ELEVATOR_POSITION_KP = 0.035;
    public static double ELEVATOR_POSITION_KI = 0.0;
    public static double ELEVATOR_POSITION_KD = 0.0;
    // the following velocity PID values were
    // taken from the sbpli branch on commit
    // 70bd22a872187a41bf395f079323d7127facac46
    public static double ELEVATOR_VELOCITY_KP = 5e-5;
    public static double ELEVATOR_VELOCITY_KI = 1e-6;
    public static double ELEVATOR_VELOCITY_KD = 0.0;

    // public static double ELEVATOR_BOTTOM = 0.0;  // placeholder
    // public static double ELEVATOR_MIDDLE = -41.0;  // placeholder
    // public static double ELEVATOR_TOP = -71.0;  // placeholder
    public static int ELEVATOR_SMARTCURRENT_LIMIT = 30;  
    public static int ELEVATOR_STALL = 34;

    public static float DRIVES_P = 0.5f;
    public static float DRIVES_I = 0.0f;
    public static float DRIVES_D = 0.0f;
    public static float DRIVES_F = 0.0f;

    public static float IMU_P = 0.1f;
    public static float IMU_I = 0.0f;
    public static float IMU_D = 0.0f;
    public static float IMU_F = 0.0f;
    
    public static void reloadFloats(String filename) {
        float[] file = readFloatFile("/home/lvuser/deploy/pidf.txt", false);
        DRIVES_P = file[0];
        DRIVES_I = file[1];
        DRIVES_D = file[2];
        DRIVES_F = file[3];
    }

    private static float[] readFloatFile(String filename, boolean ignore) {
        if(ignore) {
            reloadFloats(filename);
            return new float[] {0.0f, 0.0f, 0.0f, 0.0f};
        }
        try {
            Scanner scan = new Scanner(new File(filename));
            List<String> lines = new ArrayList<String>();
            while(scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            scan.close();  // This is not a python context manager (with open) Mr. Colton.  You need to clean up after yourself.
            String[] sf = lines.toArray(new String[0]);
            return new float[] {Float.parseFloat(sf[0]), Float.parseFloat(sf[1]), Float.parseFloat(sf[2]), Float.parseFloat(sf[3])};
        } catch (java.io.FileNotFoundException e) {
            System.out.println("error: file not found. pidf values for drives not loaded from file");
            return new float[] {0.0f, 0.0f, 0.0f, 0.0f};
        }
    }
}