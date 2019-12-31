
package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

public class StopPants extends InstantCommand {
  public StopPants() {
    requires(Robot.pants);
  }
  @Override
  protected void initialize() { 
    Robot.pants.stopMoving();
  }
}