package frc.robot.commands.auto.climb;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.climber.*;

// Driver has robot manually positioned properly (flush up against platform), arms are extended and verified
public class Climb_20_InitClimb extends CommandGroup {
    public Climb_20_InitClimb() {
        addParallel(new AppendageToFloor(Robot.pants, Constants.REV_NEO_MAX_RPMS));
        addParallel(new AppendageToFloor(Robot.arms, Constants.REV_NEO_MAX_RPMS));
    }
}
