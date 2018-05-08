package org.usfirst.frc3173.IgKnighters2018.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc3173.IgKnighters2018.Robot;

/**
 * @Bowen
 */
public class AcquisitionReverse extends Command {

	private boolean shouldTerminate;
    public AcquisitionReverse() {
        requires(Robot.acquisition);
        shouldTerminate = false;
    }
    
    @Override
    protected void execute() {
    	System.out.println("[COMMAND] AcquisitionReverse!");
		Robot.acquisition.reverse();
		shouldTerminate = true;

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return shouldTerminate;
    }
}
