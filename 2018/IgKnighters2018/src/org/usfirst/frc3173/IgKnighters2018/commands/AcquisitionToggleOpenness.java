package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AcquisitionToggleOpenness extends Command {
	
	private boolean isComplete;
	
	public AcquisitionToggleOpenness() {
		isComplete = false;
	}
	
	@Override
	protected void initialize() {
		isComplete = false;
	}
	
	@Override
	protected void execute() {
		if (Robot.acquisition.isOpen()) {
			Robot.acquisition.closeAcq();
		} else {
			Robot.acquisition.openAcq();
		}
		isComplete = true;
	}
	
	@Override
	protected boolean isFinished() {
		return isComplete;
	}
}
