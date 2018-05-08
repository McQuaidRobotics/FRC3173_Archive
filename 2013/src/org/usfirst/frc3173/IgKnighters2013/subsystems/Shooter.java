/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc3173.IgKnighters2013.subsystems;
import org.usfirst.frc3173.IgKnighters2013.RobotMap;
import org.usfirst.frc3173.IgKnighters2013.commands.*;
import edu.wpi.first.wpilibj.*;
/* DO NOT CHANGE THIS COMMENT!
 * It's needed so the RobotBuilder doesn't put the import back in
import edu.wpi.first.wpilibj.can.*;
*/
import org.usfirst.frc3173.IgKnighters2013.utilities.CANJaguar;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Csimons2016 and Thomas Skysword
 */
public class Shooter extends Subsystem {
    
    public double manualSpeed = 2000;
    private double interval =1 ;

    protected void initDefaultCommand() {
        setDefaultCommand(new ShooterInitialize());
    }
    /**
     * moves a shooter from storage to shooter.
     * @param asunder is a boolean. when true sets the shooter solenoid to out. when false, the shooter solenoid is in
     */
    public void fireManual(boolean asunder){
        if(asunder){
            RobotMap.storageSolenoid.set(Relay.Value.kForward);//.set(DoubleSolenoid.Value.kForward);
        }else{
            RobotMap.storageSolenoid.set(Relay.Value.kOff);//.set(DoubleSolenoid.Value.kReverse);
        }
    }
    /**
     * will eventually hold auto code.
     */
    public void fireAuto(){
        
    }
    /**
     * will eventually hold auto code.
     */
    public void fireTower(){
        
    }
    /**
     * increases manual percentage, use only in percentVBus.
     */
    public void upManualSpeed(){
        if(manualSpeed<.99){//.95){
            manualSpeed+=.05;
        }
    }
    /**
     * increases manual RPM, use only in SpeedMode.
     */
    public void upManualSpeedA(){
        if(manualSpeed<5800){//.95){
            manualSpeed+=250;
        }
    }
    /**
     * increases manual RPM, use only in SpeedMode; changes with intervals.
     */
    public void upManualSpeedB(){
        if(manualSpeed<5800){//.95){
            manualSpeed+=250*interval;
        }
    }
    /**
     * decreases manual RPM, use only in SpeedMode.
     */
    public void downManualSpeedA(){
        if(manualSpeed>1500){//.95){
            manualSpeed-=250;
        }
    }
    /**
     * decreases manual RPM, use only in SpeedMode; changes with intervals.
     */
     public void downManualSpeedB(){
        if(manualSpeed>1500){//.95){
            manualSpeed-=250*interval;
        }
    }
    public void downManualSpeed(){
        if(manualSpeed>.05){
            manualSpeed-=.05;
        }
    }
    /**
     * decreases manual percentage, use only in percentVBus.
     */
    public void setSpeedUp(double speed){
        RobotMap.shooterShooter.setX(-speed);
    }
    /**
     * 
     * @return speed percent. Used in percentVBus
     */
    public int getSpeed(){
        return (int)(manualSpeed*100);
    }
    /**
     * 
     * @return speed percent. Used in speedMode
     */
    public int getSpeedA(){
        return (int)((manualSpeed/6060)*100);
    }
    /**
     * 
     * @return speed RPM. Used in speedMode.
     */
    public int getSpeedB(){
        return (int) manualSpeed;
    }
    /**
     * 
     * @return more accurate RPM value. SpeedMode only.
     */
    public double getActualSpeed(){
        return manualSpeed;
    }
    /**
     * used for both SpeedMode and PercentVBus.
     * @param speed . SpeedMode=RPM. PercentVBus =0-1;
     */
    public void setSpeed(double speed){
        manualSpeed=speed;
        RobotMap.shooterShooter.setX(-speed);
    }
    /**
     * sets speed to current speed.
     */
    public void setSpeed(){
        RobotMap.shooterShooter.setX(-manualSpeed);
    }
    /**
     * sets speed to current speed.
     * @return current speed
     */
    public double setSpeedA(){
        RobotMap.shooterShooter.setX(-manualSpeed);
        return manualSpeed;
    }
 
    /**
     * moves acquisition in and out.
     * @param asunder if true , out; if false, in
     */
    public void movingAquisition(boolean asunder){
        if(asunder){
            RobotMap.AcQSolenoid.set(Relay.Value.kForward);//.set(DoubleSolenoid.Value.kForward);
        }else{
            RobotMap.AcQSolenoid.set(Relay.Value.kOff);//.set(DoubleSolenoid.Value.kReverse);
        }
    }
    public void setInterval(double inter){
        interval=inter;
    }
    /**
     * Checks current RPM and sets so that the motor is not trying to start in SpeedMode; helps avoid current faults.
     * @param firstRun takes a value to see if you are setting the jaguar to the correct mode for the first time
     * @return true when it is set to speed mode and false when set to percentVbus
     */
    public boolean checkRPM(boolean firstRun){
        if(RobotMap.shooterShooter.getSpeed()>=1200 && firstRun){
            RobotMap.shooterShooter.changeControlMode(edu.wpi.first.wpilibj.CANJaguar.ControlMode.kSpeed);
            RobotMap.shooterShooter.setPID(1,.01,0);//10,.001 ,.001 );10,.001 ,.001 );
            RobotMap.shooterShooter.setSpeedReference(edu.wpi.first.wpilibj.CANJaguar.SpeedReference.kEncoder);
            RobotMap.shooterShooter.configEncoderCodesPerRev(1);
            RobotMap.shooterShooter.enableControl(0.0);
            return true;
        }else if(RobotMap.shooterShooter.getSpeed()>=1200 &&!firstRun){
            RobotMap.shooterShooter.setSpeedReference(edu.wpi.first.wpilibj.CANJaguar.SpeedReference.kEncoder);

            return true;
        }else if(RobotMap.shooterShooter.getSpeed()<1200 && firstRun){
            RobotMap.shooterShooter.changeControlMode(edu.wpi.first.wpilibj.CANJaguar.ControlMode.kPercentVbus);
            RobotMap.shooterShooter.setSpeedReference(edu.wpi.first.wpilibj.CANJaguar.SpeedReference.kEncoder);

            RobotMap.shooterShooter.disableControl();
            
            return false;
        }else{
            RobotMap.shooterShooter.setSpeedReference(edu.wpi.first.wpilibj.CANJaguar.SpeedReference.kEncoder);

            return false;
        }
    }
}
