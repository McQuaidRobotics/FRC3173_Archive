package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;

public class TurnUrgersByRevs extends Command {  // phase 2 - roll the urgers until we're on the platform
    private double targetPos;
    private double rpm;
    public TurnUrgersByRevs(double rpm, double targetPos) {
        requires(Robot.urgers);
        this.rpm = rpm;
        this.targetPos = targetPos;
        assert (rpm > 0 && targetPos > 0) || (rpm < 0 && targetPos < 0) : "RPM and target pos must be same sign";
    }

    @Override
    protected void initialize() {
        if(targetPos != Constants.URGE_REVOLUTIONS * (Constants.NEO_TICKS_PER_REVOLUTION * Constants.URGERS_GEAR_REDUCTION)) {
            targetPos = Constants.URGE_REVOLUTIONS * (Constants.NEO_TICKS_PER_REVOLUTION * Constants.URGERS_GEAR_REDUCTION);
            // reference: $CONSTANTS_VAR_CHECK
        }
        if(rpm != Constants.URGE_RPMS) {
            targetPos = Constants.URGE_RPMS;
            // reference: $CONSTANTS_VAR_CHECK
        }
        Robot.urgers.zeroEncoder();
        Robot.urgers.go(this.rpm);
    }

    @Override
    protected boolean isFinished() {
        double pos = Robot.urgers.getTicks();
        if (rpm >= 0) {
            return pos > targetPos;
        } else {
            return pos < targetPos;
        }
    }

    @Override
    protected void end() {
        Robot.urgers.stop();
    }
}