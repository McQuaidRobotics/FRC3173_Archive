/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc3173.IgKnighters2013.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc3173.IgKnighters2013.RobotMap;
import org.usfirst.frc3173.IgKnighters2013.commands.Climb;
import org.usfirst.frc3173.IgKnighters2013.utilities.CANJaguar;

/**
 *
 * @author Thomas Skysword
 */
public class Climber extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public Relay climberSolenoid = RobotMap.ClimberSolenoid;//Solenoid climberSolenoid = RobotMap.ClimberSolenoid;
   // public Solenoid leftclimberSolenoid = RobotMap.rightClimber;
    //public CANJaguar climberMotor = RobotMap.ClimberJaguar;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Climb());
    }
    /**
     * @deprecated use solenoid climber instead
     */
    public void ClimberUp(){
       // climberMotor.setX(-0.5);
    }
    /**
     * @deprecated use solenoid climber instead
     */
    public void ClimberDown(){
       // climberMotor.setX(0.5);
    }
    /**
     * now used for lights
     * @param walk is the true and false value for direction of relay. When in false, when out true;
     */
    public void solenoidClimber(boolean walk){
        if(walk){
        climberSolenoid.set(Relay.Value.kForward);
        }else{
            climberSolenoid.set(Relay.Value.kOff);
        }
        //leftclimberSolenoid.set(walk);
    }
}
