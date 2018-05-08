package org.usfirst.frc3173.IgKnighters2018.commands;

import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.RobotMap;
import org.usfirst.frc3173.IgKnighters2018.utilities.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class DrivesTurnAround extends Command {

	private static final int turn_ticks = 166213 * 2;
	private long start_time;
	private WPI_TalonSRX left = RobotMap.drivesLeftMaster,
			right = RobotMap.drivesRightMaster;
	
	public DrivesTurnAround() {
		requires(Robot.drives);
		left.setSelectedSensorPosition(0, 0, 0);
		right.setSelectedSensorPosition(0, 0, 0);
	}
	
	@Override
	protected void initialize() {
		left.setSelectedSensorPosition(0, 0, 0);
		right.setSelectedSensorPosition(0, 0, 0);
		start_time = System.currentTimeMillis();
	}
	
	@Override
	protected void execute() {
		left.set(ControlMode.Position, turn_ticks);
		right.set(ControlMode.Position, -turn_ticks);
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		//return isComplete;
		return (System.currentTimeMillis() - start_time > 1000);
	}
	
	@Override
	protected void end() {
		left.set(ControlMode.PercentOutput, 0);
		right.set(ControlMode.PercentOutput, 0);
	}
	@Override
	protected void interrupted() {
		end();
	}

}
