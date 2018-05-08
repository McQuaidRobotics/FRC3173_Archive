package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.deprecated.RigidBody;
import org.usfirst.frc3173.IgKnighters2018.deprecated.Vector2;

import edu.wpi.first.wpilibj.command.Command;

public class AutonomousUpdatePose extends Command {

	private boolean isComplete;
	public AutonomousUpdatePose(RigidBody originalPose, Vector2 toAdd) {
		isComplete = false;
		System.out.println("[COMMAND] AutonomousUpdatePose: Adding vector " + toAdd.toString() + " to rb " + originalPose.toString());
		originalPose.add(toAdd);
		isComplete = true;
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return isComplete;
	}

}
