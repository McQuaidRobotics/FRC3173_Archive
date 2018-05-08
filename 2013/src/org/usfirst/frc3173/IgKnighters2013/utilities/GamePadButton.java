/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc3173.IgKnighters2013.utilities;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 *
 * @author Thomas Skysword
 */
public class GamePadButton extends Button {

    GamePad m_gamepad;
    int m_buttonNumber;

    /**
     * Create a joystick button for triggering commands
     * @param joystick The GenericHID object that has the button (e.g. Joystick, KinectStick, etc)
     * @param buttonNumber The button number (see {@link GenericHID#getRawButton(int) }
     */
    public GamePadButton(GamePad gamepad, int buttonNumber) {
        m_gamepad = gamepad;
        m_buttonNumber = buttonNumber;
    }
    
    /**
     * Gets the value of the joystick button
     * @return The value of the joystick button
     */
    public boolean get() {
        return m_gamepad.getRawButton(m_buttonNumber);
    }
}
