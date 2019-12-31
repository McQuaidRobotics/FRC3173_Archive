package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class StopAll extends CommandGroup {
    public StopAll() {
        addParallel(new StopPants());
        addParallel(new StopUrge());
        addSequential(new StopArms());
    }

    @Override
    public void initialize() {
        super.initialize();
    }
}