package org.usfirst.frc3173.IgKnighters2018.commandgroups;

import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousDriveEncoder;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousTurnToHeading;
import org.usfirst.frc3173.IgKnighters2018.deprecated.RigidBody;
import org.usfirst.frc3173.IgKnighters2018.utilities.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousFirstStation extends CommandGroup {
	private String gameSpecificData;
	public AutonomousFirstStation(String gameSpecificData) {
		this.gameSpecificData = gameSpecificData;
		int distance = (int)RigidBody.distanceInTicks(168f, Constants.DRIVES_WHEEL_DIAMETER_INCHES, Constants.LEFT_DRIVES_TICKS_PER_REV);
		addSequential(new AutonomousDriveEncoder(distance, distance));
		if (gameSpecificData.toLowerCase().charAt(0) == 'l') {
			addSequential(new AutonomousTurnToHeading(90f));
			//addSequential(new AutonomousSwitch());
		} else if (gameSpecificData.toLowerCase().charAt(1) == 'l') {
			/*int distanceTwo = (int)RigidBody.distanceInTicks(156f, Constants.DRIVES_WHEEL_DIAMETER_INCHES, Constants.DRIVES_TICKS_PER_REV);
			addSequential(new AutonomousDriveEncoder(distanceTwo, distanceTwo));
			addSequential(new AutonomousTurnToHeading(90f));
			addSequential(new AutonomousScale());*/
		} else {
			System.out.println("[Auto] There is nothing... :(( MY KINGDOM IS IN RUIN!!");
		}
	}
}
