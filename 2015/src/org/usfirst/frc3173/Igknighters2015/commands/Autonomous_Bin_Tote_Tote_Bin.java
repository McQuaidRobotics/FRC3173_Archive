package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Autonomous_Bin_Tote_Tote_Bin extends CommandGroup {
    
    public  Autonomous_Bin_Tote_Tote_Bin() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	
    	//Bin & Tote
    	//Turn 180
    	//Bin
    	//tote
    	//turn left
    	//go to safe zone
    	
    	addSequential(new CalibrateSensors(),Autonomous.sensorTime);
    	
    	addSequential(new AutonomousAcquireBin(true)); //get it, start it moving for later
    	addSequential(new OpenBinHolder());
    	addSequential(new AutonomousElevate(Robot.elevation.HOLD_BIN));
    	
    	addSequential(new AutonomousAcquireTote(false)); //don't elevate
    //	addSequential(new AutonomousTurnDegrees(90,true,Autonomous.AUTO_DRIVE_SPEED)); //90 clockwise
    	addSequential(new CloseBinHolder());
    	addSequential(new OpenElevationArms());
    	addSequential(new AutonomousElevate(Robot.elevation.TOTE));
    	//addSequential(new AutonomousTurnDegrees(90,true, Autonomous.AUTO_DRIVE_SPEED)); //90 clockwise
    	
    	
    	addSequential(new AutonomousAcquireTote(false)); //don't elevate
    	addSequential(new AutonomousAcquireTote(false)); //don't move arm\
    	//addSequential(new AutonomousTurnDegrees(90,true,Autonomous.AUTO_DRIVE_SPEED)); //counterclockwise
    	addSequential(new AutonomousDrive(Autonomous.AUTO_DRIVE_SPEED, Autonomous.AUTO_DRIVE_SPEED), Autonomous.TIME_TIL_SAFE_ZONE);
    }
    
}
