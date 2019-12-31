
package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

public class StopArms extends InstantCommand {
  public StopArms() {
    requires(Robot.arms);
  }
  @Override
  protected void initialize() { 
    Robot.arms.stopMoving();
  }
}