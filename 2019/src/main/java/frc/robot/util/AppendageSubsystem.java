/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import frc.robot.Constants;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class AppendageSubsystem extends Subsystem {
  protected CANSparkMax m_motor;
  protected CANPIDController m_pidController;
  protected CANEncoder m_encoder;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
  protected CANDigitalInput forwardLimit;
  protected CANDigitalInput reverseLimit;

  protected long dt = 0;

  protected AppendageSubsystem(CANSparkMax motor, int stallLimit, int freeLimit, int limitRPM) {
    m_motor = motor;
    m_motor.restoreFactoryDefaults();
    m_motor.setIdleMode(CANSparkMax.IdleMode.kBrake);  //Put motors into Brake Mode (default is coast)
    m_motor.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
    m_motor.setClosedLoopRampRate(60);
    forwardLimit = m_motor.getForwardLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
    reverseLimit = m_motor.getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);    
    forwardLimit.enableLimitSwitch(true);
    reverseLimit.enableLimitSwitch(true);

    m_pidController = m_motor.getPIDController();

    // Encoder object created to display position values
    m_encoder = m_motor.getEncoder();
    m_encoder.setPosition(0);
  }

  public CANDigitalInput getForwardLimit() {
    return forwardLimit;
  }

  public CANDigitalInput getReverseLimit() {
    return reverseLimit;
  }

  public void toggleForwardLimit() {
    forwardLimit.enableLimitSwitch(!forwardLimit.isLimitSwitchEnabled());
  }

  public void toggleReverseLimit() {
    reverseLimit.enableLimitSwitch(!reverseLimit.isLimitSwitchEnabled());
  }

  public boolean isForwardLimitEnabled() {
    return forwardLimit.isLimitSwitchEnabled();
  }

  public boolean isReverseLimitEnabled() {
    return reverseLimit.isLimitSwitchEnabled();
  }

  protected void setupPid() {
    // set PID coefficients
    m_pidController.setP(kP);
    m_pidController.setI(kI);
    m_pidController.setD(kD);
    m_pidController.setIZone(kIz);
    m_pidController.setFF(kFF);
    m_pidController.setOutputRange(kMinOutput, kMaxOutput);

    // display PID coefficients on SmartDashboard
    if(Constants.SMARTDASHBOARD_ENABLE_DEBUGINFO) { 
      SmartDashboard.putNumber("P Gain", kP);
      SmartDashboard.putNumber("I Gain", kI);
      SmartDashboard.putNumber("D Gain", kD);
      SmartDashboard.putNumber("I Zone", kIz);
      SmartDashboard.putNumber("Feed Forward", kFF);
      SmartDashboard.putNumber("Max Output", kMaxOutput);
      SmartDashboard.putNumber("Min Output", kMinOutput);
    }
  }

  //// LIMIT SWITCHES ////
  public boolean hittingTopSwitch() {
    return forwardLimit.get();
  }
  public boolean hittingBottomSwitch() {
    return reverseLimit.get();
  }

  //// MOVEMENT ////
  public void moveAt(double rpm) {
    if (rpm == 0) {
      stopMoving();
      return;
    }

    m_pidController.setReference(rpm, ControlType.kVelocity);
  }
  public void moveUp(double rpm) {
    moveAt(+rpm);
  }
  public void moveDown(double rpm) {
    moveAt(-rpm);
  }
  public void stopMoving() {
    //moveAt(0);  // don't move at all
    m_pidController.setReference(0, ControlType.kVoltage);
  }

  //// ENCODERS ////
  public double getPosition() {  // get position of the encoder
    return m_encoder.getPosition();
  }
  public void zeroEncoder() {
    m_encoder.setPosition(0);
  }

  public abstract boolean onSurface();
}
