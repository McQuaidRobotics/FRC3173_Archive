package frc.robot.commands.auto.climb;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.PrintCommand;
import frc.robot.Constants;
//import frc.robot.Robot;
//import frc.robot.commands.buddy.Buddy1;
//import frc.robot.commands.climber.AppendageToTop;
//import frc.robot.commands.climber.ForwardUrgeRevolutions;
import frc.robot.commands.hatch.ImpactExtend;
//import frc.robot.commands.hatch.ReleaseExtend;

public class AutoClimb extends CommandGroup {

    public AutoClimb() {
        // ********************************************************
        /* Pre-Climb
        // ********************************************************
        1) Lower Elevator
        2) Extend HatchAcquire
        3) Break velcro that retains arms instide frame
        4) Partially elevate arms to allow them to drop outside of fram
        5) Extend Arms out over bumpers
        6) Raise arms to max height
        7) Manually drive to dock at climb platform
        */

        addSequential(new ToggleAutoClimbActive());  // set Constants.AUTOCLIMB_IS_ACTIVE = true;

        addSequential(new PrintAutoClimbVars());  // show climb variables on run

        addSequential(new ImpactExtend());
        //addSequential(new Buddy1());

        // Break velcro and release arms
        //addSequential(new ForwardUrgeRevolutions(-3800, -2*(42*64)));  // two rev
        
        // MANUAL STEP: driver has to lower arms by momentum 
        addSequential(new WaitForButton());

        // Move arms up
        addSequential(new PrintCommand("Move arms UP to limit"));
        //addSequential(new AppendageToTop(Robot.arms, Constants.REV_NEO_MAX_RPMS));
        //debugWait();

        addSequential(new PrintCommand("Wait for manual dock at HAB platform by driver"));
        //addSequential(new WaitForButton());


        // ********************************************************
        // Climb
        // ********************************************************

        // Move to floor
        addSequential(new Climb_20_InitClimb());
        debugWait();

        // Rise
        addSequential(new PrintCommand("Climb_30_Rise"));
        addSequential(new Climb_30_Rise());
        debugWait();

        // Level with platform; drive forward on urgers
        addSequential(new PrintCommand("Climb_40_UrgeOnPlatform"));
        addSequential(new Climb_40_UrgeOnPlatform());
        debugWait();

        // Lift pants
        addSequential(new PrintCommand("Climb_50_LowerToPlatform"));
        addSequential(new Climb_50_LowerToPlatform());
        debugWait();

        // Drive backwards to get further on to platform
        addSequential(new Climb_60_PostClimb());

        addSequential(new ToggleAutoClimbActive());  // set Constants.AUTOCLIMB_IS_ACTIVE = false;
    }
    
    private void debugWait(){
        if (Constants.DEBUG_WAIT) {
            addSequential(new WaitForButton());
        }
    }

}

