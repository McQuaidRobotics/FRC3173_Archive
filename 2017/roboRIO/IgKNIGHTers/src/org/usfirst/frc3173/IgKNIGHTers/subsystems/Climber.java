// RobotBuilder Version: 2.0
//


/* Programming done by
 *  -----------------------------------------------
 *  @JohnMarangola of team 3173
 *  -----------------------------------------------
 * 
 */


package org.usfirst.frc3173.IgKNIGHTers.subsystems;

import org.usfirst.frc3173.IgKNIGHTers.RobotMap;
import org.usfirst.frc3173.IgKNIGHTers.commands.*;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.command.Subsystem;


/**
 * @author JohnMarangola
 */
public class Climber extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final static DoubleSolenoid arm = RobotMap.climberArm;
    public final DigitalInput isAtTopSwitch = RobotMap.climberisAtTopSwitch;
    private final CANTalon climbingMotor = RobotMap.climberclimbingMotor;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS


    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    public void RotateClimberMotor(double d)
    {
    	climbingMotor.set(d);
    }
    
    public boolean getInput()
    {
    	return isAtTopSwitch.get();
    }
    public double getSpeed()
    {
    	
    	return climbingMotor.get();
    }
    //public enum getPnuematicPosition()
    //{
    //	return arm.get();
   // }
   
  
    
    
  
}
