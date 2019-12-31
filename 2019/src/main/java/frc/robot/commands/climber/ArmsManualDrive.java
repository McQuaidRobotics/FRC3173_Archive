package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.Robot;
//import frc.robot.commands.auto.climb.AutoClimb;

public class ArmsManualDrive extends Command {
    public ArmsManualDrive() {
        requires(Robot.arms);
    }

    @Override
    public void execute() {
        double mult = Constants.REV_NEO_MAX_RPMS;
        double setPoint = OI.leftJoystick.getY() * mult;
        double deadband = 0.5 * mult;
        if(Math.abs(setPoint) <= deadband) {
            setPoint = 0;
        }
        Robot.arms.moveAt(setPoint);
    }

    @Override
    protected void end() {
        Robot.arms.stopMoving();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
