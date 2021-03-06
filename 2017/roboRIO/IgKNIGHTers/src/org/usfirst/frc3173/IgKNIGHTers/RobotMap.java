// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3173.IgKNIGHTers;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	
	private static final int ModuleNumber = 20;
    public static CANTalon drivesLeftMotorTop;
    public static CANTalon drivesRightMotorTop;
    public static CANTalon drivesLeftMotorBottom;
    public static CANTalon drivesRightMotorBottom;
    public static DoubleSolenoid gearsGearPnuematic;
    public static Solenoid gearPnuematic1;
    public static Solenoid gearPnuematic2;
    public static DigitalInput gearsGearSensor;
    public static Solenoid fuelHandlingDumpGate;
    public static CANTalon fuelHandlingAcquireFuel;
    public static DoubleSolenoid climberArm;
    public static DigitalInput climberisAtTopSwitch;
    public static CANTalon climberclimbingMotor;
    public static Compressor compressor_Shared_FunctionalityRobotCompressor;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        drivesLeftMotorTop = new CANTalon(2);
        LiveWindow.addActuator("Drives", "LeftMotorTop", drivesLeftMotorTop);
        
        drivesRightMotorTop = new CANTalon(1);
        LiveWindow.addActuator("Drives", "RightMotorTop", drivesRightMotorTop);
        
        drivesLeftMotorBottom = new CANTalon(4);
        LiveWindow.addActuator("Drives", "LeftMotorBottom", drivesLeftMotorBottom);
        
        drivesRightMotorBottom = new CANTalon(3);
        LiveWindow.addActuator("Drives", "RightMotorBottom", drivesRightMotorBottom);
       
        climberclimbingMotor = new CANTalon(5);
        LiveWindow.addActuator("Climber", "climbingMotor", climberclimbingMotor);
        
        fuelHandlingAcquireFuel = new CANTalon(6);
        LiveWindow.addActuator("Fuel Handling", "AcquireFuel", fuelHandlingAcquireFuel);
       
        fuelHandlingDumpGate = new Solenoid(ModuleNumber, 1);
        LiveWindow.addActuator("Fuel Handling", "DumpGate", fuelHandlingDumpGate);
        
        gearsGearPnuematic = new DoubleSolenoid(ModuleNumber, 6, 7);
        LiveWindow.addActuator("Gears", "GearPnuematic", gearsGearPnuematic);

        compressor_Shared_FunctionalityRobotCompressor = new Compressor(0);

        climberisAtTopSwitch = new DigitalInput(3);
        LiveWindow.addSensor("Climber", "isAtTopSwitch", climberisAtTopSwitch);
    //    gearPnuematic1=new Solenoid(ModuleNumber,6);
      //  LiveWindow.addActuator("Gears", "GearPnuematic", gearPnuematic1);
        //gearPnuematic2=new Solenoid(ModuleNumber,7);
        //LiveWindow.addActuator("Gears", "GearPnuematic", gearPnuematic2);
        //gearsGearSensor = new DigitalInput(1);
        //LiveWindow.addSensor("Gears", "GearSensor", gearsGearSensor);
    }
}
