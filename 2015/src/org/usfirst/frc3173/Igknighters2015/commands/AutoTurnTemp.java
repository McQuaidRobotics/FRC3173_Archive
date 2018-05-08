package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoTurnTemp extends Command {
int d=0;
    public AutoTurnTemp(int direction) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	d=direction;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drives.setDriveSpeed(d*.8, d*.8);
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
