package org.usfirst.frc3173.IgKnighters2016.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoDriveTest extends CommandGroup {
	public AutoDriveTest(){
		
		addSequential(new AutoStraight(-1),4);
    	
    	
	}
}
