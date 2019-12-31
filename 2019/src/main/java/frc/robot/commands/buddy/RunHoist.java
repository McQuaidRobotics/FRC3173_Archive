package frc.robot.commands.buddy;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.RobotMap;
import frc.robot.commands.auto.climb.ToggleAutoClimbActive;
import frc.robot.commands.climber.*;

public class RunHoist extends CommandGroup {
    public RunHoist() {
        addSequential(new TogglePantsLimit("forward"));  // turn off limit
        addSequential(new MoveXRevs());  // move it past the limit
        addSequential(new MoveNegativeXRevs());  // move it back now that we have slack
        addSequential(new TogglePantsLimit("forward"));  // turn on limit
        // STARING POINT IS PIN STRAIGHT UP
        // DELTA(NUMBER 1 AND NUMBER 2) ARE WHAT WE NEED TO MOVE WHEN THE PIN
        // IS STRAIGHT UP
        // 19.57 IS THE FIRST NUMBER (ENCODER TICKS FOR PANTS)
        // 59.40 IS THE NEXT NUMBER
        // DELTA IS 40.33
        // 189.90 IS WHEN THE CHAIN STARTS SLIPPING
        // 311.49 IS WHEN WE ARE ABLE TO CLIMB

        // ZERO POINT IS AT 10.38
        // 308.713 IS NEXT ENCODER COUNT
        // 
        // 
        // 
    }
}