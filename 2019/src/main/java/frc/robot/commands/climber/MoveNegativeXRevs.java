package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.Constants;

public class MoveNegativeXRevs extends InstantCommand {
    @Override
    protected void initialize() {
        Robot.pants.moveBy(-(Constants.AUTOCLIMB_HOIST_REVS_TO_SET + 0.25));
    }
}