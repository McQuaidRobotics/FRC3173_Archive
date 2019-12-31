package frc.robot.commands.auto.climb;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;

public class WaitForButton extends Command {
    @Override
    protected void initialize() { 
        System.out.println("Waiting for button");
    }

    @Override
    protected boolean isFinished() {
        return OI.rightJoystick.getRawButton(1);
    }
}
