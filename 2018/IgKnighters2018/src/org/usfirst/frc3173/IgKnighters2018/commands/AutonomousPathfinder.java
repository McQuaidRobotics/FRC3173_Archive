package org.usfirst.frc3173.IgKnighters2018.commands;

import java.io.File;
import java.io.InputStream;

import org.usfirst.frc3173.IgKnighters2018.Robot;
import org.usfirst.frc3173.IgKnighters2018.RobotMap;
import org.usfirst.frc3173.IgKnighters2018.utilities.Constants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class AutonomousPathfinder extends Command {

	private boolean shouldTerminate;
	private EncoderFollower left, right;
	private WPI_TalonSRX leftMotor = RobotMap.drivesLeftMaster, 
			rightMotor = rightMotor = RobotMap.drivesRightMaster;
	private static final AHRS imu = Robot.drives.getIMU();
	private String gameSpecificData;
	
    public AutonomousPathfinder(String gameSpecificData) {
    	requires(Robot.drives);
    	
    	this.gameSpecificData = gameSpecificData;
    	File left_traj_file, right_traj_file;
    	
    	if (imu == null) {
    		System.out.println("[CRASH] IMU NOT DETECTED.");
    		return;
    	}
    	
    	if (gameSpecificData.substring(0, 1).toLowerCase().equals("l")) {
    		System.out.println("[PF] Running auto center left switch...");
    		try {
        		left_traj_file = new File("//home//lvuser//pathfinder_stuff//center_left_left_detailed.csv");
            	right_traj_file = new File("//home//lvuser//pathfinder_stuff//center_left_right_detailed.csv");
    		} catch (Exception e) {
    			System.out.println("[CRASH] AUTONOMOUS CANT DETECT PATHFINDER FILES IN DIRECTORY");
    			e.printStackTrace();
    			return;
    		}
    	} else if (gameSpecificData.substring(0, 1).toLowerCase().equals("r")) {
    		System.out.println("[PF] Running auto center right switch...");
    		try {
        		left_traj_file = new File("//home//lvuser//pathfinder_stuff//center_right_left_detailed.csv");
        		right_traj_file = new File("//home//lvuser//pathfinder_stuff//center_right_right_detailed.csv");
    		} catch (Exception e) {
    			System.out.println("[CRASH] AUTONOMOUS CANT DETECT PATHFINDER FILES IN DIRECTORY");
    			e.printStackTrace();
    			return;
    		}
    	} else {
    		System.out.println("[CRASH] AUTONOMOUS NO GAMEDATA DETECTED!!");
    		return;
    	}
    	
    	//load trajectories from files from earlier
    	Trajectory left_traj = Pathfinder.readFromCSV(left_traj_file);
    	Trajectory right_traj = Pathfinder.readFromCSV(right_traj_file);
    	
    	left = new EncoderFollower(left_traj);
    	right = new EncoderFollower(right_traj);
    	
    	left.configureEncoder(leftMotor.getSelectedSensorPosition(0), Constants.LEFT_DRIVES_TICKS_PER_REV, 4);
    	right.configureEncoder(rightMotor.getSelectedSensorPosition(0), Constants.RIGHT_DRIVES_TICKS_PER_REV, 4);
    	
    	left.configurePIDVA(1.0, 0.0, 0.0, 1 / 1.7, 0);
    	right.configurePIDVA(10.0, 0.0, 0.0, 1 / 1.7, 0);
    	//7000 right
    	//200 left
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	double l = left.calculate(leftMotor.getSelectedSensorPosition(0));
    	double r = right.calculate(rightMotor.getSelectedSensorPosition(0));
    	
    	double gyro_heading = imu.getAngle();
    	double desired_heading = Pathfinder.r2d(left.getHeading());
    	double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
    	double turn = 0.8 * (-1.0 / 80.0) * angleDifference;
    	
    	leftMotor.set(l);
    	rightMotor.set(r);
    }

    @Override
    protected void end() {
    	Robot.drives.stop();
    }
    
    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }
    
    @Override
    protected void interrupted() {
    	end();
    }
}
