
package frc.robot.commands.auto.climb;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.climber.*;

public class Climb_30_Rise extends CommandGroup {
    public Climb_30_Rise() {
        addParallel(new AppendageRise(Robot.pants, Constants.PANTS_RPMS, "pants"));  // last variable is telling command what the appendage is
        addParallel(new AppendageRise(Robot.arms, Constants.ARMS_RPMS, "arms"));
    }
}
