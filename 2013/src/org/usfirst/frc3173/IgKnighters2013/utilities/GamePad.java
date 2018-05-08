/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc3173.IgKnighters2013.utilities;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.DriverStation;


/**
 * Class controlling the controller;
 * @author jaho2013
 */

public class GamePad{
    
    private DriverStation m_ds;
    private int m_port = 0;
    
    /**
     * Creates the game pad object
     * @param port Port on the driver station that the pad is plugged into
     */
    
    public GamePad(final int port){
        m_ds = DriverStation.getInstance();
            m_port = port;
    }
    
    /**
     * Creates the buttons on the controller.
     */
    
    static class Button {
        public int button;
        static final int kXButtonV = 1;
        static final int kAButtonV = 2;
        static final int kBButtonV = 3;
        static final int kYButtonV = 4;
        static final int kFrontLeftShoulderV = 5;
        static final int kFrontRightShoulderV = 6;
        static final int kBackLeftShoulderV = 7;
        static final int kBackRightShoulderV = 8;
        static final int kBackV = 9;
        static final int kStartV = 10; 
        static final int kLeftStickV = 11;
        static final int kRightStickV = 12;
        public static final Button kAButton = new Button(kAButtonV);
        public static final Button kBButton = new Button(kBButtonV);
        public static final Button kYButton = new Button(kYButtonV);
        public static final Button kXButton = new Button(kXButtonV);
        public static final Button kFrontLeftShoulder = new Button(kFrontLeftShoulderV);
        public static final Button kFrontRightShoulder = new Button(kFrontRightShoulderV);
        public static final Button kBackLeftShoulder = new Button(kBackLeftShoulderV);
        public static final Button kBackRightShoulder = new Button(kBackRightShoulderV);
        public static final Button kStart = new Button(kStartV);
        public static final Button kBack = new Button(kBackV);
        public static final Button kLeftStick = new Button(kLeftStickV);
        public static final Button kRightStick = new Button(kRightStickV);
    
        private Button(int button){
            this.button = button;
       }
    }
    
    /**
     * Creates the axes on the controller.
     */
    
    static class Axis {
        public int axis;
        static final int kLeftStickXV = 1;
        static final int kLeftStickYV = 2;
        static final int kRightStickXV = 3;
        static final int kRightStickYV = 4;
        static final int kDPadXV = 5;
        static final int kDPadYV = 6;
        public static final Axis kLeftStickX = new Axis(kLeftStickXV);
        public static final Axis kLeftStickY = new Axis(kLeftStickYV);
        public static final Axis kRightStickX = new Axis(kRightStickXV);
        public static final Axis kRightStickY = new Axis(kRightStickYV);
        public static final Axis kDPadX = new Axis(kDPadXV);
        public static final Axis kDPadY = new Axis(kDPadYV);
            
        private Axis(int axis){
            this.axis = axis;
        }
    }
    
    /**
     * Creates the DPad buttons on the controller.
     */
    
    static class DPadButton {
        public int dPadButton;
        static final int kDPadUpV = 1;
        static final int kDPadRightV = 2;
        static final int kDPadDownV = 3;
        static final int kDPadLeftV = 4;
        public static final DPadButton kDPadUp = new DPadButton(kDPadUpV);
        public static final DPadButton kDPadRight = new DPadButton(kDPadRightV);
        public static final DPadButton kDPadDown = new DPadButton(kDPadDownV);
        public static final DPadButton kDPadLeft = new DPadButton(kDPadLeftV);
        
        private DPadButton(int dPadButton){
            this.dPadButton = dPadButton;
        }
    }
    
    /**
     * Gets the state of a button.
     * @param button The button to be evaluated.
     * @return The state of the button.
     */
    
    public boolean getButton(Button button){
        return getRawButton(button.button);
    }
    
    /**
     * Gets the state of an axis.
     * @param axis The axis to be evaluated.
     * @return The state of the button/
     */
    
    public double getAxis(Axis axis){
        return getRawAxis(axis.axis);
    }
    
    /**
     * Gets the state of a DPad button.
     * @param dPadButton The DPad button to be evaluated.
     * @return The state of the DPad button.
     */
    
    public boolean getDPadButton(DPadButton dPadButton){
        switch(dPadButton.dPadButton){
            case DPadButton.kDPadUpV:
                if(getAxis(Axis.kDPadY) > .5){
                    return true;
                }
                break;
            case DPadButton.kDPadRightV:
                if(getAxis(Axis.kDPadX) > .5){
                    return true;
                }
                break;
            case DPadButton.kDPadDownV:
                if(getAxis(Axis.kDPadY) < -.5){
                    return true;
                }
                break;
            case DPadButton.kDPadLeftV:
                if(getAxis(Axis.kLeftStickX) < -.5){
                    return true;
                }
                break;
        }
        return false;
    }
    
    /**
     * Gets the bit value of the button and returns it as a boolean.
     * @param buttonID The ID of the button to be evaluated.
     * @return The boolean state of the button.
     */
    
    public boolean getRawButton(int buttonID){
        return ((0x1 << (buttonID - 1)) & m_ds.getStickButtons(m_port)) != 0;
    }
    
    /**
     * Gets the double value of the axis.
     * @param axisID The ID of the axis to be evaluated.
     * @return The double value of the axis.
     */
    
    public double getRawAxis(int axisID){
        return m_ds.getStickAxis(m_port,axisID);
    }
    
    /**
     * Returns the magnitude of the left stick.
     * @return The magnitude.
     */
    
    public double getMagnitudeLeftStick(){
        return Math.sqrt((getAxis(Axis.kLeftStickX)*(getAxis(Axis.kLeftStickX)))+((getAxis(Axis.kLeftStickY)*getAxis(Axis.kLeftStickY))));
    }
    
    /**
     * Returns the magnitude of the right stick.
     * @return The magnitude.
     */
    
    public double getMagnitudeRightStick(){
        return Math.sqrt((getAxis(Axis.kRightStickX)*(getAxis(Axis.kRightStickX)))+((getAxis(Axis.kRightStickY)*getAxis(Axis.kRightStickY))));
    }
    
    /**
     * Gets the direction of the left stick in radians.
     * @return Radian value of the direction.
     */
    
    public double getDirectionRadiansLeftStick() {
        return MathUtils.atan2(getAxis(Axis.kLeftStickX), -getAxis(Axis.kLeftStickY));
    } 
    
    /**
     * Gets the direction of the right stick in radians.
     * @return Radian value of the direction.
     */
    
    public double getDirectionRadiansRightStick() {
        return MathUtils.atan2(getAxis(Axis.kRightStickX), -getAxis(Axis.kRightStickY));
    }
    
    /**
     * Gets the direction of the left stick in degrees.
     * @return Degree value of the direction.
     */
    
    public double getDirectionDegreesLeftStick(){
        return Math.toDegrees(this.getDirectionRadiansLeftStick());
    }
    
    /**
     * Gets the direction of the right stick in degrees.
     * @return Degree value of the direction.
     */
    
    public double getDirectionDegreesRightStick(){
        return Math.toDegrees(this.getDirectionRadiansRightStick());
    }
}
