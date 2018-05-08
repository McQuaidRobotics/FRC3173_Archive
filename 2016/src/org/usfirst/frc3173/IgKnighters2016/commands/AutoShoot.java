package org.usfirst.frc3173.IgKnighters2016.commands;

import org.usfirst.frc3173.IgKnighters2016.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoShoot extends CommandGroup {
	public AutoShoot(){
		//System.out.println("auto shoot begin");
		addSequential(new AutoShootBegin(),0.75);
		//System.out.println("auto shoot spin up");
		addSequential(new AutoShootSpinUp(),2.65);
		//System.out.println("auto shoot left right");
		addSequential(new AutoShootLeftRight());
		//System.out.println("auto shoot forward back");
		addSequential(new AutoShootForwardBack());
		//System.out.println("auto shoot left right");
		addSequential(new AutoShootLeftRight());
		//System.out.println("auto shoot belt");
		addSequential(new AutoShootBelt());
		//System.out.println("auto shoot finish");
		addSequential(new doNothin(),.5);
		addSequential(new AutoShootFinish());
		//System.out.println("auto shoot finished");
		this.cancel();
	}
}
