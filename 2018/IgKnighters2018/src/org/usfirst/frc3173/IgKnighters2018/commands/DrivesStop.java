package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DrivesStop extends Command {
	private boolean shouldTerminate;
	
    public DrivesStop() {
    	shouldTerminate = false;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	System.out.println("[COMMAND] DrivesStop!");
    	Robot.drives.stop();
		shouldTerminate = true;

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return shouldTerminate;
    }
}
