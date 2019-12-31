/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.*;
import frc.robot.commands.hatch.ElevatorManualDrive;

import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;

public class Elevator extends Subsystem {
  protected CANSparkMax e_motor;
  protected CANPIDController e_pidController;
  protected CANEncoder e_encoder;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
  public double maxSpeed = 1.0;

  public boolean inPositionMode = true;

  public Elevator() {
    // create motor
    e_motor = RobotMap.elevator;
    e_motor.setIdleMode(IdleMode.kBrake);
    e_motor.setSmartCurrentLimit(Constants.ELEVATOR_SMARTCURRENT_LIMIT);
    e_motor.setClosedLoopRampRate(1.0);
    e_pidController = e_motor.getPIDController();
    e_encoder = e_motor.getEncoder();
    // PID coefficients
    // THESE PID VALUES ARE FOR POSITION MODE. PLEASE, FOR THE LOVE OF GOD,
    // DON'T TRY USING THESE VALUES ON VELOCITY MODE. THE SPARK DOESNT LIKE THAT.
    //kP = 0.1;
    // kP = 0.1;
    //kI = 0;
    //kD = 0; 
    kP = Constants.ELEVATOR_POSITION_KP;
    kI = Constants.ELEVATOR_POSITION_KI;
    kD = Constants.ELEVATOR_POSITION_KD;
    kIz = 0; 
    kFF = 0; 
    kMaxOutput = maxSpeed;
    kMinOutput = -maxSpeed;
    maxRPM = 1000;
    setupPid();
  }

  private void setupPid() {
    // set PID coefficients
    e_pidController.setP(kP);
    e_pidController.setI(kI);
    e_pidController.setD(kD);
    e_pidController.setIZone(kIz);
    e_pidController.setFF(kFF);
    e_pidController.setOutputRange(kMinOutput, kMaxOutput);

    // display PID coefficients on SmartDashboard
    if(Constants.SMARTDASHBOARD_ENABLE_DEBUGINFO) { 
      SmartDashboard.putNumber("Elevator P Gain", kP);
      SmartDashboard.putNumber("Elevator I Gain", kI);
      SmartDashboard.putNumber("Elevator D Gain", kD);
      SmartDashboard.putNumber("Elevator I Zone", kIz);
      SmartDashboard.putNumber("Elevator Feed Forward", kFF);
      SmartDashboard.putNumber("Elevator Max Output", kMaxOutput);
      SmartDashboard.putNumber("Elevator Min Output", kMinOutput);
    }
  }

  private void updateSmartStuff() {
    
    //dt += System.currentTimeMillis() / 1000L;
    // read PID coefficients from SmartDashboard
    double p = SmartDashboard.getNumber("Elevator P Gain", 0);
    double i = SmartDashboard.getNumber("Elevator I Gain", 0);
    double d = SmartDashboard.getNumber("Elevator D Gain", 0);
    double iz = SmartDashboard.getNumber("Elevator I Zone", 0);
    double ff = SmartDashboard.getNumber("Elevator Feed Forward", 0);
    double max = SmartDashboard.getNumber("Elevator Max Output", 0);
    double min = SmartDashboard.getNumber("Elevator Min Output", 0);
    // if PID coefficients on SmartDashboard have changed, write new values to controller
    if((p != kP)) { e_pidController.setP(p); kP = p; }
    if((i != kI)) { e_pidController.setI(i); kI = i; }
    if((d != kD)) { e_pidController.setD(d); kD = d; }
    if((iz != kIz)) { e_pidController.setIZone(iz); kIz = iz; }
    if((ff != kFF)) { e_pidController.setFF(ff); kFF = ff; }
    if((max != kMaxOutput) || (min != kMinOutput)) { 
      e_pidController.setOutputRange(min, max); 
      kMinOutput = min; kMaxOutput = max; 
    }
    

    //SmartDashboard.putNumber("Time", dt);
    if(Constants.SMARTDASHBOARD_ENABLE_DEBUGINFO) { 
      SmartDashboard.putNumber("Elevator current", e_motor.getOutputCurrent());
    }

    //SmartDashboard.putNumber("ProcessVariable", e_encoder.getVelocity());
  }

  @Override
  public void periodic() {
    updateSmartStuff();

    double pos = e_encoder.getPosition();
    if(Constants.SMARTDASHBOARD_ENABLE_DEBUGINFO) {
      SmartDashboard.putNumber("Elevator Encoder count", pos);
      SmartDashboard.putNumber("Elevator current", e_motor.getOutputCurrent());
    }
  }

  public double getPosition() {
    return e_encoder.getPosition();
  }

  public void goTo(double encSetNewPosition) {
    initializePositionMode();
    if (stalled()) {
      stopMoving();
    } else {
      e_pidController.setReference(encSetNewPosition, ControlType.kPosition);
    }
  }

  public void goToInches(double inches) {
    initializePositionMode();
    if (stalled()) {
      stopMoving();
    } else {
      // convert inches to revolution
      //double revolutions = inches * Constants.ELEVATOR_MOTOR_REV_TO_INCHES;
      double revolutions = (1 / Constants.ELEVATOR_WINCH_CIRCUMFERENCE) * inches * Constants.ELEVATOR_MOTOR_ROTATIONS_TO_WINCH_REV;  // i don't understand this but whatever
      //revolutions *= 0.67;
      if(Constants.ELEVATOR_DEBUG_PRINTS) {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nTHE REVOLUTIONS WE'RE SUPPOSED TO BE GOING FOR " + inches + " INCHES\nIS " + revolutions + " REVOLUTIONS");
      }
      e_pidController.setReference(revolutions, ControlType.kPosition);
    }
  }

  public void forwardMoveUp(double rpm) {
    moveAt(+rpm); 
  }

  public void reverseMoveDown(double rpm) {
    moveAt(-rpm);
  }

  // TODO: find out _when_ these modes are being called in relation
  // to them being reflected off of Shuffleboard. if these are being called
  // _during_ periodic, then this is a race condition and we probably
  // should just be using signals instead.
  public void initializeVelocityMode() {
    if(inPositionMode) {
      if(Constants.ELEVATOR_DEBUG_PRINTS) {
        System.out.println("INITIALIZING VELOCITY MODE ON THE ELEVATOR");
      }
      kP = Constants.ELEVATOR_VELOCITY_KP;
      kI = Constants.ELEVATOR_VELOCITY_KI;
      kD = Constants.ELEVATOR_VELOCITY_KD;
    }
  }

  public void initializePositionMode() {
    if(!inPositionMode) {
      if(Constants.ELEVATOR_DEBUG_PRINTS) {
        System.out.println("INITIALIZING POSITION MODE ON THE ELEVATOR");
      }
      kP = Constants.ELEVATOR_POSITION_KP;
      kI = Constants.ELEVATOR_POSITION_KI;
      kD = Constants.ELEVATOR_POSITION_KD;
    }
  }

  public void moveAt(double rpm) {
    initializeVelocityMode();
    if(rpm == 0) {
      stopMoving();
      return;
    }
    e_pidController.setReference(rpm, ControlType.kVelocity);
  }
  
  public void stopMoving() {
    initializeVelocityMode();
    e_pidController.setReference(0, ControlType.kVoltage);
  }
  
  public boolean stalled() {
    return e_motor.getOutputCurrent() >= Constants.ELEVATOR_STALL;
    //return false;
  }

  public void zeroEncoder() {
    e_encoder.setPosition(0);
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new ElevatorManualDrive());
  }
}
