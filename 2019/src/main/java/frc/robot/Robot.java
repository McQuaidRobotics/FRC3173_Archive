/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import frc.robot.commands.auto.PathfinderTest;
import frc.robot.subsystems.*;
import frc.robot.OI;
import frc.robot.util.ConstantsToShuffleboard;
//import edu.wpi.first.wpilibj.CameraServer;
import frc.robot.util.Timekeeper;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static Arms arms;
  public static Pants pants;
  public static Urgers urgers;
  public static Drives drives;
  public static Hatch hatch;
  public static OI oi;
  public static Elevator elevator;
  public static Cargo cargo;
  public static Timekeeper timekeeper;

  private static ConstantsToShuffleboard constantinople = new ConstantsToShuffleboard();
  private static double time = 0;
  private static boolean statesInitialized = false;

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    RobotMap.init();

    //CameraServer.getInstance().startAutomaticCapture();

    urgers = new Urgers();
    arms = new Arms();
    pants = new Pants();
    hatch = new Hatch();
    elevator = new Elevator();
    drives = new Drives();
    cargo = new Cargo();
    oi = new OI();
    
    timekeeper = new Timekeeper();
    constantinople.init();
    // possible flaw: if another part of the code changes a constant, it might not change.
    // perhaps we can remedy this by hooking into a static method in ConstantsToShuffleboard
    // to set a variable.

    //hatch.impactExtend();

    // zero encoders on startup
    drives.zeroEncoders();

    // m_chooser.setDefaultOption("Default Auto", new PathfinderTest());
    //SmartDashboard.putData("Auto mode", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    //NavXBoardTest.tick();
    double dt = timekeeper.tick();
    time += dt;
    if(time >= 3 * 1000) {  // every 3 seconds, do this:
      //System.out.println("3 SECONDS PASSED : " + time);
      //System.out.println("\n\n\n\n\nTARGET: " + Constants.ELEVATOR_SMARTCURRENT_LIMIT + "\n\n\n\n\n");
      time = 0;
      constantinople.tick();  // read and write from shuffleboard
    }
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
    // zero the encoders on disable startup
    drives.zeroEncoders();
    RobotMap.imu.zeroYaw();
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {

    /*
    m_autonomousCommand = m_chooser.getSelected();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
    */
    setInitRobotState();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  public void setInitRobotState() {
    //RobotMap.armsRelease.set(false);
    RobotMap.cargoLauncher.set(false);

    elevator.zeroEncoder();
    arms.zeroEncoder();
    pants.zeroEncoder();
    urgers.zeroEncoder();
    statesInitialized = true;
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    if(statesInitialized) {
      return;  // don't zero the encoders again if we've already done it
    }
    setInitRobotState();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
