package frc.robot.commands.hatch;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.Robot;

public class ElevatorManualDrive extends Command {
    private double deadband = 0.35;  // deadzone for the joystick
    private double speed = 2.0;  // speed for the manual elevator drive

    public ElevatorManualDrive() {
        requires(Robot.elevator);
    }

    @Override
    public void execute() {
        //System.out.println("EXECUTING ELEVSAPKPJFAOJKFJKD MANUA;");
        double setPoint = -OI.rightJoystick.getX();  // get joystick x value
        double deadzone = deadband;  // create deadzone
        if(Math.abs(setPoint) <= deadzone) {
            setPoint = 0;
        }

        boolean toggleButtonClicked = OI.rightJoystick.getRawButton(6);
        if(toggleButtonClicked) {
            // flip between position and velocity modes
            Robot.elevator.inPositionMode = !Robot.elevator.inPositionMode;
        }

        System.out.println(Robot.elevator.inPositionMode);

        if(Robot.elevator.inPositionMode) {
            boolean middleButtonClicked = OI.rightJoystick.getRawButtonPressed(3);
            boolean leftButtonClicked = OI.rightJoystick.getRawButtonPressed(4);
            boolean rightButtonClicked = OI.rightJoystick.getRawButtonPressed(5);
            if (middleButtonClicked) {
                Robot.elevator.goToInches(-Constants.ELEVATOR_WINCH_INCHES_MIDDLE);
            } else if (leftButtonClicked) {
                Robot.elevator.goToInches(-Constants.ELEVATOR_WINCH_INCHES_FLOOR);
            } else if (rightButtonClicked) {
                Robot.elevator.goToInches(-Constants.ELEVATOR_WINCH_INCHES_TOP);
            }
        } else {
            // we're just manually driving the elevator
            double pos = setPoint * Constants.ELEVATOR_MAX_MANUAL_WITHOUT_PID;  // rpms
            Robot.elevator.moveAt(pos);
        }
    }

    @Override
    protected void end() {
        System.out.println("STOPPING THE ELEVATOR");
        Robot.elevator.stopMoving();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
