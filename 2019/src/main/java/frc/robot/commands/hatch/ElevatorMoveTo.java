package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ElevatorMoveTo extends Command {
    private double toPos;
    private double positionDeadzone = 0.5;
    public ElevatorMoveTo(double pos) {
        requires(Robot.elevator);
        toPos = pos;
    }

    @Override
    public void initialize() {
        System.out.println("GOING TO " + toPos);
        Robot.elevator.goTo(toPos);
    }

    private boolean withinDeadzone() {
        /*double absPos = Math.abs(Robot.elevator.getPosition() - toPos);
        boolean negCheck = absPos < deadzoneUp;
        boolean posCheck = absPos < ;*/
        return Math.abs(Robot.elevator.getPosition() - toPos) < positionDeadzone;
    }

    @Override
    protected void end() {
        System.out.println("did thing");
        Robot.elevator.stopMoving();
    }

    @Override
    public boolean isFinished() {
        System.out.println("isFinished(): are we in the deadzone? " + withinDeadzone());
        return withinDeadzone();
    }
}
