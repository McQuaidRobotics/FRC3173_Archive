package org.usfirst.frc3173.IgKnighters2016.commands;

import org.usfirst.frc3173.IgKnighters2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AutoShootSpinUp extends Command {
boolean above=false;
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		//Robot.shooter.spinVoltage(.75);
		Robot.shooter.spinSpeed(5000);
		System.out.println("Auto shoot spin up");
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		//if(Robot.shooter.getCurrent()>=10) above=true;
		//if(above && Robot.shooter.getCurrent()<=10) return true;
		//return false;
		return Robot.shooter.getSpinnerRPM()>=4800;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		Robot.shooter.spinVoltage(-1);
		Robot.shooter.spinSpeed(0);
	}

}
