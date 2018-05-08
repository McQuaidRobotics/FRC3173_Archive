package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomousTurn90DeadRecon extends Command {
	
	public boolean clockwise;
	public double speed=Autonomous.AUTO_DRIVE_SPEED;
	public static final double TIME=.57;  //TODO:Find time
    public AutonomousTurn90DeadRecon(boolean clockwise) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.clockwise=clockwise;
    	requires(Robot.drives);
    	
    	if(!clockwise){ //counterclockwise 
    		speed *= -1; //do opposite
    	}
    	
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drives.setDriveSpeed(speed, -1*speed);
    	System.out.println("start");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drives.setDriveSpeed(0, 0);
    	System.out.println("end");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
