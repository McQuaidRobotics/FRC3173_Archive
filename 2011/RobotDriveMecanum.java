package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;

/**
 * McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * Primary class for controlling the robot's mecanum drive subsystem
 *
 * @author Nick Brown and Ben Pylko
 */
public class RobotDriveMecanum {

    private CANJaguar m_FLDriveCANJaguar; // Front left drive motor
    private CANJaguar m_FRDriveCANJaguar; // Front right drive motor
    private CANJaguar m_RLDriveCANJaguar; // Rear left drive motor
    private CANJaguar m_RRDriveCANJaguar; // Rear right drive motor

    /**
     * Class for controlling the robot's mecanum drive system (uses pre-initiated objects)
     * @param FLDriveCANJaguar Front left drive motor
     * @param FRDriveCANJaguar Front right drive motor
     * @param RLDriveCANJaguar Rear left drive motor
     * @param RRDriveCANJaguar Rear right drive motor
     */
    public RobotDriveMecanum(CANJaguar FLDriveCANJaguar, CANJaguar FRDriveCANJaguar, CANJaguar RLDriveCANJaguar, CANJaguar RRDriveCANJaguar) {
        m_FLDriveCANJaguar = FLDriveCANJaguar;
        m_FRDriveCANJaguar = FRDriveCANJaguar;
        m_RLDriveCANJaguar = RLDriveCANJaguar;
        m_RRDriveCANJaguar = RRDriveCANJaguar;
    }

    /**
     * Class for controlling the robot's mecanum drive system (uses object IDs)
     * @param FLDriveCANJaguar Front left drive motor ID
     * @param FRDriveCANJaguar Front right drive motor ID
     * @param RLDriveCANJaguar Rear left drive motor ID
     * @param RRDriveCANJaguar Rear right drive motor ID
     */
    public RobotDriveMecanum(int FLDriveCANJaguarID, int FRDriveCANJaguarID, int RLDriveCANJaguarID, int RRDriveCANJaguarID) {
        m_FLDriveCANJaguar = RobotCANJaguar.initializeCANJaguar(FLDriveCANJaguarID);
        m_FRDriveCANJaguar = RobotCANJaguar.initializeCANJaguar(FRDriveCANJaguarID);
        m_RLDriveCANJaguar = RobotCANJaguar.initializeCANJaguar(RLDriveCANJaguarID);
        m_RRDriveCANJaguar = RobotCANJaguar.initializeCANJaguar(RRDriveCANJaguarID);
    }

    /**
     * Method to control drive system using mecanum wheels
     * @param x Strafe (side to side movement); Positive is right, vice-versa
     * @param y Velocity (forward/backward movement); Positive is forwards, vice-versa
     * @param rotation Rotation (clockwise/counterclockwise movement); Positive is clockwise, vice-versa
     */
    public void mecanumDrive(double x, double y, double rotation) {
        // Calculates the required motorspeed for each wheel
        double FLMotor_val = y + rotation + x; //Front Left wheel, etc...
        double FRMotor_val = -(y - rotation - x);
        double RLMotor_val = y + rotation - x;
        double RRMotor_val = -(y - rotation + x);

        // Sets the calculated motorspeed for each wheel
        RobotCANJaguar.setX(m_FLDriveCANJaguar, FLMotor_val);
        RobotCANJaguar.setX(m_FRDriveCANJaguar, FRMotor_val);
        RobotCANJaguar.setX(m_RLDriveCANJaguar, RLMotor_val);
        RobotCANJaguar.setX(m_RRDriveCANJaguar, RRMotor_val);
    }

    /**
     * Method to control drive system using mecanum wheels with gyro correction (absolute drive)
     * @param x Strafe (side to side movement); Positive is right, vice-versa
     * @param y Velocity (forward/backward movement); Positive is forwards, vice-versa
     * @param rotation Rotation (clockwise/counterclockwise movement); Positive is clockwise, vice-versa
     * @param gyro Angle from gyro
     */
    public void mecanumDriveAbsolute(double x, double y, double rotation, double gyro) {

        gyro = gyro % 360; // Standardizes angle measurement to be out of 360

        double[] newAxes = rotateVector(x, y, gyro);

        x = newAxes[0];
        y = newAxes[1];

        // Calculates the required motorspeed for each wheel
        double FLMotor_val = y + rotation - x; //Front Left wheel
        double FRMotor_val = -(y - rotation + x); //Front Right wheel
        double RLMotor_val = y + rotation + x; //Rear Left wheel
        double RRMotor_val = -(y - rotation - x); //Rear Right wheel

        // Sets the calculated motorspeed for each wheel
        RobotCANJaguar.setX(m_FLDriveCANJaguar, FLMotor_val);
        RobotCANJaguar.setX(m_FRDriveCANJaguar, FRMotor_val);
        RobotCANJaguar.setX(m_RLDriveCANJaguar, RLMotor_val);
        RobotCANJaguar.setX(m_RRDriveCANJaguar, RRMotor_val);
    }

    /**
     * Rotates the vector to create new axes for absolute drive
     * @param x X value to be rotated
     * @param y Y value to be rotated
     * @param angle Input double from the gyro
     * @return Returns a double array, where out[0] is the returned x value and out[1] is the y value
     */
    public double[] rotateVector(double x, double y, double angle) {
        double cosA = Math.cos(angle * (Math.PI / 180.0));
        double sinA = Math.sin(angle * (Math.PI / 180.0));
        double out[] = new double[2]; //        x,y
        out[0] = -x * cosA + y * sinA;//out[0] [-|-]
        out[1] = x * sinA + y * cosA;// out[1] [+|-]
        return out;
    }
}
