package org.usfirst.frc3173.IgKnighters2018.commandgroups;

import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionClose;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionIntake;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionStop;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AcquisitionGroupIntakeFinish extends CommandGroup {
	public AcquisitionGroupIntakeFinish() {
		addParallel(new AcquisitionClose());
		addSequential(new AcquisitionStop());
	}
}
