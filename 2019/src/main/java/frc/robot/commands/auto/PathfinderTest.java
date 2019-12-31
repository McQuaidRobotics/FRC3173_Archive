/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.Constants;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

//import java.io.File;

public class PathfinderTest extends Command {
  private EncoderFollower left, right;

  public PathfinderTest() {
    requires(Robot.drives);
  }

  @Override
  protected void initialize() {
    Robot.drives.zeroEncoders();
    RobotMap.imu.zeroYaw();

    boolean loadFromFile = false;  // change this when you want to load from file or not
    Trajectory trajectory;
    if(loadFromFile) {
      // Note:  (Chuck) Commented out and re-arranged to elimate pre-compile error
      /*
      String filename = "/home/lvuser/deploy/First.pf1.csv";
      System.out.println("loading from file");
      trajectory = Pathfinder.readFromCSV(new File(filename));
      */
      Waypoint[] points = new Waypoint[] {
        new Waypoint(0, 0, 0),
        new Waypoint(10, -5, Pathfinder.d2r(-90)),
      };
      Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, 0.02, Constants.DRIVES_MAX_VEL, Constants.DRIVES_MAX_ACC, 60.0);
      trajectory = Pathfinder.generate(points, config);
    } else {
      System.out.println("loading from internal waypoints");
      // DISABLE AND RE-ENABLE EVERY TIME YOU RUN THE PATH FOR EFFICACY
      // MAKE SURE THAT WHEN YOU TEST THIS, YOU'RE MEASURING BASED OFF OF
      // THE CENTER OF THE BOT AND NOT THE FRONT WHEELS, AS MATT SAID
      Waypoint[] points = new Waypoint[] {
        new Waypoint(0, 0, 0),
        //new Waypoint(10, 0, 0),
        new Waypoint(10, -5, Pathfinder.d2r(-90)),
        //new Waypoint(5, 0, Pathfinder.d2r(90)),
        //new Waypoint(5, 5, Pathfinder.d2r(90)),
      };
      double time = getTime();
      // distance delta / interpolation delta = low interpolation
      Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, 0.02, Constants.DRIVES_MAX_VEL, Constants.DRIVES_MAX_ACC, 60.0);
      System.out.println("debug: config time = " + (getTime() - time));
      trajectory = Pathfinder.generate(points, config);
      System.out.println("debug: generation time = " + (getTime() - time));
      System.out.println("trajectory: " + trajectory.toString());
      System.out.println("trajectory generated");
    }

    TankModifier modifier = new TankModifier(trajectory).modify(Constants.DRIVES_WHEELBASE_FT);
    
    //yes this is backwards, but apparently it is correct!
    left = new EncoderFollower(modifier.getRightTrajectory());
    right = new EncoderFollower(modifier.getLeftTrajectory()); 

    //configure encoders
    left.configureEncoder(Robot.drives.getLeftDriveEncoderValue(), Constants.DRIVES_TICKS_PER_REV, Constants.DRIVES_WHEEL_DIAM_IN / 12.0);
    right.configureEncoder(Robot.drives.getRightDriveEncoderValue(), Constants.DRIVES_TICKS_PER_REV, Constants.DRIVES_WHEEL_DIAM_IN / 12.0);
    left.configurePIDVA(Constants.DRIVES_P, Constants.DRIVES_I, Constants.DRIVES_D, 1 / Constants.DRIVES_MAX_VEL, 0);
    right.configurePIDVA(Constants.DRIVES_P, Constants.DRIVES_I, Constants.DRIVES_D, 1 / Constants.DRIVES_MAX_VEL, 0);
  }

  @Override
  protected void execute() {
    double leftOutput = left.calculate(Robot.drives.getLeftDriveEncoderValue());
    double rightOutput = right.calculate(Robot.drives.getRightDriveEncoderValue());

    double gyroHeading = RobotMap.imu.getYaw();
    double desiredHeading = Pathfinder.r2d(left.getHeading());
    double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
    double turn = -0.01 * angleDifference;

    Robot.drives.goBoth(leftOutput - turn, rightOutput + turn);
  }

  private double getTime() {
    return (double) System.currentTimeMillis();
  }

  @Override
  protected boolean isFinished() {
    return left.isFinished() && right.isFinished();
  }

  @Override
  protected void end() { }

  @Override
  protected void interrupted() { end(); }
}
