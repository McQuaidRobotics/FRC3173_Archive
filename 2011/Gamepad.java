package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * Primary class for controlling the Logitech F310 Gamepad
 *
 * @author Nick Brown and Joshua Kwiatkowski
 */
public class Gamepad {

    /*
     * Constant values associated with each button
     */
    static class Button {

        public int button;
        static final int kXButton_val = 1;
        static final int kAButton_val = 2;
        static final int kBButton_val = 3;
        static final int kYButton_val = 4;
        static final int kLeftFrontTrigger_val = 5;
        static final int kRightFrontTrigger_val = 6;
        static final int kLeftBackTrigger_val = 7;
        static final int kRightBackTrigger_val = 8;
        static final int kBackButton_val = 9;
        static final int kStartButton_val = 10;
        static final int kLeftJoystick_val = 11;
        static final int kRightJoystick_val = 12;
        public static final Button kXButton = new Button(kXButton_val);
        public static final Button kAButton = new Button(kAButton_val);
        public static final Button kBButton = new Button(kBButton_val);
        public static final Button kYButton = new Button(kYButton_val);
        public static final Button kLeftFrontTrigger = new Button(kLeftFrontTrigger_val);
        public static final Button kRightFrontTrigger = new Button(kRightFrontTrigger_val);
        public static final Button kLeftBackTrigger = new Button(kLeftBackTrigger_val);
        public static final Button kRightBackTrigger = new Button(kRightBackTrigger_val);
        public static final Button kBackButton = new Button(kBackButton_val);
        public static final Button kStartButton = new Button(kStartButton_val);
        public static final Button kLeftJoystick = new Button(kLeftJoystick_val);
        public static final Button kRightJoystick = new Button(kRightJoystick_val);

        private Button(int button) {
            this.button = button;
        }
    }

    /**
     * Constant values associated with each axis
     */
    static class Axis {

        public int axis;
        static final int kLeftJoystickX_val = 1;
        static final int kLeftJoystickY_val = 2;
        static final int kRightJoystickX_val = 3;
        static final int kRightJoystickY_val = 4;
        static final int kDPadX_val = 5;
        static final int kDPadY_val = 6;
        public static final Axis kLeftJoystickX = new Axis(kLeftJoystickX_val);
        public static final Axis kLeftJoystickY = new Axis(kLeftJoystickY_val);
        public static final Axis kRightJoystickX = new Axis(kRightJoystickX_val);
        public static final Axis kRightJoystickY = new Axis(kRightJoystickY_val);
        public static final Axis kDPadX = new Axis(kDPadX_val);
        public static final Axis kDPadY = new Axis(kDPadY_val);

        private Axis(int axis) {
            this.axis = axis;
        }
    }

    /**
     * Constant values associated with the DPad buttons
     */
    static class DPadButton {

        public int dPadButton;
        static final int kUp_val = 0;
        static final int kDown_val = 1;
        static final int kRight_val = 2;
        static final int kLeft_val = 3;
        public static final DPadButton kUp = new DPadButton(kUp_val);
        public static final DPadButton kDown = new DPadButton(kDown_val);
        public static final DPadButton kRight = new DPadButton(kRight_val);
        public static final DPadButton kLeft = new DPadButton(kLeft_val);

        private DPadButton(int dPadButton) {
            this.dPadButton = dPadButton;
        }
    }
    private DriverStation m_ds;
    private final int m_port;

    /**
     * Construct an instance of a gamepad.
     * The gamepad index is the usb port on the drivers station.
     *
     * This constructor allows the subclass to configure the Drivestation and port number
     *
     * @param port The port on the driver station that the gamepad is plugged into.
     */
    public Gamepad(final int port) {
        m_ds = DriverStation.getInstance();
        m_port = port;
    }

    /**
     * Returns whether or not the specified button is pressed
     *
     * @param button Button to get the output from
     * @return Boolean: true is pressed
     */
    public boolean getButton(Button button) {
        return getRawButton(button.button);
    }

    /**
     * Returns the value for the specified joystick and axis
     *
     * @param axis Axis to get the output from
     * @return Double: -1.0 to 1.0 (For X: Right is positive; For Y: Backward is positive)
     */
    public double getAxis(Axis axis) {
        return getRawAxis(axis.axis);
    }

    /**
     * Returns the state of the specified DPad button
     *
     * @param dPadButton Which DPad button to get the state of
     * @return State of the specified DPad button (Pressed: returns true)
     */
    public boolean getDPadButton(DPadButton dPadButton) {
        switch (dPadButton.dPadButton) {
            case DPadButton.kUp_val:
                if (getAxis(Axis.kDPadY) == -1) {
                    return true;
                }
                break;
            case DPadButton.kDown_val:
                if (getAxis(Axis.kDPadY) == 1) {
                    return true;
                }
                break;
            case DPadButton.kRight_val:
                if (getAxis(Axis.kDPadX) == 1) {
                    return true;
                }
                break;
            case DPadButton.kLeft_val:
                if (getAxis(Axis.kDPadX) == -1) {
                    return true;
                }
                break;
        }

        return false; // Returns false if the button is not being pressed
    }

    /**
     * Returns the magnitude of the left joystick
     *
     * @return Returns the magnitude of the left joystick
     */
    public double getMagnitudeLeftJoystick() {
        return Math.sqrt(MathUtils.pow(getAxis(Axis.kLeftJoystickX), 2) + MathUtils.pow(getAxis(Axis.kLeftJoystickY), 2));
    }

    /**
     * Returns the magnitude of the right joystick
     *
     * @return Returns the magnitude of the right joystick
     */
    public double getMagnitudeRightJoystick() {
        return Math.sqrt(MathUtils.pow(getAxis(Axis.kRightJoystickX), 2) + MathUtils.pow(getAxis(Axis.kRightJoystickY), 2));
    }

    /**
     * Returns the direction the left stick is angled in radians
     *
     * @return Returns the direction the left stick is angled in radians
     */
    public double getDirectionRadiansLeftStick() {
        return MathUtils.atan2(getAxis(Axis.kLeftJoystickX), -getAxis(Axis.kLeftJoystickY));
    }

    /**
     * Returns the direction the right stick is angled in radians
     *
     * @return Returns the direction the right stick is angled in radians
     */
    public double getDirectionRadiansRightStick() {
        return MathUtils.atan2(getAxis(Axis.kRightJoystickX), -getAxis(Axis.kRightJoystickY));
    }

    /**
     * Returns the direction the left stick is angled in degrees
     *
     * @return Returns the direction the left stick is angled in degrees
     */
    public double getDirectionDegreesLeftStick() {
        return Math.toDegrees(getDirectionRadiansLeftStick());
    }

    /**
     * Returns the direction the right stick is angled in degrees
     *
     * @return Returns the direction the right stick is angled in degrees
     */
    public double getDirectionDegreesRightStick() {
        return Math.toDegrees(getDirectionRadiansRightStick());
    }

    /**
     * Get the button value for buttons 1 through 12
     *
     * The buttons are returned in a single 16 bit value with one bit representing the state
     * of each button. The appropriate button is returned as a boolean value.
     *
     * @param buttonID The button number to be read
     * @return The state of the button
     */
    public boolean getRawButton(int buttonID) {
        return ((0x1 << (buttonID - 1)) & m_ds.getStickButtons(m_port)) != 0;
    }

    /**
     * Get the value of the axis
     *
     * @param axisID The axis to read [1-6]
     * @return The value of the axis
     */
    public double getRawAxis(int axisID) {
        return m_ds.getStickAxis(m_port, axisID);
    }
}