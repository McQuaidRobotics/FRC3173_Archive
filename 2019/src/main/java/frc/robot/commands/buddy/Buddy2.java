package frc.robot.commands.buddy;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.RobotMap;

public class Buddy2 extends InstantCommand {
    @Override
    protected void initialize() { 
        RobotMap.buddy2.set(!RobotMap.buddy2.get());
    }
}