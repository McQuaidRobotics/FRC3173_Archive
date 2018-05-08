package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Autonomous_Bin extends CommandGroup {
    
    public  Autonomous_Bin() {
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
    	
    	//Calibrate sensors
    	//drive forward
    	//acquire
    	//turn 90 right
    	//forward
    	
    	//addSequential(new CalibrateSensors(),Autonomous.sensorTime);
    	addSequential(new AutonomousElevate(Robot.elevation.LOW));
    	addSequential(new AutonomousAcquireBin(false)); //don't elevate arm
    	addSequential(new StackOneTote());
    	//addSequential(new AutonomousTurnDegrees(90,true,Autonomous.AUTO_DRIVE_SPEED));//90 Clockwise
    	addSequential(new AutonomousTurn90DeadRecon(true),AutonomousTurn90DeadRecon.TIME);
    	addSequential(new AutonomousMove(Autonomous.AUTO_DRIVE_SPEED, Autonomous.AUTO_DRIVE_SPEED),Autonomous.TIME_TIL_SAFE_ZONE);
    	
    }
}
