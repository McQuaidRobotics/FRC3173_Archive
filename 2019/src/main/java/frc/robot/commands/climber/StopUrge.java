package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

public class StopUrge extends InstantCommand {
  public StopUrge() {
    requires(Robot.urgers);
  }
  @Override
  protected void initialize() { 
    //Robot.urgers.suspendUrge();
    Robot.urgers.go(0);
  }
}
