package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.TimedCommand;
import frc.robot.Constants;
import frc.robot.util.AppendageSubsystem;

public class AppendageToTop extends TimedCommand {
    private AppendageSubsystem appendage;
    private double rpm;
    public AppendageToTop(AppendageSubsystem appendage, double rpm) {
        super(3.5);  // argument is timeout
        this.appendage = appendage;
        this.rpm = rpm;
        requires(appendage);
    }

    @Override
    protected void initialize() {
        if(rpm != Constants.REV_NEO_MAX_RPMS) {
            rpm = Constants.REV_NEO_MAX_RPMS;  // if the rpm we got is different, there's been a user change
            // we do this because when we create the command,
            // the rpm does not change. the only way to fix this
            // would be to pass in a pointer to the
            // Constants.REV_NEO_MAX_RPMS variable, but in java
            // i don't think pointers exist easily, so we do this check.
            // to my knowledge there really isn't any other better
            // way to do this, due to how commands work in wpilib
            // comment reference: $CONSTANTS_VAR_CHECK
        }
        appendage.moveUp(rpm);
    }

    @Override
    protected void execute() {}

    @Override
    protected boolean isFinished() {
        return appendage.hittingTopSwitch();
    }

    @Override
    protected void end() {
        appendage.stopMoving();
        appendage.zeroEncoder();
    }
}