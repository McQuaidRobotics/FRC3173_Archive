package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AutonomousStall extends Command {
	
	private boolean isComplete;
	
	public AutonomousStall() {
		requires(Robot.drives);
		isComplete = false;
	}
	
	@Override
	public void initialize() {
		System.out.println("[STALL] Stalling...");
		isComplete = false;
	}
	
	@Override
	protected void execute() {
		Robot.drives.stop();
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return isComplete;
	}

	@Override
    protected void end() {
		System.out.println("[STALL] Done!");
    	Robot.drives.stop();
    }

    @Override
    protected void interrupted() {
    	isComplete = true;
    	end();
    }
}
