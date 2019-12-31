package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.Robot;

public class UrgersManualDrive extends Command {
    public UrgersManualDrive() {
        requires(Robot.urgers);
    }

    @Override
    public void execute() {
        double mult = 1500;
        //double setPoint = OI.debugGamepad.getRawAxis(4/*Right X*/)*1500;
        double setPoint = OI.leftJoystick.getX() * 1500;
        double deadband = 0.5 * mult;
        if(Math.abs(setPoint) <= deadband) {
            setPoint = 0;
        }
        Robot.urgers.go(setPoint);
    }

    @Override
    protected void end() {
        Robot.urgers.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
