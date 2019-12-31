
package frc.robot.commands.auto.climb;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Constants;
import frc.robot.commands.climber.*;

public class Climb_40_UrgeOnPlatform extends CommandGroup {
    public Climb_40_UrgeOnPlatform() {
        addSequential(new TurnUrgersByRevs(
            Constants.URGE_RPMS,  // Speed to turn
            Constants.URGE_REVOLUTIONS * (Constants.NEO_TICKS_PER_REVOLUTION * Constants.URGERS_GEAR_REDUCTION) // Distance to turn
        ));
    }
}
