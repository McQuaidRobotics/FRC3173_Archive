/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

//import com.ctre.phoenix.motorcontrol.can.TalonSRXPIDSetConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
//import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.commands.drives.ManualDrive;

/*
 * @Jonathan drives subsystem for 2019. nuff' said!
 */
public class Drives extends Subsystem {

  private final DifferentialDrive drivesController = RobotMap.drivesController;
  private final WPI_TalonSRX drivesLeftCenter = RobotMap.drivesLeftMaster;
  private final WPI_TalonSRX drivesRightCenter = RobotMap.drivesRightMaster;

  private double inverser = 1.0;
  private double speedMultiplier = 0.85;
  private double speedMultiplierAdd = 0.2;  // 20% increase/decrease
  //private double speedMultiplierMaxSpeed = 2.0;
  //private double speedMultiplierMinSpeed = speedMultiplierAdd;

  private double lastPOV = 0;  // what the last POV was because we can't tell what the last one is in wpilib

  public double kP, kI, kD, kFF;

  public Drives() {
    kP = 0.1;
    kI = 1e-6;
    kD = 0; 
    kFF = 0; 

    setupPid();
  }
  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ManualDrive());
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("currentAngle", RobotMap.imu.getYaw());
    //System.out.println(RobotMap.imu.getYaw());
    if(Constants.SMARTDASHBOARD_ENABLE) SmartDashboard.putNumber("[Drives] leftMotorEncoder", drivesLeftCenter.getSelectedSensorPosition(0));
    if(Constants.SMARTDASHBOARD_ENABLE) SmartDashboard.putNumber("[Drives] rightMotorEncoder", drivesRightCenter.getSelectedSensorPosition(0));
  }

  public int getLeftDriveEncoderValue() { return drivesLeftCenter.getSelectedSensorPosition(0); }
  public int getRightDriveEncoderValue() { return drivesRightCenter.getSelectedSensorPosition(0); }

  public void zeroEncoders() {
    drivesLeftCenter.setSelectedSensorPosition(0, 0, 0);
    drivesRightCenter.setSelectedSensorPosition(0, 0, 0);
  }

  private void setupPid() {
    // set PID coefficients
    drivesLeftCenter.config_kP(0, kP);
    drivesLeftCenter.config_kI(0, kI);
    drivesLeftCenter.config_kD(0, kP);
    drivesLeftCenter.config_kF(0, kFF);

    drivesRightCenter.config_kP(0, kP);
    drivesRightCenter.config_kI(0, kI);
    drivesRightCenter.config_kD(0, kD);
    drivesRightCenter.config_kF(0, kFF);

    // display PID coefficients on SmartDashboard
    if(Constants.SMARTDASHBOARD_ENABLE_DEBUGINFO) { 
      SmartDashboard.putNumber("P Gain", kP);
      SmartDashboard.putNumber("I Gain", kI);
      SmartDashboard.putNumber("D Gain", kD);
      SmartDashboard.putNumber("Feed Forward", kFF);
    }
  }

  //simply put vbus to both master drives
  public void goBoth(double left, double right) {
    drivesLeftCenter.set(left);
    drivesRightCenter.set(right);
  }

  public void invertControls() {
    inverser = -inverser;  // quick invert
  }

  //one iteration, check joysticks, manual drive
  public void manualDrive() {
      /*
      double leftZ = OI.leftJoystick.getZ();
      double rightZ = OI.rightJoystick.getZ();
      double zaxis = rightZ * 0.9 + 0.1;
      */
      double zaxis = 1.0;
      //double leftSpeed = (Math.round(-OI.leftJoystick.getRawAxis(1) * 10)/10.0);
      double leftSpeed = -OI.mainGamepad.getY(Hand.kLeft);  // get left joystick y
      leftSpeed *= zaxis;
      leftSpeed *= inverser;
      //double rightSpeed = (Math.round(OI.rightJoystick.getRawAxis(1) * 10)/10.0);
      double rightSpeed = OI.mainGamepad.getX(Hand.kRight); // get right joystick x
      rightSpeed *= zaxis;
      rightSpeed *= inverser;
      //drivesController.arcadeDrive(leftSpeed, rightSpeed);
      double leftTrigger = OI.mainGamepad.getTriggerAxis(Hand.kLeft);
      double rightTrigger = OI.mainGamepad.getTriggerAxis(Hand.kRight);
      //boolean leftBumperPressed = false;
      boolean leftBumperPressed = OI.mainGamepad.getBumper(Hand.kLeft);
      //boolean leftBumperJustPressed = OI.mainGamepad.getBumperPressed(Hand.kLeft);
      boolean leftBumperJustPressed = false;
      boolean rightBumperPressed = OI.mainGamepad.getBumper(Hand.kRight);
      boolean rightBumperJustPressed = OI.mainGamepad.getBumperPressed(Hand.kRight);
      double triggerDeadzone = 0.2;  // 20% held down
      int pov = OI.mainGamepad.getPOV();
      //OI.mainGamepad.setRumble(RumbleType.kRightRumble, 1.0);
      if(pov != lastPOV) {
        lastPOV = pov;
        switch(pov) {
          case 0:  // dp up
            speedMultiplier += speedMultiplierAdd;
            break;
          case 180:  // dp down
            speedMultiplier -= speedMultiplierAdd;
            break;
          case 90:
            speedMultiplier = 0.2;
            break;
          case 270:
            speedMultiplier = 0.8;
            break;

        }
      }

      if (speedMultiplier < speedMultiplierAdd) {
        speedMultiplier = speedMultiplierAdd;
      }

      if (speedMultiplier > 2.0) {
        speedMultiplier = 2.0;
      }


      if(Math.abs(leftTrigger) < triggerDeadzone || Math.abs(rightTrigger) < triggerDeadzone) {
        // if we're trying to use the triggers, work in tank drive
        if(rightBumperPressed) {
          rightSpeed = -rightSpeed;
        }
        rightSpeed *= Constants.DRIVES_ARCADE_SPIN_MULTIPLIER;
        leftSpeed *= speedMultiplier;
        rightSpeed *= speedMultiplier;
        if(leftBumperPressed) {
          drivesController.curvatureDrive(rightSpeed, leftSpeed, false);
        } else {
          drivesController.arcadeDrive(rightSpeed, leftSpeed);
        }
      } else {
        //drivesController.tankDrive(leftSpeed, rightSpeed);
        if((leftBumperPressed && rightBumperJustPressed) || (leftBumperJustPressed && rightBumperPressed) || (leftBumperJustPressed && rightBumperJustPressed)) {
          // if both bumpers are pressed, invert the arcade drive
          Robot.drives.invertControls();
        }
      }
      //drivesController.arcadeDrive(rightSpeed, leftSpeed);
      //drivesController.tankDrive(leftSpeed, rightSpeed);
  }
}
