/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.*;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.climber.UrgersManualDrive;

public class Urgers extends Subsystem {
  private CANSparkMax climbUrgers = RobotMap.climbUrgers;
  private CANEncoder urgerEncoder = climbUrgers.getEncoder();
  private CANPIDController urgerPID;

  private static Solenoid armsReleaseStatic = RobotMap.armsRelease;
  private Solenoid armsRelease = RobotMap.armsRelease;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

  public Urgers() {
    climbUrgers.restoreFactoryDefaults();
    climbUrgers.setIdleMode(CANSparkMax.IdleMode.kBrake);  //Put motors into Brake Mode (default is coast)
    climbUrgers.setClosedLoopRampRate(60);  // should ramp over 60 seconds, but doesn't seem to, at least in arms
    urgerPID = climbUrgers.getPIDController();
    urgerEncoder = climbUrgers.getEncoder();
    zeroEncoder();

    // PID coefficients
    kP = 5e-5; 
    kI = 1e-6;
    kD = 0; 
    kIz = 0; 
    kFF = 0; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    maxRPM = 3840;
    // set PID coefficients
    urgerPID.setP(kP);
    urgerPID.setI(kI);
    urgerPID.setD(kD);
    urgerPID.setIZone(kIz);
    urgerPID.setFF(kFF);
    urgerPID.setOutputRange(kMinOutput, kMaxOutput);

    urgerEncoder.setPositionConversionFactor(42);
  }

  public CANSparkMax getUrger() {
    return climbUrgers;
  }

  public void zeroEncoder() {
    urgerEncoder.setPosition(0);
  }

  public void go(double velocity) {
    if(velocity == 0) {
      stop();
      return;
    }
    urgerPID.setReference(velocity, ControlType.kVelocity);
  }

  public static void toggleArmsExtendStatic() {
    armsReleaseStatic.set(!armsReleaseStatic.get());  // just invert the value of the armsRelease
  }

  public void toggleArmsExtend() {
    armsRelease.set(!armsRelease.get());
  }

  public void runForward() {
    //go(maxRPM);
    go(-maxRPM);
  }

  public void runBackward() {
    //go(-maxRPM);
    go(maxRPM);
  }

  public double getTicks() {
    return urgerEncoder.getPosition();
  }

  public void stopMoving() {  // consistency
    stop();
  }

  public void stop() {
    //go(0);
    urgerPID.setReference(0, ControlType.kVoltage);
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new UrgersManualDrive());
  }
}
