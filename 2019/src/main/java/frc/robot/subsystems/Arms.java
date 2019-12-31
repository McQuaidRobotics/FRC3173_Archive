/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.*;
import frc.robot.commands.climber.ArmsManualDrive;
import frc.robot.util.AppendageSubsystem;

public class Arms extends AppendageSubsystem {
  public Arms() {
    // initialize motor
    super(RobotMap.climbArms, Constants.CLIMB_ARMS_STALL_CURRENT, 5700, 5700);

    // PID coefficients
    kP = 5e-5; 
    kI = 1e-6;
    kD = 0; 
    kIz = 0; 
    kFF = 0; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    maxRPM = 1500;

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

    if(Constants.SMARTDASHBOARD_ENABLE_DEBUGINFO) {
      SmartDashboard.putNumber("Arms forward limit", hittingTopSwitch()?1:0);
      SmartDashboard.putNumber("Arms reverse limit", hittingBottomSwitch()?1:0);
      SmartDashboard.putNumber("Arms current", m_motor.getOutputCurrent());
    }
  }

  @Override
  public void periodic() {
    updateSmartStuff();

    double pos = m_encoder.getPosition();
    
    if (Constants.SMARTDASHBOARD_ENABLE_DEBUGINFO) {
      SmartDashboard.putNumber("Arms Encoder count", pos);
    }
  }
  
  public boolean onSurface() {
    return m_motor.getOutputCurrent() >= Constants.ARMS_SURFACE_OUTPUT;
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new ArmsManualDrive());
  }
}
