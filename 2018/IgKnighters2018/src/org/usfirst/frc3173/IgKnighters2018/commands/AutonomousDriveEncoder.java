package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.RobotMap;
import org.usfirst.frc3173.IgKnighters2018.utilities.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;

public class AutonomousDriveEncoder extends Command {
	
	private WPI_TalonSRX leftMotor, rightMotor;
	private int leftTicks, rightTicks;
	
	public AutonomousDriveEncoder(int leftTicks, int rightTicks) {
		leftMotor = RobotMap.drivesLeftMaster;
		rightMotor = RobotMap.drivesRightMaster;
		this.leftTicks = leftTicks;
		this.rightTicks = rightTicks;
	}
	
	@Override
	protected void initialize() {
		leftMotor.setSelectedSensorPosition(0, 0, 0);
		rightMotor.setSelectedSensorPosition(0, 0, 0);
	}
	
	@Override
	protected void execute() {
		leftMotor.set(ControlMode.Position, leftTicks);
		rightMotor.set(ControlMode.Position, rightTicks);
	}

	@Override
	protected boolean isFinished() {
		int leftDif = Math.abs(leftMotor.getSelectedSensorPosition(0) - leftTicks);
		int rightDif = Math.abs(rightMotor.getSelectedSensorPosition(0) - rightTicks);
		return false; //leftDif < Constants.LEFT_DRIVES_TICKS_PER_REV && rightDif < Constants.RIGHT_DRIVES_TICKS_PER_REV && leftMotor.get() < 0.2 && rightMotor.get() < 0.2;
	}
	
	@Override
	protected void end() {
		System.out.println(leftMotor.getSelectedSensorPosition(0) + " : " + rightMotor.getSelectedSensorPosition(0));
	}
	
	@Override
	protected void interrupted() {
		end();
	}
}
