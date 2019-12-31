package frc.robot.commands.auto.climb;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Constants;

public class ToggleAutoClimbActive extends InstantCommand {
    @Override
    protected void initialize() { 
        Constants.AUTOCLIMB_IS_ACTIVE = !Constants.AUTOCLIMB_IS_ACTIVE;  // flip the boolean
    }
}