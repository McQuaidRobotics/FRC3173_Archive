package org.usfirst.frc3173.Igknighters2015.commands;

import org.usfirst.frc3173.Igknighters2015.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Autonomous_Nothing extends CommandGroup {
    
    public  Autonomous_Nothing() {
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
    	
    	
    	/*addSequential(new CalibrateSensors(),Autonomous.sensorTime);
    	addSequential(new AutonomousElevate(400));
    	addSequential(new AutonomousGoForward(), Autonomous.TIME_TIL_SAFE_ZONE);
    	*/
    	//addSequential(new AutonomousMove(Autonomous.AUTO_DRIVE_SPEED, Autonomous.AUTO_DRIVE_SPEED),Autonomous.TIME_TIL_SAFE_ZONE);
    	
    	addSequential(new AutonomousTurnDegreesBetter(-45));
    	
    	/*addSequential(new AutonomousTurnDegreesBetter(45));
    	addSequential(new AutonomousWait(),3);
    	addSequential(new AutonomousTurnDegreesBetter(90));
    	addSequential(new AutonomousWait(),3);
    	
    	addSequential(new AutonomousWait(),3);
    	addSequential(new AutonomousTurnDegreesBetter(20));
    	
    	*/
    }
}
