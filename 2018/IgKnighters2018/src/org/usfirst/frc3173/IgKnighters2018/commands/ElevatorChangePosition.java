package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.subsystems.Elevator;
import org.usfirst.frc3173.IgKnighters2018.subsystems.Elevator.ElevatorStates;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorChangePosition extends Command {

	private boolean actionCompleted;
	private boolean up;
	private Elevator.ElevatorStates state;
	
    public ElevatorChangePosition(boolean goingUp) {
        requires(Robot.elevator);
    	actionCompleted = false;
        up = goingUp;
    }
    
    public ElevatorChangePosition(Elevator.ElevatorStates state) {
    	requires(Robot.elevator);
    	actionCompleted = false;
    	this.state = state;
    }
    
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	if (state == null) {
	    	System.out.println("[COMMAND] ElevatorChangePosition: " + ((up) ? "Going up" : "Going down"));
	    	if (up) Robot.elevator.goUpPosition();
	    	else Robot.elevator.goDownPosition();
    	} else {
    		System.out.println("[COMMAND] ElevatorChangePosition: going to " + state.toString());
    		switch (state) {
    		case SCALE:
    			Robot.elevator.scaleState();
    			break;
    		case SWITCH:
    			Robot.elevator.switchState();
    			break;
    		case VAULT:
    			Robot.elevator.vaultState();
    			break;
    		case FLOOR:
    			Robot.elevator.floorState();
    			break;
    		}
    	}

    	actionCompleted = true;
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return actionCompleted;
    }
}
