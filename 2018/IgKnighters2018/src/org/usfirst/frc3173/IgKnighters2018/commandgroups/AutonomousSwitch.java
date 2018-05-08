package org.usfirst.frc3173.IgKnighters2018.commandgroups;

import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.RobotMap;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionIntake;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionOpen;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionPivotChangePosition;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionPivotPercent;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionReverse;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionStop;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionToggleOpenness;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousDriveEncoder;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousDrivePercent;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousStall;
import org.usfirst.frc3173.IgKnighters2018.commands.ElevatorChangePosition;
import org.usfirst.frc3173.IgKnighters2018.subsystems.Acquisition;
import org.usfirst.frc3173.IgKnighters2018.subsystems.Elevator;
import org.usfirst.frc3173.IgKnighters2018.utilities.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousSwitch extends CommandGroup {
	public AutonomousSwitch(String gameSpecificData, String ds_position) {
		if (!ds_position.equals(Constants.DRIVERSTATION_TWO_STRING)) {
			int ticks_for_switch = (int)((14 - 1.5) * 12 * Constants.DRIVES_TICKS_PER_IN);
			addSequential(new AutonomousDriveEncoder(ticks_for_switch, ticks_for_switch), 3);
			if (ds_position.equals(Constants.DRIVERSTATION_ONE_STRING) && gameSpecificData.substring(0, 1).toLowerCase().equals("l")) {
				System.out.println("I'm in!!");
				addSequential(new ElevatorChangePosition(Elevator.ElevatorStates.SWITCH));
				addSequential(new AutonomousStall(), 3);
				addSequential(new AutonomousDriveEncoder(166213, -166213), 1);
				addSequential(new AutonomousDrivePercent(0.5f, 0.5f), 2);
				addSequential(new AcquisitionPivotPercent(-1f), 1);
				addSequential(new AcquisitionReverse());
				addSequential(new AutonomousStall(), 2);
				addSequential(new AcquisitionStop());
			} else if (ds_position.equals(Constants.DRIVERSTATION_THREE_STRING) && gameSpecificData.substring(0, 1).toLowerCase().equals("r")) {
				addSequential(new ElevatorChangePosition(Elevator.ElevatorStates.SWITCH));
				addSequential(new AutonomousStall(), 3);
				addSequential(new AutonomousDriveEncoder(-166213, 166213), 1);
				addSequential(new AutonomousDrivePercent(0.5f, 0.5f), 2);
				addSequential(new AcquisitionPivotPercent(-1f), 1);
				addSequential(new AcquisitionReverse());
				addSequential(new AutonomousStall(), 2);
				addSequential(new AcquisitionStop());
			}
		} else {
			int first_move_ticks = (int)((5 - 1.5) * 12 * Constants.DRIVES_TICKS_PER_IN);
			addSequential(new AutonomousDriveEncoder(first_move_ticks, first_move_ticks), 1.5);
			if (gameSpecificData.substring(0, 1).toLowerCase().equals("l")) {
				addSequential(new AutonomousDriveEncoder(-166213, 166213), 1);
				int second_move_ticks = (int)Math.round((45 + 36.625) * Constants.DRIVES_TICKS_PER_IN);
				addSequential(new AutonomousDriveEncoder(second_move_ticks, second_move_ticks), 2);
				addSequential(new AutonomousDriveEncoder(166213, -166213), 1);
			} else if (gameSpecificData.substring(0, 1).toLowerCase().equals("r")) {
				addSequential(new AutonomousDriveEncoder(166213, -166213), 1);
				int second_move_ticks = (int)Math.round((36.625) * Constants.DRIVES_TICKS_PER_IN);
				addSequential(new AutonomousDriveEncoder(second_move_ticks, second_move_ticks), 2);
				addSequential(new AutonomousDriveEncoder(-166213, 166213), 1);
			}
			//int third_move_ticks = (int)Math.round(2.5 * 12 * Constants.DRIVES_TICKS_PER_IN);
			addSequential(new ElevatorChangePosition(Elevator.ElevatorStates.SWITCH));
			addParallel(new AcquisitionPivotPercent(-1f), 1);
			addParallel(new AcquisitionIntake());
			addSequential(new AutonomousStall(), 2);
			addParallel(new AcquisitionStop());
			addSequential(new AutonomousDrivePercent(0.5f, 0.5f), 3);
			addSequential(new AcquisitionReverse());
			addSequential(new AutonomousStall(), 0.5);
			addSequential(new AcquisitionToggleOpenness());
			addSequential(new AcquisitionStop());
		}
	}
}
