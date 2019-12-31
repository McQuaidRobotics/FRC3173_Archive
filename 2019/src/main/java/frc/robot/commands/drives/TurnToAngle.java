package frc.robot.commands.drives;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.command.PIDCommand;
//import edu.wpi.first.wpilibj.command.Scheduler;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class TurnToAngle extends Command {
    private final static double ERROR_THRESHOLD = Constants.DRIVES_TICKS_PER_IN * 8;
    private long start_time;
    private static double ticks_to_turn;
    private WPI_TalonSRX left = RobotMap.drivesLeftMaster,
            right = RobotMap.drivesRightMaster;
    
    public TurnToAngle(double angle_deg) {
        requires(Robot.drives);
        double division_factor = 360 / angle_deg;
        ticks_to_turn = Constants.DRIVES_WHEELBASE_FT * 12 * Math.PI / division_factor * Constants.DRIVES_TICKS_PER_IN;
    }

    @Override
    public void initialize() {
        System.out.println("ticksToTurn: " + ticks_to_turn);
        Robot.drives.zeroEncoders();
        start_time = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        left.set(ControlMode.Position, ticks_to_turn);
        right.set(ControlMode.Position, -ticks_to_turn);
    }

    @Override
    protected boolean isFinished() {
        double leftError = Math.abs(left.getSelectedSensorPosition(0) - ticks_to_turn);
        double rightError = Math.abs(right.getSelectedSensorPosition(0) - ticks_to_turn);
        double combinedError = leftError + rightError;
        return (combinedError)/2 < ERROR_THRESHOLD || System.currentTimeMillis() - start_time > 2000;
        //return (System.currentTimeMillis() - start_time) > 1000;
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
