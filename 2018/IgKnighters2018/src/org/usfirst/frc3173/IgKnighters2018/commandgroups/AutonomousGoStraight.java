package org.usfirst.frc3173.IgKnighters2018.commandgroups;

import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionIntake;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionPivotChangePosition;
import org.usfirst.frc3173.IgKnighters2018.commands.AcquisitionStop;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousDriveEncoder;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousDrivePercent;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousStall;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousTurnToHeading;
import org.usfirst.frc3173.IgKnighters2018.commands.DrivesStop;
import org.usfirst.frc3173.IgKnighters2018.deprecated.RigidBody;
import org.usfirst.frc3173.IgKnighters2018.subsystems.Acquisition;
import org.usfirst.frc3173.IgKnighters2018.utilities.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousGoStraight extends CommandGroup {
	private String gameSpecificData;
	public AutonomousGoStraight(String gameSpecificData) {
		/*this.gameSpecificData = gameSpecificData;
		int distance = (int)RigidBody.distanceInTicks(168f, Constants.DRIVES_WHEEL_DIAMETER_INCHES, Constants.DRIVES_TICKS_PER_REV);
		addSequential(new AcquisitionPivotChangePosition(Acquisition.AcquisitionPivotStates.DEFAULT));
		SmartDashboard.putNumber("desired drives encoder", distance);
		addSequential(new AutonomousDriveEncoder(-distance, -distance));
		if (gameSpecificData.toLowerCase().charAt(0) == 'r') {
			addSequential(new AutonomousTurnToHeading(-90f));
			addSequential(new AutonomousSwitch());
		} else if (gameSpecificData.toLowerCase().charAt(1) == 'r') {
			int distanceTwo = (int)RigidBody.distanceInTicks(156f, Constants.DRIVES_WHEEL_DIAMETER_INCHES, Constants.DRIVES_TICKS_PER_REV);
			addSequential(new AutonomousDriveEncoder(-distanceTwo, -distanceTwo));
			addSequential(new AutonomousTurnToHeading(-90f));
			addSequential(new AutonomousScale());
		} else {
			System.out.println("[Auto] There is nothing... :(( MY KINGDOM IS IN RUIN!!");
		}*/
		addSequential(new AutonomousStall(), 9);
		addSequential(new AutonomousDrivePercent(0.62f, 0.62f), 4);
	}
}
