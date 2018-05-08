package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Autonomous_Bin_Tote_Not_Let_Go_Over_Step extends CommandGroup {
    
    public  Autonomous_Bin_Tote_Not_Let_Go_Over_Step() {
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
    	
    	addSequential(new AutonomousElevate(Robot.elevation.LOW));
    	addSequential(new AutonomousAcquireBin(false),.5);
    	addSequential(new AutonomousWait(),1);    //let elevation arms get low enough
    	addSequential(new CloseElevationArms());
    	addSequential(new CloseElevationTips());
    	addSequential(new AutonomousWait(),.2);
    	addSequential(new AutonomousElevate(Robot.elevation.TWO_TOTES+30));//addSequential(new AutonomousElevate(Robot.elevation.HOLD_BIN+20));
    	addSequential(new AutonomousWait(),.5);
    	addSequential(new AutonomousWait(),2);
    	addSequential(new AutonomousAcquireTote(false),1); 
    	addSequential(new CloseAcquisitionArms());
    	addSequential(new AutonomousWait(),1.3);   //let elevation arms get high enough
    	addSequential(new AutonomousTurn90DeadRecon(false),AutonomousTurn90DeadRecon.TIME);//counterclockwise
    	addSequential(new AutonomousWait(),.2); //let wheels reach same speed
    	addSequential(new AutonomousMove(Autonomous.AUTO_DRIVE_SPEED, Autonomous.AUTO_DRIVE_SPEED),Autonomous.TIME_TIL_SAFE_ZONE+1);
    	//addSequential(new OpenElevationArms());
    	//addSequential(new AutonomousWait(),.3);
    	//addSequential(new OpenElevationTips());
    	addSequential(new OpenAcquisitionArms());
    	addSequential(new AutonomousMove(-Autonomous.AUTO_DRIVE_SPEED, -Autonomous.AUTO_DRIVE_SPEED),.1);
    }
}
