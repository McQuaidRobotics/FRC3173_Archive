package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.Robot;
//import frc.robot.commands.auto.climb.AutoClimb;

public class PantsManualDrive extends Command {
    public PantsManualDrive() {
        requires(Robot.pants);
    }

    @Override
    public void execute() {
        double mult = Constants.REV_NEO_MAX_RPMS;
        double setPoint = OI.rightJoystick.getY() * mult;
        double deadband = 0.5 * mult;
        if(Math.abs(setPoint) <= deadband) {
            setPoint = 0;
        }
        Robot.pants.moveAt(setPoint);
    }

    @Override
    protected void end() {
        Robot.pants.stopMoving();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
