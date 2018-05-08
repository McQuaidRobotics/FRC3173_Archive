package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ElevateToOneToteHeight extends Command {

    public ElevateToOneToteHeight() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.elevation.elevate(Robot.elevation.TWO_TOTES-15); //practice bot: 225
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (Robot.elevation.getPosition()<=Robot.elevation.TWO_TOTES && Robot.elevation.getPosition()>=Robot.elevation.TWO_TOTES-20);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
