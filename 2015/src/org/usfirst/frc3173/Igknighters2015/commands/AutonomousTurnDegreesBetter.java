package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;
import org.usfirst.frc3173.Igknighters2015.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomousTurnDegreesBetter extends Command {
	private double currentOrientation = 0;
	private double desiredOrientation = 0;
	private double speed = Autonomous.AUTO_TURN_SPEED;
	private final double  error = 5;
	private final double SLOWRANGE = 55;
	private final double SLOWDIVIDER = 2;
	double delta;
	
    public AutonomousTurnDegreesBetter(double degrees) {
    	requires(Robot.drives);
    	RobotMap.drivesFirstLeft.setVoltageRampRate(0);
    	RobotMap.drivesSecondLeft.setVoltageRampRate(0);
    	RobotMap.drivesFirstRight.setVoltageRampRate(0);
    	RobotMap.drivesSecondRight.setVoltageRampRate(0);

    	currentOrientation = Robot.imu.getZOrientation(); //Rather than having to do a second sensor call in case java is stupid
    	
    	desiredOrientation = Math.abs(currentOrientation + degrees) % 360 - 2;	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//Turns right if clockwise, left otherwise
    	Robot.imu.setOrientation(1);
    	Robot.drives.setDriveSpeed(-speed,  speed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	currentOrientation = Robot.imu.getZOrientation();
    	
    	if(Robot.imu.getZRotation() * .6 > Math.abs(desiredOrientation-currentOrientation))
    	{
        	Robot.drives.setDriveSpeed(-.2,.2);
    	}
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//Returns true if robot is within +- error degrees
		//return (Math.abs(currentOrientation-desiredOrientation) <= error);
    	return currentOrientation > desiredOrientation;
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
