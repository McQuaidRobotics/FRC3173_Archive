package frc.robot.util;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Helper class to provide a fluent API for JoystickButton.
 */
public class JoyButton {
    private JoystickButton button;
    public JoyButton(GenericHID joystick, int buttonId) {
        button = new JoystickButton(joystick, buttonId);
    }

    public JoyButton whenPressed(Command cmd) {
        button.whenPressed(cmd);
        return this;
    }

    public JoyButton whenReleased(Command cmd) {
        button.whenReleased(cmd);
        return this;
    }

    public JoyButton whileHeld(Command cmd) {
        button.whileHeld(cmd);
        return this;
    }

	public void whenPressed() {
	}
}
