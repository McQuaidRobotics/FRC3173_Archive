package frc.robot.commands.climber;

import com.revrobotics.CANDigitalInput;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

public class TogglePantsLimit extends InstantCommand {
    private CANDigitalInput limit;
    public TogglePantsLimit(String limit) {
        limit = limit.toLowerCase();  // normalize
        if(limit.equals("forward")) {
            this.limit = Robot.pants.getForwardLimit();
        } else if(limit.equals("reverse")) {
            this.limit = Robot.pants.getReverseLimit();
        } else {
            System.out.println("WARNING: TogglePantsLimit(\"" + limit + "\") IS NOT A VALID LIMIT TYPE.");
        }
    }

    @Override
    protected void initialize() {
        limit.enableLimitSwitch(!limit.isLimitSwitchEnabled());  // flip the state of the limit switch
    }
}