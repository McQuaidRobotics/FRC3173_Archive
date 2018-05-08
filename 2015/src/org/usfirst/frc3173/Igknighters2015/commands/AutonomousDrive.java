package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;
import org.usfirst.frc3173.Igknighters2015.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomousDrive extends Command {

	private double left,right;
    public AutonomousDrive(double left,double right) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drives);
    	this.left=left;
    	this.right=right;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drives.setRampRate(0);
    	Robot.drives.setDriveSpeed(left, right);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       // return !Robot.acquisition.getLimitSwitch();
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drives.setRampRate(Autonomous.RAMP_RATE);
    	Robot.drives.setDriveSpeed(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
