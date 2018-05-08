package org.usfirst.frc3173.IgKnighters2016.commands;

import org.usfirst.frc3173.IgKnighters2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AutoShootBelt extends Command {
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		Robot.acquisition.moveAcquisitionBelt(Robot.acquisition.BELT_SPEED);
		System.out.println("Auto shoot belt");
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		//if(System.currentTimeMillis()-time>1000)return true;
		//return false;
		return (!Robot.acquisition.isBallAtSwitch());
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		Robot.acquisition.moveAcquisitionBelt(0);
		Robot.shooter.spinVoltage(-1);
	}

}
