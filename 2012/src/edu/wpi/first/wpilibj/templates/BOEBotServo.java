/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author benjamin
 */
public class BOEBotServo extends PWM{
    public static final int FULL_REVERSE = 1;
    public static final int STOP = 127;
    public static final int FULL_FORWARD = 255;
    private SpinningThread spinningThread;
    private double m_currentLocation;
    private double m_requiredLocation;
    private double m_millisToDegreesRatio = 1020/360d;
    private class SpinningThread extends Thread{
	private BOEBotServo m_BOEBotServo;
	private double m_rotationAmount;
	/**
	 * Constructs a new Thread to spin the servo.
	 * @param name The name of the Thread.
	 * @param bbs The BoeBot Servo to activate.
	 */
	public SpinningThread(String name, BOEBotServo bbs){
	    super(name);
	    m_BOEBotServo = bbs;
	}
	/**
	 * The code that runs continuously.
	 */
	public void run(){
	    setLocation(m_requiredLocation);
	}
	/**
	 * Sets the location of the servo to whatever is specified.
	 * @param degrees The degrees to move the servo to.
	 */
	public void setLocation(double degrees){
	    m_rotationAmount = degrees - m_currentLocation;
	    if(m_rotationAmount < 0){
		m_BOEBotServo.setRaw(FULL_REVERSE);
	    }
	    else if(m_rotationAmount > 0){
		m_BOEBotServo.setRaw(FULL_FORWARD);
	    } else{
		m_BOEBotServo.setRaw(STOP);
	    }
	    try {
		Thread.sleep((long)(m_millisToDegreesRatio*degrees));
	    } catch (InterruptedException ex) {
		ex.printStackTrace();
	    }
	    m_BOEBotServo.setRaw(STOP);
	    m_currentLocation = m_requiredLocation;
	}
    }
    /**
     * Constructs a new BOEBotServo on the specified channel.
     * @param channel The channel that the BOEBotServo is connected to.
     */
    public BOEBotServo(int channel){
	super(channel);
	m_currentLocation = 0;
	m_requiredLocation = 0;
	spinningThread = new SpinningThread("Servo Spinner", this);
	spinningThread.start();
    }
    /**
     * Sets the location of the servo to the specified degrees.
     * @param degrees The amount to rotate the servo.
     */
    public void setLocation(double degrees){
	if(degrees > 360 || degrees < 0){
	    throw new IllegalArgumentException("Location must be between 0 and 360");
	}
	else{
	    m_requiredLocation = degrees;
	}
    }
    /**
     * Returns the current location of the servo.
     * @return The current location of the servo.
     */
    public double getLocation(){
	return m_currentLocation;
    }
    /**
     * Resets the current location and the required location to zero;
     */
    public void resetLocation(){
	m_currentLocation = 0;
	m_requiredLocation = 0;
    }
}
