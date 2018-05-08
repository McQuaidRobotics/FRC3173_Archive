package org.usfirst.frc3173.IgKnighters2018.deprecated;

import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousDriveEncoder;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousTurnToHeading;
import org.usfirst.frc3173.IgKnighters2018.utilities.Constants;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousGoToPoint extends CommandGroup {

	public AutonomousGoToPoint(RigidBody pose, Vector2 desiredPos) {
		
		addSequential(new AutonomousTurnToHeading(pose.calculateIdealHeading(Robot.drives.getIMU().getAngle(), desiredPos)));
		int distanceInTicks = RigidBody.distanceInTicks(pose.distanceToTarget(desiredPos), Constants.DRIVES_WHEEL_DIAMETER_INCHES, Constants.LEFT_DRIVES_TICKS_PER_REV);
		addSequential(new AutonomousDriveEncoder(distanceInTicks - Robot.drives.getLeftRightError()[0], distanceInTicks - Robot.drives.getLeftRightError()[1]));
	}
}