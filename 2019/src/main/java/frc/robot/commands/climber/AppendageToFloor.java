package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.util.AppendageSubsystem;

public class AppendageToFloor extends Command {
    private AppendageSubsystem appendage;
    private double rpm;
    public AppendageToFloor(AppendageSubsystem appendage, double rpm) {
        this.appendage = appendage;
        this.rpm = rpm;
        requires(appendage);
    }

    @Override
    protected void initialize() {
        if(rpm != Constants.REV_NEO_MAX_RPMS) {
            rpm = Constants.REV_NEO_MAX_RPMS;
            // comment reference: $CONSTANTS_VAR_CHECK
        }
        appendage.moveDown(rpm);
    }

    @Override
    protected void execute() {}

    @Override
    protected boolean isFinished() {
        return appendage.onSurface();
    }

    @Override
    protected void end() {
        appendage.stopMoving();
    }
}