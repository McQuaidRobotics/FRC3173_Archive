package org.usfirst.frc3173.IgKnighters2018.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.RobotMap;

/**
 * @Bowen
 */
public class AcquisitionIntake extends Command {

	private boolean shouldTerminate;
	
    public AcquisitionIntake() {
    	shouldTerminate = false;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	System.out.println("[COMMAND] AcquisitionIntake!");
    	Robot.acquisition.intake();
		shouldTerminate = true;

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return shouldTerminate;
    }
}
