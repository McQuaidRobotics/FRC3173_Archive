/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * @todo document this class
 * @author Thomas Skysword
 */
public class Storage {
    private RobotJaguar m_pulleyBottom;
    private DigitalInput m_bottom,m_top,m_middle;
    private boolean m_pulleyMoving=false;
    private static final float M_SPEEDREVERSE=0.25f;
    private static final float M_SPEEDFORWARD=1; // @todo make sure this is the right speed
    public static final int BALL_POSITION_TOP = 1;
    public static final int BALL_POSITION_MIDDLE = 2;
    public static final int BALL_POSITION_BOTTOM = 3;
    public static final int BALL_POSITION_NONE = -1;
    private int m_storageCount=0;
	
    public Storage(DigitalInput bottom, DigitalInput middle, DigitalInput top){
		m_pulleyBottom = new RobotJaguar(ElectricalIDs.CANJaguars.STORAGE_JAGUAR,false);
		m_bottom=bottom;
		m_top=top;
		m_middle=middle;
//            m_startcounter=0;
    }
    
    
    /**
     * This starts the pulleys. The pulleys will move in the direction specified.
     * Manually starts storage.
     * A better way to start storage is trackMove(int direction)
	 * @todo why is this depreciated instead of removed? It is never used!
	 * @param direction 1 or -1 for forward or backwards
         * @deprecated 
     */
    public void startPulleyMove(int direction){  
		m_pulleyMoving = true;
		m_pulleyBottom.setX(-direction);
    }                     
    /**
     * MANUAL OVERIDE use trackMove(int direction);
     * @param direction 1 or -1
     * @param speed range from 0-1
     */
    public void startPulleyMove(int direction,float speed){
		m_pulleyMoving = true;
		m_pulleyBottom.setX(-speed*direction);
    }
    /**This manually stops the motors and the pulleys
     * the motor should already stop during the trackMove(int direction) method
     */
    public void stopPulleyMove(){
		m_pulleyBottom.setX(0);
		m_pulleyMoving=false; 
    }  
    /**
     * if run forward(1) the track move method will run until a ball is in the top position
     * if run in reverse(-1) the storage unit will not stop until the method is overrode.
     * @param direction 
     */
    public void trackMove(int direction){
        if(direction>0){
            if(!isPositionOccupied(BALL_POSITION_TOP)){
                startPulleyMove(direction,M_SPEEDFORWARD);
            }
            else{
                stopPulleyMove();
            }
        }
        else if(direction<0){
            startPulleyMove(direction,M_SPEEDREVERSE);
        }
        else{
            stopPulleyMove();
        }        
    }
    /**
     * use if there are no sensors on the robot will raise a ball to the top of the robot.
     * this method may delay robot please use with care.
     * I will mark this method as deprecated so that you won't forget.
     * @deprecated 
     * @param direction 1 is forward -1 is backwards
     */
    public void trackMoveNone(int direction){
        startPulleyMove(1,M_SPEEDFORWARD);
        Timer.delay(2);
        stopPulleyMove();
    }
    /**
     * Sends a ball to the shooter.
     * @return the value of isMoving()
     */
    public boolean toShoot(){
        startPulleyMove(1,M_SPEEDFORWARD);
        Timer.delay(1);
        stopPulleyMove();
/*        int counter=0;
        if(isPositionOccupied(BALL_POSITION_TOP)){
            if(counter>0){
                trackMove(1);
            }
            else{
                counter=0;
                startPulleyMove(1,M_SPEEDFORWARD);
            }
        }
        else{
            if(isMoving()){
                counter++;
            }
        }*/
        return isMoving();
    }
    
  
    
    
    /**
     * Receives a ball from the space between acquisition and storage.
     * @return isMoving()
     */
    public boolean toAcquire(){
        if(getTopMostFreePosition()==1){
            trackMove(1);
            return isMoving();
        }
        else{
            return false;
        }
    }
    /**
     * 
     * @return count of balls within the robot 
     */
    public int getStorageCount(){
        return m_storageCount;
    }
    /**
     * Tells whether the pulley is moving or not.
     * @return m_pulleyMoving
     */
    public boolean isMoving(){
        return m_pulleyMoving;
    }
    /**
     * @return The top most position that is free
     */
    public int getTopMostFreePosition(){
        if(!m_top.get()){
            return BALL_POSITION_TOP;
        }
        else if(!m_middle.get()){
            return BALL_POSITION_MIDDLE;
        }
        else if(!m_bottom.get()){
            return BALL_POSITION_BOTTOM;
        }
        else{
            return BALL_POSITION_NONE;
        }
    }
    /**
     * Takes a position 1 is top 3 is bottom.
     * @param pos
     * @return The value of that position
     */
    public boolean isPositionOccupied(int pos){
        DigitalInput x=m_top;
        if(pos==BALL_POSITION_TOP){
            x=m_top;
        }
        else if(pos==BALL_POSITION_MIDDLE){
            x=m_middle;
        }
        else if(pos==BALL_POSITION_BOTTOM){
            x=m_bottom;
        }
        else{
            return false;
        }
        return x.get();
    }
	public double motorCurrent() {
		return m_pulleyBottom.getOutputCurrent();
	}
}
