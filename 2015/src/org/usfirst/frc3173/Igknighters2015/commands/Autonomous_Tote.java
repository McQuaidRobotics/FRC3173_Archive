package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Autonomous_Tote extends CommandGroup {
    
    public  Autonomous_Tote() {
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
    	//forward
    	//acquire tote
    	//turn 90 right
    	//forward
    	
    	//addSequential(new CalibrateSensors(), Autonomous.sensorTime);
    	addSequential(new AutonomousElevate(Robot.elevation.LOW));
    	addSequential(new AutonomousWait(),2);
    	addSequential(new AutonomousAcquireTote(false),4); //don't elevate arm
    	addSequential(new AutonomousWait(),.5);
    	addSequential(new StopAcquisitionWheels());
    	//addSequential(new StackOneTote());
    	//addSequential(new AutonomousTurnDegrees(90,true,1),1); //90 clockwise
    	//addSequential(new AutonomousTurn90DeadRecon(true),AutonomousTurn90DeadRecon.TIME);
    	//addSequential(new AutonomousGoForward(),Autonomous.TIME_TIL_SAFE_ZONE);
    }
}
