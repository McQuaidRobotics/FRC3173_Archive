package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.Solenoid;

// Hatch
public class Cargo extends Subsystem {
  private Solenoid cargoLauncher = RobotMap.cargoLauncher;
  //private static Solenoid cargoLauncherStatic = RobotMap.cargoLauncher;

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
  }

  public void toggleCargoLauncher() {
    cargoLauncher.set(!cargoLauncher.get());
  }
}
