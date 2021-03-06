// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3173.Igknighters2015;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3173.Igknighters2015.commands.*;
import org.usfirst.frc3173.Igknighters2015.subsystems.*;
import org.usfirst.frc3173.Igknighters2015.utilities.MPU_6050;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

//Mr. Sadler is too formal. You may call me God.

public class Robot extends IterativeRobot {

    Command autonomousCommand;

    public static OI oi;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static Drives drives;
    public static Acquisition acquisition;
    public static Elevation elevation;
    public static BinHolder binHolder;
    int session;
    Image frame;
    public static MPU_6050 imu;
	private char imuAddress = 0x68;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    RobotMap.init();
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    	imu = new MPU_6050(I2C.Port.kOnboard, imuAddress, false, 2);
        drives = new Drives();
        acquisition = new Acquisition();
        elevation = new Elevation();
        binHolder = new BinHolder();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // OI must be constructed after subsystems. If the OI creates Commands 
        //(which it very likely will), subsystems are not guaranteed to be 
        // constructed yet. Thus, their requires() statements may grab null 
        // pointers. Bad news. Don't move it.
        oi = new OI();
        
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

        // the camera name (ex "cam0") can be found through the roborio web interface
        session = NIVision.IMAQdxOpenCamera("cam0",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);

        // instantiate the command used for the autonomous period
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        autonomousCommand = new Autonomous();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){

    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
    	imu.setOrientation(0);
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("Z-Orientation", Robot.imu.getZOrientation());
        SmartDashboard.putString("Mode",Robot.elevation.getControlMode().toString());
        System.out.println(Robot.imu.getZOrientation());
        SmartDashboard.putNumber("Position", Robot.elevation.getPosition());
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.

        if (autonomousCommand != null) autonomousCommand.cancel();
        Robot.drives.setRampRate(10);
    	Robot.elevation.percentVBusMode();
    	imu = new MPU_6050(I2C.Port.kOnboard, imuAddress, false, 6);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        NIVision.IMAQdxStartAcquisition(session);

        /**
         * grab an image, draw the circle, and provide it for the camera server
         * which will in turn send it to the dashboard.
         */

        NIVision.IMAQdxGrab(session, frame, 1);
            
        CameraServer.getInstance().setImage(frame);
        SmartDashboard.putNumber("Z-Orientation", Robot.imu.getZOrientation());
        System.out.println(Robot.imu.getZOrientation());
        SmartDashboard.putString("Mode",Robot.elevation.getControlMode().toString());
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
