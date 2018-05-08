// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3173.IgKnighters2016.subsystems;

import org.usfirst.frc3173.IgKnighters2016.Robot;
import org.usfirst.frc3173.IgKnighters2016.RobotMap;
import org.usfirst.frc3173.IgKnighters2016.commands.*;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class Shooter extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final CANTalon spinnerMotor = RobotMap.shooterSpinnerMotor;
    private boolean isSpinning=false;
    private final double MIN_VOLTAGE=5;
    private final double MAX_VOLTAGE=11;
    public final double MAX_SPEED=6600;
    public final double MIN_SPEED=0;
    
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS


    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    public Shooter(){
    	//System.out.println("Setup");
    	SmartDashboard.putNumber("ShooterP",100);
    	SmartDashboard.putNumber("ShooterI",.1);
    	SmartDashboard.putNumber("ShooterD",.1);
		//spinnerMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
		spinnerMotor.setFeedbackDevice(CANTalon.FeedbackDevice.EncRising);
	    spinnerMotor.configEncoderCodesPerRev(1);
	    spinnerMotor.setP(750);
	    spinnerMotor.setI(.15);
	    spinnerMotor.setD(0);
    }
    
    public void spinPercentVBus (double speed) {
    	if(spinnerMotor.getControlMode()!=CANTalon.TalonControlMode.PercentVbus){
    		spinnerMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    	}
    	if(speed==0){
    		Robot.shooter.isSpinning=false;
    	}
    	else{
    		Robot.shooter.isSpinning=true;
    	}
    	//spinnerMotor.setPID(SmartDashboard.getNumber("ShooterP",750), SmartDashboard.getNumber("ShooterI",.15), SmartDashboard.getNumber("ShooterD",.1));
    	
    	spinnerMotor.set(speed);
    	
    }
    
    public void spinVoltage(double proportion){
    	if(spinnerMotor.getControlMode()!=CANTalon.TalonControlMode.Voltage){
    		spinnerMotor.changeControlMode(CANTalon.TalonControlMode.Voltage);
    	}
    	if(proportion==-1){
    		Robot.shooter.isSpinning=false;
    		spinnerMotor.set(0);
    	}else{
    		Robot.shooter.isSpinning=true;
    		spinnerMotor.set((MAX_VOLTAGE-MIN_VOLTAGE)*proportion+MIN_VOLTAGE);
    	}
    	
    }
    
    public void spinSpeed(double speed){
    	//spinnerMotor.setPID(SmartDashboard.getNumber("ShooterP"), SmartDashboard.getNumber("ShooterI"), SmartDashboard.getNumber("ShooterD"));
    	if(spinnerMotor.getControlMode()!=CANTalon.TalonControlMode.Speed){
    		spinnerMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
    		spinnerMotor.enableControl();
    	}
    	if(speed==0){
    		Robot.shooter.isSpinning=false;
    		spinnerMotor.set(0);
    	}else{
    		Robot.shooter.isSpinning=true;
    		spinnerMotor.set(speed);
    	}
    }
    public boolean isSpinning(){
    	return isSpinning;
    	
    }
    public double getSpinnerRPM(){
    	//System.out.println("Check speed of motor");
    	//SmartDashboard.putNumber("Speed", spinnerMotor.getSpeed());
    	return spinnerMotor.getSpeed();
    }
    public double getCurrent(){
    	return spinnerMotor.getOutputCurrent();
    }
    
    public boolean getMode(){
    	return Robot.oi.shooterModeSwitch.get();
    }
    
}


