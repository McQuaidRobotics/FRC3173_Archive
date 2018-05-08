package org.usfirst.frc3173.Igknighters2015.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Autonomous_Tote_Bin extends CommandGroup {
    
    public  Autonomous_Tote_Bin() {
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
    	
    	//sensors
    	//get tote
    	//forward
    	//get bin
    	//turn 90 counterclockwise
    	//forward
    	
    	//addSequential(new CalibrateSensors(), Autonomous.sensorTime);
    	addSequential(new AutonomousAcquireTote(true)); //elevate
    	addSequential(new AutonomousAcquireBin(false)); //don't move arm
    	//addSequential(new AutonomousTurnDegrees(90, false, Autonomous.AUTO_DRIVE_SPEED)); //90 counterclockwise
    	addSequential(new AutonomousTurn90DeadRecon(false),AutonomousTurn90DeadRecon.TIME); //counterclockwise
    	addSequential(new AutonomousDrive(Autonomous.AUTO_DRIVE_SPEED, Autonomous.AUTO_DRIVE_SPEED), Autonomous.TIME_TIL_SAFE_ZONE);
    }
}
