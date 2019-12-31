package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

public class ReleaseExtend extends InstantCommand {
    @Override
    protected void initialize() { 
        Robot.hatch.releaseExtend();
    }
}