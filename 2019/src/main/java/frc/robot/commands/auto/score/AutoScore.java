package frc.robot.commands.auto.score;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class AutoScore extends Command {

    private enum STATES {
        INITIAL_ACCELERATION,
        FORWARD,
        RELEASE,
        BACKUP,
        FINISHED,
    }

    private static final double COLLISION_JERK_THRESHOLD = 1.0;
    private static final double BACKUP_DISTANCE = -6.0 * Constants.DRIVES_TICKS_PER_IN;
    private static final double FORWARD_MAX_DISTANCE = 24.0 * Constants.DRIVES_TICKS_PER_IN;
    private STATES currentState;
    private double last_accel;
    private long state_start_time;

    public AutoScore() {
        requires(Robot.drives);
        requires(Robot.hatch);
    }

    @Override
    protected void initialize() {
        state_start_time = System.currentTimeMillis(); // So debug println doesn't get huge duration
        goTo(STATES.INITIAL_ACCELERATION);
    }

    @Override
    protected void execute() {
        double accel = RobotMap.imu.getWorldLinearAccelX();
        switch (currentState) {
        case INITIAL_ACCELERATION:
            Robot.drives.goBoth(0.8, 0.8);
            if (stateDuration() > 200) //200 ms
                currentState = STATES.FORWARD;
            break;
        case FORWARD:
            double jerk = accel - last_accel;
            if(Constants.SMARTDASHBOARD_ENABLE) SmartDashboard.putNumber("Auto Score Jerk", jerk);
            if (Math.abs(jerk) > COLLISION_JERK_THRESHOLD) {
                //we got hit!
                goTo(STATES.RELEASE);
            }

            if (Robot.drives.getLeftDriveEncoderValue() > FORWARD_MAX_DISTANCE
                    || Robot.drives.getRightDriveEncoderValue() > FORWARD_MAX_DISTANCE) {
                end();
            }
            break;
        case RELEASE:
            Robot.drives.goBoth(0.0, 0.0);
            Robot.hatch.releaseExtend();
            if (stateDuration() > 100)
                goTo(STATES.BACKUP);
            break;
        case BACKUP:
            Robot.drives.goBoth(-0.6, -0.6);
            if (Robot.drives.getLeftDriveEncoderValue() < BACKUP_DISTANCE
                    || Robot.drives.getRightDriveEncoderValue() < BACKUP_DISTANCE) {
                end();
            }
            break;
        case FINISHED:
            // Nothing should happen
            break;
        }
        last_accel = accel;
    }

    @Override
    protected boolean isFinished() {
        return currentState == STATES.FINISHED;
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void end() {
        goTo(STATES.FINISHED);
        Robot.drives.goBoth(0.0, 0.0);
    }

    private void goTo(STATES nextState) {
        System.out.println("Auto Score: "+nextState.name()+" after "+stateDuration()+" ms");
        Robot.drives.zeroEncoders();
        currentState = nextState;
        state_start_time = System.currentTimeMillis();
    }

    private double stateDuration() {
        return System.currentTimeMillis() - state_start_time;
    }
}