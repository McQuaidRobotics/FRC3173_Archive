

package frc.robot.commands.auto.climb;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.climber.*;

public class Climb_50_LowerToPlatform extends CommandGroup {
    public Climb_50_LowerToPlatform() {
        addParallel(new AppendageToTop(Robot.pants, Constants.REV_NEO_MAX_RPMS));
        addSequential(new AppendageToTop(Robot.arms, Constants.REV_NEO_MAX_RPMS));
    }
}
