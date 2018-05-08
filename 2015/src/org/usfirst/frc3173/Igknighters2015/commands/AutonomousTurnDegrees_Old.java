package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomousTurnDegrees_Old extends Command {
	
	//Orientation measured clockwise
	private double currentOrientation = 0;
	private double desiredOrientation = 0;
	private double speed;
	
    public AutonomousTurnDegrees_Old(double degrees, boolean clockwise, double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	// negative = counterclockwise, positive || 0 = clockwise
    	requires(Robot.drives);
    	this.speed = speed;
    	
    	desiredOrientation = Robot.imu.getZOrientation();
    	currentOrientation = desiredOrientation; //Rather than having to do a second sensor call in case java is stupid
    	
    	if(clockwise)
    	{
    		desiredOrientation += degrees;
    	}
    	else
    	{
    		desiredOrientation -= degrees;
    		this.speed *= -1;
    	}	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//Call TurnToOrientation's overloaded constructor
    	new TurnToOrientation(desiredOrientation, currentOrientation, speed).start();;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Slow down once within SLOWRANGE times the desired accuracy
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
