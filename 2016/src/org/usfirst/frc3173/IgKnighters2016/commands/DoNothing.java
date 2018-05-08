package org.usfirst.frc3173.IgKnighters2016.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
public class DoNothing extends CommandGroup {
	public DoNothing()
	{
		this.addSequential(new doNothin(), 1);
	}
	
	
	
	
}
