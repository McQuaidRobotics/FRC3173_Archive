package frc.robot.commands.auto.climb;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Constants;
//import frc.robot.Robot;

public class PrintAutoClimbVars extends InstantCommand {
    @Override
    protected void initialize() { 
        System.out.println("---BEGIN AUTO CLIMB VARIABLES---");
        System.out.println("DEBUG_WAIT: " + Constants.DEBUG_WAIT);
        System.out.println("ARMS_RPMS: " + Constants.ARMS_RPMS);
        System.out.println("PANTS_RPMS: " + Constants.PANTS_RPMS);
        System.out.println("URGE_RPMS: " + Constants.URGE_RPMS);
        System.out.println("--- END AUTO CLIMB VARIABLES ---");
    }
}