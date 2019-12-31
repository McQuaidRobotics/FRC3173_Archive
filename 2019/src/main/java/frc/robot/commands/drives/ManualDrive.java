/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drives;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * An example command.  You can replace me with your own command.
 */
public class ManualDrive extends Command {
  public ManualDrive() { requires(Robot.drives); }

  @Override
  protected void initialize() { }

  @Override
  protected void execute() { Robot.drives.manualDrive(); }

  @Override
  protected boolean isFinished() { return false; }

  @Override
  protected void interrupted() { end(); }
}
