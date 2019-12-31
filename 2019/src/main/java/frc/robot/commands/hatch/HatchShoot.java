package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

public class HatchShoot extends InstantCommand {
    /*
    Methodology:
    1. driver makes sure that impact hatch is out 
    2. flip the states of both the impact and release hatches
    this retracts the impact and extends the release
    so that we rip the hatch off of the velcro more easily
    */
    @Override
    protected void initialize() { 
        // handle part 2
        Robot.hatch.invertImpact();
        Robot.hatch.invertRelease();
    }
}