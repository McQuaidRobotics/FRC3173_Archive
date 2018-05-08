/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * Controls the acquisition mechanism
 * @author Michael Baxter
 */
public class Acquisition {
    private RobotJaguar m_rightCANJaguar, m_leftCANJaguar;
    private boolean m_disableAcquisitionStop;
    /**
     *Initialize the jaguars for the acquisition mechanism.
     */
    public Acquisition() {
		m_rightCANJaguar=new RobotJaguar(ElectricalIDs.CANJaguars.RIGHT_ACQUISITION_JAGUAR,false);
		m_leftCANJaguar=new RobotJaguar(ElectricalIDs.CANJaguars.LEFT_ACQUISITION_JAGUAR,false);
    }
	/**
	* Start the acquisition pulleys spinning. They will spin
	* until they are stopped.
	*/
    public void startAcquisition(){
		m_rightCANJaguar.setX(1);
		m_leftCANJaguar.setX(-1);
    }
	/**
	* Set the acquisition pulleys spinning in reverse. This is for
	* unjamming any balls that have become jammed in the robot.
	*/
    public void reverseAcquisition() {
		m_rightCANJaguar.setX(-1);
		m_leftCANJaguar.setX(1);
    }
	/**
	* Stop the acquisition mechanism.
	* @param emergency Whether or not it is an emergency stop.
	* @param ballIsInWay Whether or not a ball is in the way of the distance sensor.
	*/
    public void stopAcquisition(boolean emergency, boolean ballIsInWay){
		if(emergency || (!ballIsInWay && !m_disableAcquisitionStop)){
			m_rightCANJaguar.setX(0);
			m_leftCANJaguar.setX(0);
		}
    }
    /**
     * Return true when the acquisition mechanism is going.
     * It will return false if the acquisition mechanism is stopped or running in
     * reverse.
     * @return 
     */
    public boolean isAcquiring(){
		return (m_rightCANJaguar.getX()==1 && m_leftCANJaguar.getX()==-1);
    }
    /**
     * Enables or disables the ability of acquisition to stop (except for emergencies).
     * @param enabled Whether acquisition is allowed to stop or not.
     */
    public void enableAcquisitionStop(boolean enabled){
	m_disableAcquisitionStop = !enabled;
    }

}
