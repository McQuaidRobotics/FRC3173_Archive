package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class PushHatchAcquire extends Command {
    @Override
    protected void initialize() {
        // toggles the hatch state
        boolean state = Robot.hatch.getImpactState();
        if(state) {  // if release is out
            Robot.hatch.impactRetract();
        } else {  // if release not out
            Robot.hatch.impactExtend();
        }
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
