package org.usfirst.frc3173.IgKnighters2018.commandgroups;

import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionPivotChangePosition;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionReverse;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionStop;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousDrivePercent;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousStall;
import org.usfirst.frc3173.IgKnighters2018.commands.ElevatorChangePosition;
import org.usfirst.frc3173.IgKnighters2018.subsystems.Acquisition;
import org.usfirst.frc3173.IgKnighters2018.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousScale extends CommandGroup {
	public AutonomousScale() {
		addParallel(new ElevatorChangePosition(Elevator.ElevatorStates.SCALE));
		addParallel(new AcquisitionPivotChangePosition(Acquisition.AcquisitionPivotStates.DEFAULT));
		addSequential(new AutonomousDrivePercent(-0.6f, 0.6f), 4);
		addSequential(new AutonomousDrivePercent(0.3f, 0.3f), 4);
		addSequential(new AcquisitionReverse(), 2);
		addSequential(new AcquisitionStop());
	}
}