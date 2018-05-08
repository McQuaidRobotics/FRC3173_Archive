package org.usfirst.frc3173.IgKnighters2018.commandgroups;

import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousStall;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousTurnToHeading;
import org.usfirst.frc3173.IgKnighters2018.commands.AutonomousUpdatePose;
import org.usfirst.frc3173.IgKnighters2018.deprecated.AutonomousGoToPoint;
import org.usfirst.frc3173.IgKnighters2018.deprecated.PathBuilder;
import org.usfirst.frc3173.IgKnighters2018.deprecated.RigidBody;
import org.usfirst.frc3173.IgKnighters2018.deprecated.Vector2;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousPath extends CommandGroup {
	private RigidBody robotPose;
	public AutonomousPath(Vector2[] points, float exitAngle, PathBuilder.AutonomousAction autoAction) {
		robotPose = new RigidBody(points[0]);
		for (int i = 1; i < points.length-1; i++) {
			addSequential(new AutonomousGoToPoint(robotPose, points[i]));
			addSequential(new AutonomousUpdatePose(robotPose, points[i]));
		}
		addSequential(new AutonomousTurnToHeading(exitAngle));
		if (autoAction == PathBuilder.AutonomousAction.NOTHING) {
			addSequential(new AutonomousStall());
		} else if (autoAction == PathBuilder.AutonomousAction.SCALE) {
			addSequential(new AutonomousScale());
		} else if (autoAction == PathBuilder.AutonomousAction.SWITCH) {
			//addSequential(new AutonomousSwitch());
		}
	}
}
