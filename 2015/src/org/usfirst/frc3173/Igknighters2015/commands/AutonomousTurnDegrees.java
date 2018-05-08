package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;
import org.usfirst.frc3173.Igknighters2015.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomousTurnDegrees extends Command {
	private double currentOrientation = 0;
	private double desiredOrientation = 0;
	private double speed = Autonomous.AUTO_TURN_SPEED;
	private final double  error = 5;
	private boolean clockwise;
	
    public AutonomousTurnDegrees(double degrees, boolean clockwise) {
    	requires(Robot.drives);
    	RobotMap.drivesFirstLeft.setVoltageRampRate(0);
    	RobotMap.drivesSecondLeft.setVoltageRampRate(0);
    	RobotMap.drivesFirstRight.setVoltageRampRate(0);
    	RobotMap.drivesSecondRight.setVoltageRampRate(0);

    	this.clockwise = clockwise;
    	
    	currentOrientation = Robot.imu.getZOrientation();
    																	   //arbitrary fudge factor
    	desiredOrientation = Math.abs(currentOrientation + degrees) % 360 - 2;
    	//TODO: auto choose direction based on difference in desired and starting orientation?
    }

    //Called just before this Command runs the first time
    protected void initialize() {
    	//Start spinning in chosen direction
    	if(clockwise)
    	{
    		Robot.drives.setDriveSpeed(-speed, speed);
    	}
    	else
    	{
    		Robot.drives.setDriveSpeed(speed, -speed);
    	}
    }

    //Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//update currentOrientation with most recent imu read
    	currentOrientation = Robot.imu.getZOrientation();
    	
    	//Degrees that will be covered in next .6 seconds //TODO: try lower?
    	double predictedDelta = Robot.imu.getZRotation() * .6;
    	//Number of degrees left to get from current orientation to desired orientation.
    	double degreesRemaining =  Math.abs(desiredOrientation-currentOrientation);
    	
    	/*
    	 * If predicted amount of degrees that will be covered in the next .6
    	 * seconds is greater than the amount of degrees remaining between
    	 * current orientation and desired orientation, slow down.
    	 */
    	if(Math.abs(predictedDelta) > degreesRemaining)
    	{
        	if(clockwise)
        	{
        		Robot.drives.setDriveSpeed(-.2,.2);
        	}
        	else
        	{
        		Robot.drives.setDriveSpeed(.2,-.2);
        	}
    	}
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//Returns true if robot is within +- error degrees
		return (Math.abs(currentOrientation-desiredOrientation) <= error);
    }

    // Called once after isFinished returns true
    protected void end() {
    	//Stop turning
    	Robot.drives.setDriveSpeed(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
