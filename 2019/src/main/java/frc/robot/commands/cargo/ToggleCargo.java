package frc.robot.commands.cargo;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;

public class ToggleCargo extends Command {
    @Override
    protected void initialize() {
        //requires(Robot.cargo);
        // check to make sure that we're not in autoclimb, since i don't quite know how
        // the scheduler is going to like handling this command during autoclimb
        if(!Constants.AUTOCLIMB_IS_ACTIVE) {
            Robot.cargo.toggleCargoLauncher();
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
