package org.usfirst.frc3173.IgKNIGHTers.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc3173.IgKNIGHTers.OI;
import org.usfirst.frc3173.IgKNIGHTers.Robot;

/**
 * @author JacobLaptop
 */
public class LowSpeedMotor extends CommandGroup {

    public LowSpeedMotor() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.climber.RotateClimberMotor(.05);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
