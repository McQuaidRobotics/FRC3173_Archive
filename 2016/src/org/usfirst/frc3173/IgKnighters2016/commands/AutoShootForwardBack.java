package org.usfirst.frc3173.IgKnighters2016.commands;

import org.usfirst.frc3173.IgKnighters2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AutoShootForwardBack extends Command {
	boolean finish=false;
	
	public AutoShootForwardBack(){
		requires(Robot.drives);
	}
	protected void initialize() {
		// TODO Auto-generated method stub
		Robot.vision.getValues();
		double deltaY=Robot.vision.cogY-300;	
//		System.out.println("vision cog x" + Robot.vision.cogX);
//		System.out.println("vision cog y" + Robot.vision.cogY);
//		System.out.println("vision image width" + Robot.vision.IMAGE_WIDTH);
//		System.out.println("vision delta" + deltaY);
		//double speed=deltaY/640;
		double speed=0.8;//working at .55
		int tolerance=40;
		if(deltaY>tolerance){
			Robot.drives.drivePercentVbus(-speed, -speed);
		}
		else if(deltaY<-tolerance){
			Robot.drives.drivePercentVbus(speed, speed);
		}
		else{
			finish=true;
		}
		System.out.println("Auto shoot forward back");
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		Robot.vision.getValues();
		double deltaY=Robot.vision.cogY-300;	
//		System.out.println("vision cog x" + Robot.vision.cogX);
//		System.out.println("vision cog y" + Robot.vision.cogY);
//		System.out.println("vision image width" + Robot.vision.IMAGE_WIDTH);
//		System.out.println("vision delta" + deltaY);
		//double speed=deltaY/640;
		double speed=0.55;
		int tolerance=40;
		if(deltaY>tolerance){
			Robot.drives.drivePercentVbus(-speed, -speed);
		}
		else if(deltaY<-tolerance){
			Robot.drives.drivePercentVbus(speed, speed);
		}
		else{
			finish=true;
		}

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return finish;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		Robot.drives.drivePercentVbus(0, 0);
		Robot.shooter.spinVoltage(-1);
	}

}
