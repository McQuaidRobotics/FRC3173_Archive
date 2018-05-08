package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomousAcquireTote extends Command {

	private boolean elevate;
	
    public AutonomousAcquireTote(boolean elevate) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.elevation);
    	requires(Robot.drives);
    	
    	this.elevate = elevate;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.elevation.openRearPneumatic();
    	Robot.acquisition.setRightAcquisitionBackward();
    	Robot.acquisition.setLeftAcquisitionBackward();
    	Robot.drives.setDriveSpeed(Autonomous.AUTO_DRIVE_SPEED,Autonomous.AUTO_DRIVE_SPEED);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       return !Robot.acquisition.getLimitSwitch();
    }

    // Called once after isFinished returns true
    protected void end() {	
    	Robot.drives.setDriveSpeed(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
