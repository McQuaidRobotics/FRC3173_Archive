package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Autonomous_Three_Totes_No_Second_Bin extends CommandGroup {
    
    public  Autonomous_Three_Totes_No_Second_Bin() {
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
    	addSequential(new AutonomousAcquireBin(false),1);
    	addSequential(new CloseElevationArms());
    	addSequential(new CloseElevationTips());
    	addSequential(new AutonomousWait(),.1);
    	addSequential(new AutonomousElevate(Robot.elevation.HOLD_BIN));
    	addSequential(new AutonomousAcquireTote(false),1);
    	addSequential(new AutonomousTurn90DeadRecon(false)); //counterclockwise
    	addSequential(new CloseBinHolder());
    	addSequential(new AutonomousWait(),.1);
    	addSequential(new OpenElevationArms());
    	addSequential(new OpenElevationTips());
    	addSequential(new AutonomousWait(),.1);
    	addSequential(new AutonomousElevate(Robot.elevation.TOTE));
    	addSequential(new AutonomousTurn90DeadRecon(false));//counterclockwise
    	addSequential(new StackOneTote());
    	addSequential(new AutonomousAcquireTote(false),3);
    	addSequential(new StackOneTote());
    	addSequential(new AutonomousElevate(Robot.elevation.TOTE+10));
    	addSequential(new AutonomousAcquireTote(false));
    	addSequential(new StackOneTote());
    	addSequential(new AutonomousTurn90DeadRecon(true)); //clockwise
    	addSequential(new AutonomousDrive(Autonomous.AUTO_DRIVE_SPEED,Autonomous.AUTO_DRIVE_SPEED),1);
    	addSequential(new PutStackDown());
    }
}
