package org.usfirst.frc3173.IgKnighters2016.commands;

import org.usfirst.frc3173.IgKnighters2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AutoShootLeftRight extends Command {
	boolean finish=false;
	
	
	public AutoShootLeftRight(){
		requires(Robot.drives);
	}
	
	protected void initialize() {
		Robot.vision.getValues();
		double deltaX=Robot.vision.cogX-320;	
//		System.out.println("vision cog x" + Robot.vision.cogX);
//		System.out.println("vision cog y" + Robot.vision.cogY);
//		System.out.println("vision image width" + Robot.vision.IMAGE_WIDTH);
//		System.out.println("vision delta" + deltaX);
		//double speed=deltaX/640;
		double speed=0.55;
		int tolerance=35;
			if(deltaX>tolerance){
				Robot.drives.drivePercentVbus(-speed, speed);
			}
			else if(deltaX<-tolerance){
				Robot.drives.drivePercentVbus(speed, -speed);
			}
			else{
				finish=true;
			}
		System.out.println("Auto shoot left right");

	}

	@Override
	protected void execute() {
		Robot.vision.getValues();
		double deltaX=Robot.vision.cogX-320;	
//		System.out.println("vision cog x" + Robot.vision.cogX);
//		System.out.println("vision cog y" + Robot.vision.cogY);
//		System.out.println("vision image width" + Robot.vision.IMAGE_WIDTH);
//		System.out.println("vision delta" + deltaX);
		//double speed=deltaX/640;
		double speed=0.55;
		int tolerance=35;
			if(deltaX>tolerance){
				Robot.drives.drivePercentVbus(-speed, speed);
			}
			else if(deltaX<-tolerance){
				Robot.drives.drivePercentVbus(speed, -speed);
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
