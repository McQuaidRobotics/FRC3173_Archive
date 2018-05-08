package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;

public class AcquisitionPivotChangePositionPercent extends Command {

	private WPI_TalonSRX pivot = RobotMap.acquisitionPivot;
	private float speed;
	public AcquisitionPivotChangePositionPercent(float percent) {
		speed = percent;
	}
	@Override
	protected void initialize() {
		System.out.println("[AcquisitionPivotChangePositionPercent] init! setting to " + speed);
		pivot.set(ControlMode.PercentOutput, speed);
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void end() {
		pivot.set(ControlMode.PercentOutput, 0);
	}
	@Override
	protected void interrupted() {
		end();
	}
	
}
