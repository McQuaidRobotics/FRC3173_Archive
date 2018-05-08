package org.usfirst.frc3173.IgKnighters2016.commands;
import org.usfirst.frc3173.IgKnighters.utilities.IMU;
import org.usfirst.frc3173.IgKnighters2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AutoStraight extends Command {
	public static IMU RRIMU=null;
	double select[][]=new double[][] {{5,15,25,35,45},{1,.8,.6,0,-1},{1,1,1,1,1}};
	int index=0;
	double iniOrienation;
	double speed;
	
	public AutoStraight(double s){
		requires(Robot.drives);
		speed=s;
	}
	@Override
	
	protected void initialize() {
		// TODO Auto-generated method stub
		RRIMU.calibration();
		iniOrienation=RRIMU.getOrientationX();
	}
	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		System.out.println(RRIMU.getOrientationX());
		System.out.println(iniOrienation);
		boolean isPositive=(iniOrienation-RRIMU.getOrientationX())>0;
		
		for(int x=0;x<5;x++){
			if (Math.abs(iniOrienation-RRIMU.getOrientationX())<=select[0][x]){
				index=x;
				break;
			}
			
		}
		//System.out.println(index);
		if(isPositive){	
			Robot.drives.drivePercentVbus(speed*select[1][index],speed*select[2][index]);
		}else{
			Robot.drives.drivePercentVbus(speed*select[2][index],speed*select[1][index]);
		}
	}

	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected void end() {
		// TODO Auto-generated method stub
		Robot.drives.drivePercentVbus(0, 0);
	}
	
	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		Robot.drives.drivePercentVbus(0, 0);
	}

}
