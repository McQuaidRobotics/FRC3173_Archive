package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.util.AppendageSubsystem;

public class AppendageRise extends Command {
    private AppendageSubsystem appendage;
    private double rpm;
    private String name = "none";
    public AppendageRise(AppendageSubsystem appendage, double rpm, String name) {
        this.appendage = appendage;
        this.rpm = rpm;
        this.name = name;
        requires(appendage);
    }

    public AppendageRise(AppendageSubsystem appendage, double rpm) {
        this.appendage = appendage;
        this.rpm = rpm;
        requires(appendage);
    }

    @Override
    protected void initialize() {
        if(name.equals("pants")) {
            if(rpm != Constants.PANTS_RPMS) {
                rpm = Constants.PANTS_RPMS;
                // reference: $CONSTANTS_VAR_CHECK
            }
        } else if(name.equals("arms")) {
            if(rpm != Constants.ARMS_RPMS) {
                rpm = Constants.ARMS_RPMS;
                // reference: $CONSTANTS_VAR_CHECK
            }
        } else if(name.equals("none")) {
            System.out.println("no name given for appendage with rpm " + rpm + ", unable to check variable changes");
        } else {
            // if the name is deliberately not pants, arms, or none
            System.out.println("unknown name for appendage with rpm " + rpm + ", unable to check variable changes");
        }
        appendage.moveDown(rpm);
    }

    @Override
    protected void execute() {}

    @Override
    protected boolean isFinished() {
        // Use the arms limit to decide when to stop rising both the pants and the arms.
        // This allows us to adapt to the size of the platform so we can do level 2 climb.
        return Robot.arms.hittingBottomSwitch();
    }

    @Override
    protected void end() {
        appendage.stopMoving();
    }
}