/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc3173.IgKnighters2013.commands;


import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc3173.IgKnighters2013.OI;
import org.usfirst.frc3173.IgKnighters2013.Robot;
import org.usfirst.frc3173.IgKnighters2013.RobotMap;
import org.usfirst.frc3173.IgKnighters2013.utilities.GamePad.*;

/**
 *
 * @author Thomas Skysword
 */
public class Climb extends Command {
    private boolean previousGamePadState5=false;
    private boolean asunder=true;
    public Climb() {
        // Use requires() here to declare subsystem dependencies
         // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.climber);
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        RobotMap.driverstation.println(DriverStationLCD.Line.kUser1,1,this.toString() + " starting");
        asunder=true;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {//turns on and off the lights
        if(Robot.oi.gamepad.getRawButton(5) && !previousGamePadState5){//refers to the front left shoulder
       
        asunder=!asunder;
        }
         Robot.climber.solenoidClimber(asunder);
        previousGamePadState5=Robot.oi.gamepad.getRawButton(5);
        //System.out.println(RobotMap.compressor.getPressureSwitchValue());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
     
            return false;
        
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.climber.solenoidClimber(false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.climber.solenoidClimber(false);
    }
}
