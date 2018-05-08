package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.subsystems.Acquisition;

import edu.wpi.first.wpilibj.command.Command;

public class AcquisitionPivotChangePosition extends Command {
	private boolean isComplete;
	private Acquisition.AcquisitionPivotStates state;
	
	public AcquisitionPivotChangePosition(Acquisition.AcquisitionPivotStates state) {
		isComplete = false;
		this.state = state;
	}
	
	@Override
	protected void execute() {
		switch (state) {
		case RESET:
			Robot.acquisition.resetPivot();
			break;
		case DEFAULT:
			Robot.acquisition.defaultPivot();
			break;
		case CLIMB:
			Robot.acquisition.climbPivot();
		}
		isComplete = true;
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return isComplete;
	}
	
	

}
