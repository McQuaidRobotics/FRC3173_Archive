package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.subsystems.Drives;

import edu.wpi.first.wpilibj.command.Command;

public class DrivesShiftOpState extends Command {

	private Drives.OperativeState opState;
	private boolean actionCompleted;
    public DrivesShiftOpState(Drives.OperativeState in) {
        requires(Robot.drives);
    	actionCompleted = false;
    	opState = in;
    }
    
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	System.out.println("[COMMAND] DrivesShiftOpState: Switching to " + opState.toString());
    	if (opState == Drives.OperativeState.ARCADE_DRIVE)
    		Robot.drives.setArcadeOp();
    	else if (opState == Drives.OperativeState.TANK_DRIVE)
    		Robot.drives.setTankOp();
    	actionCompleted = true;
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return actionCompleted;
    }
}