package frc.robot.commands.drives;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class TurnAround extends Command {
    private final static double ERROR_THRESHOLD = Constants.DRIVES_TICKS_PER_IN * 2;
    private final static double ticks_to_turn = (Constants.DRIVES_WHEELBASE_FT * 12 * Math.PI / 2) * Constants.DRIVES_TICKS_PER_IN;
    //private long start_time;
    private WPI_TalonSRX left = RobotMap.drivesLeftMaster,
            right = RobotMap.drivesRightMaster;
    
    public TurnAround() {
        requires(Robot.drives);
    }

    @Override
    public void initialize() {
        System.out.println("ticksToTurn: " + ticks_to_turn);
        Robot.drives.zeroEncoders();
        //start_time = System.currentTimeMillis();
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
        return combinedError < ERROR_THRESHOLD;
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