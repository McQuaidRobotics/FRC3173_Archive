package org.usfirst.frc3173.IgKnighters2016.commands;

import org.usfirst.frc3173.IgKnighters2016.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class AutoShootBegin extends Command {
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		RobotMap.visionLED.set(true);
		System.out.println("Begin auto shoot");
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}
