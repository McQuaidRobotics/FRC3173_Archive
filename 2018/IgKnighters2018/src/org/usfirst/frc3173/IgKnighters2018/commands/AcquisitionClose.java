package org.usfirst.frc3173.IgKnighters2018.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc3173.IgKnighters2018.Robot;

/**
 * @Bowen
 */
public class AcquisitionClose extends Command {

	private boolean shouldTerminate;
	
    public AcquisitionClose() {
    	shouldTerminate = false;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	System.out.println("[COMMAND] AcquisitionClose!");
    	Robot.acquisition.closeAcq();
		shouldTerminate = true;

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return shouldTerminate;
    }
}
