package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DriverStationLCD.Line;

/**
 * McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * Primary class for correcting FIRST's supplied CANJaguar class by swallowing otherwise uncaught exceptions
 *
 * Method comments are copied from the original code (CANJaguar.java)
 *
 * @author Ben Pylko and Nick Brown
 */
public class RobotCANJaguar {

    /**
     * Initialize a CANJaguar motor controller at the specified ID
     *
     * @param ID ID of the CANJaguar you wish to initialize
     * @return Returns the initialized CANJaguar
     */
    public static CANJaguar initializeCANJaguar(int ID) {
        try {
            return new CANJaguar(ID);
        } catch (Exception e) {
            IgKnightersBot.robotDriverStation.outputLine(Line.kUser6, "EXCEPTION [ID: " + ID + "]");
            System.out.println("EXCEPTION THROWN (RobotCANJaguar.initializeCANJaguar) [ID: " + ID + "]: " + e.toString());
        }
        return null;
    }

    /**
     * Set the speed of motor associated with the specified CANJaguar
     *
     * @param canjaguar CANJaguar to set the speed of
     * @param speed Speed to set the motor to (Range: [-1,1])
     */
    public static void setX(CANJaguar canjaguar, double speed) {
        try {
            canjaguar.setX(speed);
        } catch (Exception e) {
            IgKnightersBot.robotDriverStation.outputLine(Line.kUser6, "EXCEPTION THROWN (RobotCANJaguar.setX): " + e.toString());
            System.out.println("EXCEPTION THROWN (RobotCANJaguar.setX): " + e.toString());
        }
    }

    /**
     * Get the status of the forward limit switch
     *
     * @param canjaguar CANJaguar to check the limit switch of
     * @return The motor is allowed to turn in the forward direction when true (returns false if an exception is thrown)
     */
    public static boolean getForwardLimitOK(CANJaguar canjaguar) {
        try {
            return canjaguar.getForwardLimitOK();
        } catch (Exception e) {
            IgKnightersBot.robotDriverStation.outputLine(Line.kUser6, "EXCEPTION THROWN (RobotCANJaguar.getForwardLimitOK): " + e.toString());
            System.out.println("EXCEPTION THROWN (RobotCANJaguar.getForwardLimitOK): " + e.toString());
        }
        return false;
    }

    /**
     * Get the status of the reverse limit switch.
     *
     * @param canjaguar CANJaguar to check the limit switch of
     * @return The motor is allowed to turn in the reverse direction when true (returns false if an exception is thrown)
     */
    public static boolean getReverseLimitOK(CANJaguar canjaguar) {
        try {
            return canjaguar.getReverseLimitOK();
        } catch (Exception e) {
            IgKnightersBot.robotDriverStation.outputLine(Line.kUser6, "EXCEPTION THROWN (RobotCANJaguar.getReverseLimitOK): " + e.toString());
            System.out.println("EXCEPTION THROWN (RobotCANJaguar.getReverseLimitOK): " + e.toString());
        }
        return false;
    }

    /**
     * Get the voltage being output from the motor terminals of the Jaguar.
     *
     * @return The output voltage in Volts.
     */
    public static double getOutputVoltage(CANJaguar canjaguar) {
        try {
            return canjaguar.getOutputVoltage();
        } catch (Exception e) {
            IgKnightersBot.robotDriverStation.outputLine(Line.kUser6, "EXCEPTION THROWN (RobotCANJaguar.getOutputVoltage): " + e.toString());
            System.out.println("EXCEPTION THROWN (RobotCANJaguar.getOutputVoltage): " + e.toString());
        }
        return 0.0;
    }

    /**
     * Get the voltage at the battery input terminals of the Jaguar.
     *
     * @return The bus voltage in Volts.
     */
    public static double getBusVoltage(CANJaguar canjaguar) {
        try {
            return canjaguar.getBusVoltage();
        } catch (Exception e) {
            IgKnightersBot.robotDriverStation.outputLine(Line.kUser6, "EXCEPTION THROWN (RobotCANJaguar.getBusVoltage): " + e.toString());
            System.out.println("EXCEPTION THROWN (RobotCANJaguar.getBusVoltage): " + e.toString());
        }
        return 0.0;
    }
}
