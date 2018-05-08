package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;

public class AcquisitionPivotPercent extends Command {
	
	private WPI_TalonSRX pivot = RobotMap.acquisitionPivot;
	private float leftPercent;
	public AcquisitionPivotPercent(float leftPercent) {
		this.leftPercent = leftPercent;
	}
	
	@Override
	protected void initialize() {
		
	}
	
	@Override
	protected void execute() {
		pivot.set(leftPercent);
	}
	
	@Override
	protected void end() {
		pivot.set(0);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
	protected void interrupted() {
		end();
	}
	
}
