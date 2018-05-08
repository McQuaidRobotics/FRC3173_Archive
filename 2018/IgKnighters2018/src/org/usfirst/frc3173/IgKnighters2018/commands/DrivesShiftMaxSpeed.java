package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.utilities.Constants;

import edu.wpi.first.wpilibj.command.Command;

public class DrivesShiftMaxSpeed extends Command {
	private boolean isComplete;
	public static enum SpeedMaxStates {
		HALF, FULL
	}
	private SpeedMaxStates state;

	public DrivesShiftMaxSpeed(SpeedMaxStates state) {
		isComplete = false;
		this.state = state;
	}
	
	public DrivesShiftMaxSpeed() {
		isComplete = false;
		this.state = (Robot.drives.getSpeed() == Constants.DRIVES_LOW_SPEED) ? SpeedMaxStates.FULL : SpeedMaxStates.HALF;
	}
	
	@Override
	protected void execute() {
		if (state == SpeedMaxStates.HALF) {
			Robot.drives.setLowSpeed();
		} else if (state == SpeedMaxStates.FULL) {
			Robot.drives.setHighSpeed();
		}
		isComplete = true;
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return isComplete;
	}
}
