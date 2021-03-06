// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3173.IgKnighters2016;

import org.usfirst.frc3173.IgKnighters2016.commands.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;

import org.usfirst.frc3173.IgKnighters2016.subsystems.*;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
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

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public JoystickButton acquisitionIn;
    public JoystickButton acquisitionOut;
    public Joystick gamepad;
    public Joystick leftJoystick;
    public Joystick rightJoystick;
    
    public JoystickButton climberWinchUp;
    public JoystickButton climberWinchDown;
    public JoystickButton shootButton;
    public JoystickButton acquireButton;
    public JoystickButton winchDown;
    
    
    
    
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public Joystick switchPanel;
    public JoystickButton tractionControlSwitch;
    public JoystickButton climberTapeSpringUp;
    public JoystickButton climberTapeSpringDown;
    public JoystickButton shooterMotorOnOff;
    public DigitalInput tapeStop;
    public JoystickButton autoAim;
    public JoystickButton visionLED;
    public JoystickButton drivesChangeDirection;
    public JoystickButton acquisitionEnableBallSwitch;
    public JoystickButton autoShoot;
    public JoystickButton cancel;
    public JoystickButton shooterModeSwitch;
    

    public OI() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        rightJoystick = new Joystick(1);       
        leftJoystick = new Joystick(0);    
        gamepad = new Joystick(2);       
        switchPanel = new Joystick(3);
        
        acquisitionOut = new JoystickButton(gamepad, 3);
        acquisitionOut.whileHeld(new AcquisitionBeltMove(false));
        acquisitionIn = new JoystickButton(gamepad, 2);
        acquisitionIn.whileHeld(new AcquisitionBeltMove(true));
        climberTapeSpringUp = new JoystickButton(gamepad,5);
        climberTapeSpringUp.whileHeld(new ClimberTapeSpringIn(true));
        climberTapeSpringDown = new JoystickButton(gamepad,7);
        climberTapeSpringDown.whileHeld(new ClimberTapeSpringIn(false));   
        shooterMotorOnOff =new JoystickButton(gamepad,8);
        //shooterMotorOnOff.whenPressed(new ShooterRun(((rightJoystick.getZ()+1)/2.0)));
        //shooterMotorOnOff.whenReleased(new ShooterRun(0));
        shooterMotorOnOff.whileHeld(new ShooterRun(((-rightJoystick.getZ()+1)/2.0)));
        tapeStop = new DigitalInput(3);
        autoAim=new JoystickButton(rightJoystick, 3);
        autoAim.whileHeld(new AutoShootLeftRight());
        autoShoot=new JoystickButton(rightJoystick, 4);
        autoShoot.whileHeld(new AutoShoot());
        cancel=new JoystickButton(rightJoystick, 5);
        cancel.whenPressed(new CancelAllCommands());
        drivesChangeDirection=new JoystickButton(switchPanel,1);
        acquisitionEnableBallSwitch=new JoystickButton(switchPanel,2);
        shooterModeSwitch=new JoystickButton(switchPanel,3);
        
       
        
        
       
        // SmartDashboard Buttons

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        
        
        //tractionControlSwitch = new JoystickButton(switchPanel, 3);
        shootButton=new JoystickButton(leftJoystick, 8);
        acquireButton=new JoystickButton(leftJoystick, 9);
        
        //visionLED=new JoystickButton(rightJoystick, 4);
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    public Joystick getGamepad() {
        return gamepad;
    }

    public Joystick getLeftJoystick() {
        return leftJoystick;
    }

    public Joystick getRightJoystick() {
        
    	return rightJoystick;
        
    }
    


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    
    public Joystick getSwitchPanel() {
    	return switchPanel;
    }
}

