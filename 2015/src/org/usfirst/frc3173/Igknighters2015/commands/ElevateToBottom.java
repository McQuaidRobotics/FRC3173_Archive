package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;
import org.usfirst.frc3173.Igknighters2015.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ElevateToBottom extends Command {
	
	
    public ElevateToBottom() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.elevation.elevate(Robot.elevation.LOW-15);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return ((Math.abs(Robot.elevation.getPosition())<Robot.elevation.TOTE)||(!RobotMap.elevationElevationMotor.isRevLimitSwitchClosed()));
    }
    // Called once after isFinished returns true
    protected void end() {
    	Robot.elevation.elevate(Robot.elevation.getPosition());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
