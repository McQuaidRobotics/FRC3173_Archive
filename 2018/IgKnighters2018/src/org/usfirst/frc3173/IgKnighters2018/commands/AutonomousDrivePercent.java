package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class AutonomousDrivePercent extends Command {
	private double leftSpeed, rightSpeed;
	public AutonomousDrivePercent(double leftSpeed, double rightSpeed) {
		requires(Robot.drives);
		this.leftSpeed = leftSpeed;
		this.rightSpeed = rightSpeed;
	}
	// Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	System.out.println("[COMMAND] AcquisitionDrivePercent(" + leftSpeed + ", " + rightSpeed + ")");
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	RobotMap.drivesLeftMaster.set(ControlMode.PercentOutput, leftSpeed);
    	RobotMap.drivesRightMaster.set(ControlMode.PercentOutput, rightSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Robot.drives.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	end();
    }
}
