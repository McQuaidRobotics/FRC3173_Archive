package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomousElevate extends Command {

	private double position;
	
    public AutonomousElevate(double position) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	//requires(Robot.elevation);
    	Robot.elevation.positionMode();
    	this.position=position;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("start");
    	Robot.elevation.elevate(position); //Autonomous puts the motor in position mode so this works
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.elevation.positionMode();
    	Robot.elevation.elevate(position);

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	System.out.println("end");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
