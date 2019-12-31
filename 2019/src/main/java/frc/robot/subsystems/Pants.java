/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.*;
import frc.robot.commands.climber.PantsManualDrive;
import frc.robot.util.AppendageSubsystem;

public class Pants extends AppendageSubsystem {
  public Pants() {
    // initialize motor
    super(RobotMap.climbPants, Constants.CLIMB_PANTS_STALL_CURRENT, 5700, 5700);

    // PID coefficients
    kP = 5e-5;
    kI = 1e-6;
    kD = 0;
    kIz = 0;
    kFF = 0;
    kMaxOutput = 1;
    kMinOutput = -1;
    maxRPM = 1515;

    setupPid();
  }

  private void updateSmartStuff() {
    dt += System.currentTimeMillis() / 1000L;
    // read PID coefficients from SmartDashboard
    if(Constants.SMARTDASHBOARD_ENABLE) {
      double p = SmartDashboard.getNumber("P Gain", 0);
      double i = SmartDashboard.getNumber("I Gain", 0);
      double d = SmartDashboard.getNumber("D Gain", 0);
      double iz = SmartDashboard.getNumber("I Zone", 0);
      double ff = SmartDashboard.getNumber("Feed Forward", 0);
      double max = SmartDashboard.getNumber("Max Output", 0);
      double min = SmartDashboard.getNumber("Min Output", 0);
      // if PID coefficients on SmartDashboard have changed, write new values to controller
      if((p != kP)) { m_pidController.setP(p); kP = p; }
      if((i != kI)) { m_pidController.setI(i); kI = i; }
      if((d != kD)) { m_pidController.setD(d); kD = d; }
      if((iz != kIz)) { m_pidController.setIZone(iz); kIz = iz; }
      if((ff != kFF)) { m_pidController.setFF(ff); kFF = ff; }
      if((max != kMaxOutput) || (min != kMinOutput)) { 
        m_pidController.setOutputRange(min, max); 
        kMinOutput = min; kMaxOutput = max; 
      }
    }

    if (Constants.SMARTDASHBOARD_ENABLE_DEBUGINFO) {
      SmartDashboard.putNumber("Pants forward limit", hittingTopSwitch() ? 1 : 0);
      SmartDashboard.putNumber("Pants reverse limit", hittingBottomSwitch() ? 1 : 0);
      SmartDashboard.putNumber("Pants current", m_motor.getOutputCurrent());
    }
  }

  @Override
  public void periodic() {
    updateSmartStuff();

    double pos = m_encoder.getPosition();

    if (Constants.SMARTDASHBOARD_ENABLE_DEBUGINFO) {
      SmartDashboard.putNumber("Pants Encoder count", pos);
    }
  }

  public boolean onSurface() {
    return m_motor.getOutputCurrent() >= Constants.PANTS_SURFACE_OUTPUT;
  }

  public void goTo(double position) {
    m_pidController.setReference(position, ControlType.kPosition);
  }

  public void moveBy(double amount) {
    double currentPos = m_encoder.getPosition();
    double delta = currentPos + amount;  // position to move to
    goTo(delta);
  }

  public void unwindPants() {
    // Used to deploy hoist rigging
    // TODO (Chuck):  Need to deterine which way to unwind limit
    /* steps:
    1) Disable limit switch
    2) Turn (x) revs
    3) Re-Neable limit switch
    */
  }

  private void toggleHoistLimit() {
    toggleForwardLimit();  // TODO: it might not be the forward limit switch
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new PantsManualDrive());
  }
}
