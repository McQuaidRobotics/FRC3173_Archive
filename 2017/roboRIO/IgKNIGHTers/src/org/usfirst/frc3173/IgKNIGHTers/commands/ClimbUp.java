// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3173.IgKNIGHTers.commands;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3173.IgKNIGHTers.Robot;

/**
 * This code was written by:
 * ---------------------------------------
 * @author JohnMarangola
 * ---------------------------------------
 * This program 'ClimberClimb' is the standAlone operational code to "Climb" in a fully automated fashion
 */

public class ClimbUp extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	private boolean isFirstIteration = true, hasBeenInit = false;
	private final double CLIMBER_CLIMBING_SPEED = 1.0;
	private final double seconds_Past_Switch_Activation = 30;
	private long currentTimeMillis, ms_Past_Switch, startMillisTime, currentDifferenceMillis;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public ClimbUp() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	// here I have to fire one of the pnuematics so that it is down
    	// this was removed but ima keep it in in case its reinstated boi
    	//Robot.climber.operateClimberPnuematic("Reverse");
    	// resume original code
    	 startMillisTime = System.currentTimeMillis();
    	 ms_Past_Switch = (long) (seconds_Past_Switch_Activation * 1000);
    	 currentTimeMillis = System.currentTimeMillis();
    	 isFirstIteration = false;
 
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    		System.out.println(Robot.climber.getSpeed());
    			// again commenting out below
    			//Robot.climber.operateClimberPnuematic("Reverse");
    	    	if (isFirstIteration){
    	    		 startMillisTime = System.currentTimeMillis();
    	        	 ms_Past_Switch = (long) (seconds_Past_Switch_Activation * 1000);
    	        	 currentTimeMillis = System.currentTimeMillis();
    	        	 isFirstIteration = false;
    	    	}
    	    	else{
    			// resume original code
    	    	 
    	    	 currentTimeMillis = System.currentTimeMillis();
    	    	 isFirstIteration = false;
    	    	 Robot.climber.RotateClimberMotor(CLIMBER_CLIMBING_SPEED);
    	    	}
    	    	System.out.println(Robot.climber.getInput());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	currentTimeMillis = System.currentTimeMillis();
    	currentDifferenceMillis = Math.abs(currentTimeMillis - startMillisTime);
    
    	if (currentDifferenceMillis >= ms_Past_Switch || Robot.climber.getInput()) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	// make sure motor is stopped
    	isFirstIteration = true;
    	Robot.climber.RotateClimberMotor(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	// continue execution... can't stop climbing if someone accidentally hits a button inadvertently
    	
    }
}
