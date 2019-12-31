package frc.robot.commands.drives;

//import edu.wpi.first.wpilibj.PIDController;
//import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDCommand;
//import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class TurnToAngleIMU extends PIDCommand {
    private double target_angle, last_angle, cumulative_rotations;
    private double start_time;
    private boolean done;
    public TurnToAngleIMU(double angle) {
        super(Constants.IMU_P, Constants.IMU_I, Constants.IMU_D);
        requires(Robot.drives);
        getPIDController().setOutputRange(-0.5, 0.5);
        getPIDController().setAbsoluteTolerance(1);
        target_angle = angle;
    }

    @Override
    public void initialize() {
        System.out.println("turntoangle started! " + (System.currentTimeMillis()));
        if (!RobotMap.imu.isConnected())
            done = true;
        else {
            done = false;
            last_angle = 0;
        }
        RobotMap.imu.zeroYaw();
        cumulative_rotations = 0;
        super.setSetpoint(target_angle);
        start_time = System.currentTimeMillis();
        getPIDController().setPID(Constants.IMU_P, Constants.IMU_I, Constants.IMU_D);
    }

    @Override
    public void execute() {
        double current_angle = getAngle();
        SmartDashboard.putNumber("cumulative_angle:", returnPIDInput());
        if (last_angle > 270 && current_angle < 90) {//just turned clockwise
            cumulative_rotations += 1;
            System.out.println("WOW, I JUST ADDED ONE!!! BECAUSE:" + last_angle + " TURNED INTO " + current_angle + " NOW IT IS : " + cumulative_rotations);
        } else if (last_angle < 90 && current_angle > 270) {
            cumulative_rotations -= 1;
            System.out.println("WOW, I JUST SUBTRACTED ONE!!! BECAUSE:" + last_angle + " TURNED INTO " + current_angle + " NOW IT IS : " + cumulative_rotations);
        }
        last_angle = current_angle;
    }

    @Override
    protected boolean isFinished() {  
        //Math.abs(RobotMap.imu.getYaw() - target_angle) < 5 ||
        if (done) System.err.println("navx disconnected!! killing turn command...");
        return done || System.currentTimeMillis() - start_time > 5000;
    }

    @Override
    protected double returnPIDInput() {
        return (cumulative_rotations * 360) + getAngle();
    }

    @Override
    protected void usePIDOutput(double output) {
        SmartDashboard.putNumber("angleOutput", output);
        RobotMap.drivesLeftMaster.set(output);
        RobotMap.drivesRightMaster.set(-output);
    }

    public double getAngle() {
        if (RobotMap.imu.isConnected())
            return RobotMap.imu.getYaw() + 180; // navx is - 180 to 180, add 180 to become 0 to 360
        else {
            done = true;
            return 0;
        }
    }
}