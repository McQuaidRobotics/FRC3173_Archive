/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

    // PDP
    /*
    PDP Port    Breaker(Amps)  Control      Device  CAN ID      Note
    0               40          DriveRight   Talon      1       Mag Encoder   
    1               40          DriveRight   Talon      2
    2
    3               40          Pants        Spark      10
    4
    5
    6
    7
    8
    9
    10               30         Elevator    Spark       7   
    11               30         Urgers      Spark       11
    12               30         Arms        Spark       12
    13               40         DrivesLeft  Talon       4       Mag Encoder
    14               40         DrivesLeft  Talon       5   
    15


    */
    public static PowerDistributionPanel pdp;

    // navX-MXP IMU
    public static AHRS imu;

    //drives: all objects
    public static WPI_TalonSRX drivesLeftMaster;
    public static WPI_TalonSRX drivesRightMaster;
    public static WPI_TalonSRX drivesLeftFollower;
    public static WPI_TalonSRX drivesRightFollower;
    public static DifferentialDrive drivesController;

    //climb
    public static CANSparkMax climbUrgers;
    public static CANSparkMax climbPants;
    public static CANSparkMax climbArms;

    //elevator
    public static CANSparkMax elevator;

    // urger pusher
    public static Solenoid armsRelease;

    // cargo
    public static Solenoid cargoLauncher;

    //hatch stuff
    public static Solenoid hatchRelease;
    public static Solenoid hatchImpact;

    public static Solenoid buddy1;
    public static Solenoid buddy2;
    
    public static void init() {
        // if a pdp has no CAN ID, the stdout/stderr
        // gets spammed with a "CAN: Message not found" error message and traceback
        pdp = new PowerDistributionPanel(61);
        imu = new AHRS(SPI.Port.kMXP);

        //set up master talons
        drivesLeftMaster = new WPI_TalonSRX(4); 
        drivesRightMaster = new WPI_TalonSRX(1);

        //set up slave talons
        drivesLeftFollower = new WPI_TalonSRX(5);
        drivesRightFollower = new WPI_TalonSRX(2);

        // Curent Limiting for Drive train
        drivesLeftMaster.configPeakCurrentLimit(Constants.DRIVE_PEAK_CURRENT_LIMIT);
        drivesLeftMaster.configPeakCurrentDuration(Constants.DRIVE_PEAK_CURRENT_DURATION);
        drivesLeftMaster.configContinuousCurrentLimit(Constants.DRIVE_CONTINUOUS_CURRENT_LIMIT);
        drivesLeftMaster.enableCurrentLimit(true);

        drivesLeftFollower.configPeakCurrentLimit(Constants.DRIVE_PEAK_CURRENT_LIMIT);
        drivesLeftFollower.configPeakCurrentDuration(Constants.DRIVE_PEAK_CURRENT_DURATION);
        drivesLeftFollower.configContinuousCurrentLimit(Constants.DRIVE_CONTINUOUS_CURRENT_LIMIT);
        drivesLeftFollower.enableCurrentLimit(true);

        drivesRightMaster.configPeakCurrentLimit(Constants.DRIVE_PEAK_CURRENT_LIMIT);
        drivesRightMaster.configPeakCurrentDuration(Constants.DRIVE_PEAK_CURRENT_DURATION);
        drivesRightMaster.configContinuousCurrentLimit(Constants.DRIVE_CONTINUOUS_CURRENT_LIMIT);
        drivesRightMaster.enableCurrentLimit(true);

        drivesRightFollower.configPeakCurrentLimit(Constants.DRIVE_PEAK_CURRENT_LIMIT);
        drivesRightFollower.configPeakCurrentDuration(Constants.DRIVE_PEAK_CURRENT_DURATION);
        drivesRightFollower.configContinuousCurrentLimit(Constants.DRIVE_CONTINUOUS_CURRENT_LIMIT);
        drivesRightFollower.enableCurrentLimit(true);


        drivesLeftMaster.setInverted(false);
        drivesLeftFollower.setInverted(false);

        drivesRightMaster.setInverted(true);
        drivesRightFollower.setInverted(true);

        //master leading slave motors
        drivesLeftFollower.follow(drivesLeftMaster);
        drivesRightFollower.follow(drivesRightMaster);
    
        //set up encoders
        drivesLeftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        drivesLeftMaster.setSensorPhase(true);
        
        drivesRightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        drivesRightMaster.setSensorPhase(true);

        drivesLeftMaster.configOpenloopRamp(0.4f, 0);
        drivesRightMaster.configOpenloopRamp(0.4f, 0);
        
        //set up simple differential manager so that we can just enter in tank or arcade drive
        drivesController = new DifferentialDrive(drivesLeftMaster, drivesRightMaster);
        drivesController.setSafetyEnabled(false);
        drivesController.setExpiration(0.1);
        drivesController.setMaxOutput(1.0);

        //climb motors
        climbUrgers = new CANSparkMax(11, MotorType.kBrushless);
        climbPants = new CANSparkMax(10, MotorType.kBrushless);
        climbArms = new CANSparkMax(12, MotorType.kBrushless);

        //elevator
        elevator = new CANSparkMax(7, MotorType.kBrushless);

        //hatch
        hatchRelease = new Solenoid(20, 0);
        hatchImpact = new Solenoid(20, 1);

        // urger pusher
        armsRelease = new Solenoid(20, 3);

        // cargo
        cargoLauncher = new Solenoid(20, 2);

        // Buddy lift
        buddy1 = new Solenoid(20, 5);
        buddy2 = new Solenoid(20, 6);
    }
}
