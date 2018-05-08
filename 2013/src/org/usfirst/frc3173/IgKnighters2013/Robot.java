// RobotBuilder Version: 0.0.2
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in th future.
package org.usfirst.frc3173.IgKnighters2013;
import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc3173.IgKnighters2013.commands.*;
import org.usfirst.frc3173.IgKnighters2013.subsystems.*;
import org.usfirst.frc3173.IgKnighters2013.utilities.*;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    Command autonomousCommand;
    Command climberCommand;
    public static Command shooterCommand;
    public static OI oi;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static Drives drives;
    //public static Acquisition acquisition;
    public static Tilter Tilter;
    public static Shooter shooter;
    
	public static Climber climber;
	public static Blinky blinky;
	public static Vision vision;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        RobotMap.init();
        RobotMap.driverstation.println(DriverStationLCD.Line.kUser1,1,"Initialization Starting!");
        RobotMap.driverstation.updateLCD();
	
        RobotMap.driverstation.updateLCD();
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        drives = new Drives();
        RobotMap.driverstation.updateLCD();
        //acquisition = new Acquisition();
        climber = new Climber();
        RobotMap.driverstation.updateLCD();
        //climbing=new Climbing();
     
        shooter = new Shooter();
        RobotMap.driverstation.updateLCD();
        Tilter = new Tilter();
        RobotMap.driverstation.updateLCD();
         shooterCommand = new ShooterFireManual();
        RobotMap.driverstation.updateLCD();
		//blinky = new Blinky();
		//vision = new Vision();
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();
	RobotMap.driverstation.updateLCD();
        // instantiate the command used for the autonomous period
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        autonomousCommand = new Autonomous();
       RobotMap.driverstation.updateLCD();
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        RobotMap.driverstation.println(DriverStationLCD.Line.kUser1,1,"Init. Complete");
        RobotMap.driverstation.updateLCD();
    }
    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) {
            if(RobotMap.autoing.get()){
            autonomousCommand.start();
            }
        }  
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        RobotMap.driverstation.updateLCD();
        Scheduler.getInstance().run();
    }
    public void teleopInit() {
	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        //new ShooterInitialize();
        //Robot.oi.leftTrigger.whenPressed(new DriveWithJoysticksAbsolute());
        //Robot.oi.leftTrigger.whenReleased(new DriveWithJoysticks());
        //Robot.oi.GamePadX.whenPressed(new ClimberOut());
        System.out.println("Hope?");
        //This code does not work     
        
                System.out.println("Yay!!!!");

    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        RobotMap.compressor.start();
        RobotMap.driverstation.updateLCD();
    }
    /**
     * This function called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}