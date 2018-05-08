package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;

public class AutonomousDrivePercentGyro extends Command implements PIDOutput {

	private float driveSpeed, startAngle;
	private double differential;
	private boolean isComplete;
	private PIDController turnController;
	public AutonomousDrivePercentGyro(float speed) {
		requires(Robot.drives);
		driveSpeed = speed;
		startAngle = (float)Robot.drives.getIMU().getAngle();
		isComplete = false;
		turnController = new PIDController(0.03, 0.0, 0.0, Robot.drives.getIMU(), this);
		turnController.setInputRange(-180.0f, 180.0f);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(2.0f);
		turnController.setContinuous(true);
		turnController.disable();
	}
	@Override
	protected void initialize() {
		System.out.println("[COMMAND] AutonomousDrivePercentGyro(" + driveSpeed + ")!");
		isComplete = false;
		turnController.setSetpoint(startAngle);
		turnController.enable();
		//imu.reset(); icky icky nono because we need the correct angle for turning to points
	}
	@Override
	protected void execute() {
		Robot.drives.getDriveController().arcadeDrive(driveSpeed, differential);
	}
	
    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return isComplete;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Robot.drives.stop();
    	turnController.disable();
    	isComplete = true;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	end();
    }
    
	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		differential = output;
	}

}
