/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.Solenoid;

// Hatch
public class Hatch extends Subsystem {
  private final Solenoid hatchImpact = RobotMap.hatchImpact;
  private final Solenoid hatchRelease = RobotMap.hatchRelease;

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
  }

  private static final boolean IMPACT_DIRECTION = true;
  public void impactExtend() { hatchImpact.set(IMPACT_DIRECTION); }
  public void impactRetract() { hatchImpact.set(!IMPACT_DIRECTION); }

  private static final boolean RELEASE_DIRECTION = true;
  public void releaseExtend() { hatchRelease.set(RELEASE_DIRECTION); }
  public void releaseRetract() { hatchRelease.set(!RELEASE_DIRECTION); }
  public boolean getReleaseState() { return hatchRelease.get(); }
  public boolean getImpactState() { return hatchImpact.get();}

  public void invertRelease() { hatchRelease.set(!hatchRelease.get()); }
  public void invertImpact() { hatchImpact.set(!hatchImpact.get()); }
}
