// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3173.IgKnighters2018;

import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousTurnToHeading;
import org.usfirst.frc3173.IgKnighters2018.utilities.NewPowerDistributionPanel;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix. motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.navx.frc.AHRS.SerialDataType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * @TEAM
 */
public class RobotMap {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	public static WPI_TalonSRX drivesLeftMaster;
    public static WPI_TalonSRX drivesRightMaster;
    public static WPI_TalonSRX drivesLeftFront;
    public static WPI_TalonSRX drivesRightFront;
    public static WPI_TalonSRX drivesLeftBack;
    public static WPI_TalonSRX drivesRightBack;
    public static DifferentialDrive drivesController;
    public static Solenoid drivesShifter;
    public static AHRS imu;
    
    public static WPI_TalonSRX acquisitionLeft;
    public static WPI_TalonSRX acquisitionRight;
    public static WPI_TalonSRX acquisitionPivot;
    public static Solenoid acquisitionSolenoid;
    public static AnalogInput acquisitionRangeSensor;
    
   
    public static WPI_TalonSRX elevatorMotor;
    public static WPI_TalonSRX elevatorMotorSlave;
    public static Solenoid elevatorShifter;
    
    public static DoubleSolenoid climberLock;

    public static NewPowerDistributionPanel pdp;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        drivesLeftMaster = new WPI_TalonSRX(1); 
        drivesRightMaster = new WPI_TalonSRX(4);

        drivesLeftFront = new WPI_TalonSRX(2);
        drivesRightFront = new WPI_TalonSRX(5);
        drivesLeftBack = new WPI_TalonSRX(3);
        drivesRightBack = new WPI_TalonSRX(6);
        
        imu = new AHRS(SerialPort.Port.kMXP, SerialDataType.kProcessedData, (byte)50);
        
        drivesRightMaster.setInverted(true);
        drivesRightFront.setInverted(true);
        drivesRightBack.setInverted(true);
        
        // 3 and 4 are drives encoders
        drivesShifter = new Solenoid(20, 3);
        drivesController = new DifferentialDrive(drivesLeftMaster, drivesRightMaster);
        drivesController.setSafetyEnabled(false);
        drivesController.setExpiration(0.1);
        drivesController.setMaxOutput(1.0);
        
        elevatorMotor = new WPI_TalonSRX(7);
        elevatorMotorSlave = new WPI_TalonSRX(8);
        elevatorShifter = new Solenoid(20, 1);
        
        climberLock = new DoubleSolenoid(20, 2, 4);
        
        acquisitionLeft = new WPI_TalonSRX(10);
        acquisitionRight = new WPI_TalonSRX(9);
        acquisitionPivot = new WPI_TalonSRX(11);
        acquisitionRangeSensor = new AnalogInput(0);
        acquisitionSolenoid = new Solenoid(20, 7);
        
        //pdp = new NewPo0werDistributionPanel();
    }
}