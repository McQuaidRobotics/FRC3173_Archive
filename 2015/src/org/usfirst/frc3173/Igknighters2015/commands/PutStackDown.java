package org.usfirst.frc3173.Igknighters2015.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PutStackDown extends CommandGroup {
    
    public  PutStackDown() {
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
    	addSequential(new ChangeToPosition());
    	addSequential(new OpenAcquisitionArms());
    	addSequential(new ElevateToBottom());
    	addSequential(new OpenElevationArms());
    	addSequential(new AutonomousWait(),.2);
    	addSequential(new DriveBackward(),.4);
    	addSequential(new ChangeToVBUS());
    }
}
