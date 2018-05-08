
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

import org.usfirst.frc3173.IgKNIGHTers.commands.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;
import org.usfirst.frc3173.IgKNIGHTers.subsystems.*;

/**
 * @author *_Team_*
 * @category Operator_Interface
 * 
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//THIS COMMENT IS FOR BRANCHING PURPOSES ONLY U CAN DELETE IT IF U WANT :)
    //// CREATING BUTTONS
	// BRANCHING...
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
    public JoystickButton dump;
    public Joystick mecunumJoystick;
    public JoystickButton ClimbUp;
    public JoystickButton FuelAcquisitionController;
    public JoystickButton DumpGateOpen;
    public JoystickButton ManualGear;
    public JoystickButton ManualGearDown;
    public JoystickButton toggleFuelAcquisitionOn;
    public JoystickButton toggleClimberPnuematicState;
    public JoystickButton AbortClimber;
    public JoystickButton LowSpeed;
    public JoystickButton reverseClimber;
    public JoystickButton ReverseBall;
 


    public OI() {
        // BEGIN AUTOGENERATED CODE, SOURCE~=ROBOTBUILDER ID=CONSTRUCTORS
        mecunumJoystick = new Joystick(0);
        
        ManualGear=new JoystickButton(mecunumJoystick, 3);
        ManualGear.whenReleased(new ManualGear());
        ReverseBall=new JoystickButton(mecunumJoystick,2);
        ReverseBall.whileHeld(new ReverseBallIntake());
        ClimbUp = new JoystickButton(mecunumJoystick, 4);
        ClimbUp.whenPressed(new ClimbUp());
        //reverseClimber = new JoystickButton(mecunumJoystick, 4);
        //reverseClimber.whileHeld(new ReverseClimber());
        AbortClimber = new JoystickButton(mecunumJoystick, 6);
        AbortClimber.whenPressed(new AbortClimber());
        LowSpeed=new JoystickButton(mecunumJoystick,8);
        LowSpeed.whenPressed(new LowSpeedMotor());
        
        FuelAcquisitionController = new JoystickButton(mecunumJoystick, 1);
        FuelAcquisitionController.whileHeld(new DrawInBalls());
       
        //FuelAcquisitionController.whileHeld(new ReverseBallIntake());
       
        
        DumpGateOpen = new JoystickButton(mecunumJoystick,5 );
        DumpGateOpen.whenPressed(new DumpBalls());
        toggleFuelAcquisitionOn = new JoystickButton(mecunumJoystick, 9);
        toggleFuelAcquisitionOn.whenReleased(new ToggleFuelSpinnerOn());
       

        // SmartDashboard Buttons
        SmartDashboard.putData("Autonomous Command", new AutonomousCommand());
        SmartDashboard.putData("DrawInBalls", new DrawInBalls());
        SmartDashboard.putData("ReverseBallIntake", new ReverseBallIntake());
        SmartDashboard.putData("Climb Up", new ClimbUp());
        SmartDashboard.putData("ManualGear", new ManualGear());
       // SmartDashboard.putData("AutoGear", new AutoGear());
        SmartDashboard.putData("DumpBalls", new DumpBalls());
        SmartDashboard.putData("HighGoal", new HighGoal());
        SmartDashboard.putData("Mecunum Drive", new MecunumDrive());

        
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    public Joystick getMecunumJoystick() {
        return mecunumJoystick;
    }


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
}