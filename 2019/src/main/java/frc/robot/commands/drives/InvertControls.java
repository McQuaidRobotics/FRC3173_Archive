package frc.robot.commands.drives;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class InvertControls extends Command {
    @Override
    protected void initialize() {
        Robot.drives.invertControls();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
