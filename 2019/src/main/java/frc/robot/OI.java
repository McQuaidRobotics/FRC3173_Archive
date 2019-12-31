/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.climber.*;
//import frc.robot.commands.drives.InvertControls;
//import frc.robot.commands.drives.TurnAround;
import frc.robot.commands.drives.TurnToAngle;
import frc.robot.commands.hatch.*;
import frc.robot.commands.hatch.PushHatchAcquire;
import frc.robot.commands.hatch.PushHatchRelease;
import frc.robot.util.JoyButton;
//import frc.robot.commands.auto.PathfinderTest;
import frc.robot.commands.auto.climb.AutoClimb;
import frc.robot.commands.buddy.*;
import frc.robot.commands.cargo.ToggleCargo;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  public static Joystick leftJoystick;
  public static Joystick rightJoystick;
  public static Joystick debugGamepad;
  public static XboxController mainGamepad;

  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());

  public OI() {
    leftJoystick = new Joystick(0);
    rightJoystick = new Joystick(1);
    debugGamepad = new Joystick(5);
    mainGamepad = new XboxController(2);

    // Right Joystick
    // ------------------------------------------
    /*  Trigger is set in climb/WaitForButton.java
    new JoyButton(rightJoystick, 1)
      .whenPressed();
    */

    new JoyButton(rightJoystick, 1)
        .whenPressed(new ToggleCargo());

    new JoyButton(rightJoystick, 2)
      .whenPressed(new PushHatchRelease());
    
    /*
    new JoyButton(rightJoystick, 3)
      .whenPressed(new ElevatorMoveTo(Constants.ELEVATOR_MIDDLE));
    
    new JoyButton(rightJoystick, 4)
      .whenPressed(new ElevatorMoveTo(Constants.ELEVATOR_BOTTOM));
    
    new JoyButton(rightJoystick, 5)
      .whenPressed(new ElevatorMoveTo(Constants.ELEVATOR_TOP));
    */

    /*
    new JoyButton(rightJoystick, 6) 
      .whenPressed(new ExtendArms());
    */
    
      
    new JoyButton(rightJoystick, 7) 
      .whenPressed(new Buddy2());
      

    new JoyButton(rightJoystick, 8)
      .whenPressed(new StopAll());


    new JoyButton(rightJoystick, 9)
      .whenPressed(new TogglePantsLimit("forward"));
    
      
    new JoyButton(rightJoystick, 10)
      .whenPressed(new PushHatchAcquire());
      

    new JoyButton(rightJoystick, 11)
      .whenPressed(new AutoClimb());

    /*
    TODO: (Chuck): Manual Elevator Speed Control
    new JoyButton(rightJoystick, 12)
      .whenPressed();
    */
    

    // Left Joystick
    // ------------------------------------------
    /*  
    new JoyButton(leftJoystick, 1)
      .whenPressed();
    */

    new JoyButton(leftJoystick, 2)
      //.whenPressed(new MoveXRevs());
      .whenPressed(new RunHoist());

    /*
    new JoyButton(leftJoystick, 4)
      .whenPressed();
    */

    /*
    new JoyButton(leftJoystick, 5)
      .whenPressed();
    */

    /*
    new JoyButton(leftJoystick, 6)
      .whenPressed();
    */

    /*
    new JoyButton(leftJoystick, 7)
      .whenPressed();
    */
    
    /*
    new JoyButton(leftJoystick, 8)
      .whenPressed();
    */

    /*
    new JoyButton(leftJoystick, 9)
      .whenPressed();
    */

    /*
    new JoyButton(leftJoystick, 10)
      .whenPressed();
    */

    /*
    new JoyButton(leftJoystick, 11)
      .whenPressed();
    */

    /*
    new JoyButton(leftJoystick, 12)
      .whenPressed();
    */
    
    // Main Gamepad
    // ------------------------------------------
      // button b
      new JoyButton(mainGamepad, 2)
        .whenPressed(new PushHatchAcquire());
      
       // button x
      new JoyButton(mainGamepad, 3)
        .whenPressed(new PushHatchRelease());
      
      // button y
      new JoyButton(mainGamepad, 4)
        .whenPressed(new TurnToAngle(180));

      // start button
      new JoyButton(mainGamepad, 8)
        .whenPressed(new HatchShoot());
  }
}
