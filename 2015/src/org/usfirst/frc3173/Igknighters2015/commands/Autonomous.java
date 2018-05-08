package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Autonomous extends Command {

	public final static double sensorTime = 1; //TODO:Find time sensors need to calibrate
	public final static double AUTO_TURN_SPEED=.5; //TODO:
	public final static double AUTO_DRIVE_SPEED=1;
	public final static double TIME_TIL_SAFE_ZONE=3.1;
	public final static double DISTANCE_FROM_WALL=40; 
	public final static double RAMP_RATE=3;

    public Autonomous() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    //look to use parallels in command groups
    //use sensors
    //look to add elevate boolean parameter to AutoAcquires
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.elevation.positionMode();
    	int run = 5;
    	switch(run){
    		case 0:
    			new AutonomousWait().start();
    			break;
       		case 1:
        		new Autonomous_Bin_Tote().start();
    			break;
    		case 2:
            	new Autonomous_Bin_Tote_Not_Let_Go().start();
    			break;
    		case 3:
    			new Autonomous_Bin_Tote_Over_Step().start();
    			break;
    		case 4:
    			new Autonomous_Bin_Tote_Not_Let_Go_Over_Step().start();
    			break;
    		case 5: 
    			new Autonomous_Nothing().start();
    			break;
    		default:  
    		  	new AutonomousWait().start();
    			break;
    	}
    	Robot.elevation.percentVBusMode();
	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
