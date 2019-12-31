package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class PushHatchRelease extends Command {
    @Override
    protected void initialize() {
        // toggles the hatch state
        boolean state = Robot.hatch.getReleaseState();
        if(state) {  // if release is out
            Robot.hatch.releaseRetract();
        } else {  // if release not out
            Robot.hatch.releaseExtend();
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
