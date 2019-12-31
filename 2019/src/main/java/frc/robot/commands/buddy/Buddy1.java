package frc.robot.commands.buddy;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.RobotMap;

public class Buddy1 extends InstantCommand {
    @Override
    protected void initialize() { 
        RobotMap.buddy1.set(true);
    }
}