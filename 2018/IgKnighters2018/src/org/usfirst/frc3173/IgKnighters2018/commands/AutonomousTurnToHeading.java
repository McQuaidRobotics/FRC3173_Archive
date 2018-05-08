package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.RobotMap;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousTurnToHeading extends Command implements PIDOutput {
	private float desiredAngle;
	private double rotateToAngleRate;
	private PIDController turnController;
	
	public AutonomousTurnToHeading(float angle) {
		requires(Robot.drives);
		turnController = new PIDController(0.03, 0.0, 0.0, Robot.drives.getIMU(), this);
		turnController.setInputRange(-180.0f, 180.0f);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(2.0f);
		turnController.setContinuous(true);
		turnController.disable();
		desiredAngle = angle;
	}
	@Override
	protected void initialize() {
		System.out.println("[COMMAND] AutonomousTurnToHeading(" + desiredAngle +")!");
		turnController.setSetpoint(desiredAngle);
		turnController.enable();
		Robot.drives.getIMU().reset(); //icky icky nono because we need the correct angle for turning to points
	}
	@Override
	protected void execute() {
		Robot.drives.getDriveController().arcadeDrive(0, rotateToAngleRate);
		//SmartDashboard.putNumber("Rotate to angle rate", rotateToAngleRate);
	}
	/*public AutonomousTurn(double angle) {
		requires(Robot.drives);
		imu = Robot.drives.getIMU();
		startAngle = angle;
	}
	// Called just before this Command runs the first time
    @Override
    protected void initialize() {
		desiredAngle = startAngle;
		isComplete = false;
		imu.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	double diffAngle = imu.getAngle() - desiredAngle;
    	SmartDashboard.putNumber("diff angle", diffAngle);
    	double multiplier = Math.abs(diffAngle) / desiredAngle;
    	if (multiplier > 1) multiplier = 1;
    	if (diffAngle > 1) Robot.drives.getDriveController().arcadeDrive(0, -.5 + .5 * -multiplier);
    	else if (diffAngle < -1) Robot.drives.getDriveController().arcadeDrive(0, .5 + .5 * multiplier);
    	else isComplete = true;
    }
*/
    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return rotateToAngleRate < 0.01;
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
    
	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		rotateToAngleRate = output;
	}
}