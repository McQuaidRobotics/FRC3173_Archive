/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 * @todo what is this supposed to do?
 * @author benjamin
 */
public class RobotID {
    private String m_robotName;
    public static final RobotID COMPETITION_ROBOT = new RobotID("Competition robot");
    public static final RobotID TEST_ROBOT = new RobotID("Test robot");
    private RobotID m_currentRobot = COMPETITION_ROBOT;
    private RobotID(String name){
        m_robotName = name;
    }
    public String getName(){
        return m_robotName;
    }
    public RobotID getCurrentRobot(){
        return m_currentRobot;
    }
    public void setCurrentRobot(RobotID robotID){
        m_currentRobot = robotID;
    }    
}
