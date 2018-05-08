package org.usfirst.frc3173.IgKnighters2016.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ClimberCurrent extends CommandGroup {
	public ClimberCurrent()
	{
		this.addSequential(new DoNothing(), 2);
		this.addSequential(new ClimberTapeSpringIn(false));
	
	}

}
