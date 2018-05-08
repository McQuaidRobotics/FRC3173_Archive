/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Servo;

/**
 * @todo test this class
 * @todo Beginning JavaDoc?
 * @author Thomas Skysword
 */
public class ProximitySensor {
    private BOEBotServo m_rotationServo;
    private Gyro m_robotGyro;
    private AnalogChannel m_proximitySensor;
    private final double VCC = 5; // Voltage to sensor [Measured in Volts]
    private final double SCALING_FACTOR = VCC / 512; // Scaling factor is (Vcc / 512) per inch [Measured in Volts/Inch)
    /**
     * Constructs a new ProximitySensor object.
     * @param rotationDetection The gyro to use for detecting the current angle.
     */
    public ProximitySensor (Gyro rotationDetection){
        m_proximitySensor=new AnalogChannel(ElectricalIDs.AnalogIn.PROXIMITY_SENSOR);
        m_rotationServo=new BOEBotServo(ElectricalIDs.PWM.PROXIMITY_SENSOR_SERVO);
        m_robotGyro=rotationDetection;
    }
    /**
     * @return The Distance of the closest detected obstacle (in inches).
     */
    public double getDistance(){
        return m_proximitySensor.getVoltage()/SCALING_FACTOR;
    }
    /**
     * Rotates the servo that the proximity sensor is mounted on. 
     * ANGLE_CONVERSION changes from a value of 0-360 to 0-1.
     * @param angle The angle to rotate to.
     */
    public void rotateSensor(double angle){
        m_rotationServo.setLocation(angle);
    }
    /**
     * ANGLE_CONVERSION changes from a value of 0-1 to 0-360.
     * @return The current angle.
     */
    public double getAngle(){
        return m_rotationServo.getLocation();
    }
    /**
     * Rotates the servo to face the proximity sensor at the wall.
     * @TODO The description is a guess, after verifying or fixing it remove this todo.
     * @TODO A little to much automagic here, please add javadoc to every if statement.
     * @TODO What is the rotation variable used for?
     * @TODO what is the last else for? It doesn't do anything(neededRotation is already 0).
     */
    public void faceWall(){
        int rotation=0;
        double neededRotation=0;
        if(m_robotGyro.getAngle()>360 || m_robotGyro.getAngle()<-360){
            neededRotation=(m_robotGyro.getAngle()/360);
            Double temp=new Double(neededRotation);
            rotation=temp.intValue();
            neededRotation=(neededRotation-(double)rotation)*360;
        }
        else{
            neededRotation=0.0;
        }
       
       /* if(neededRotation>0 && neededRotation+getAngle()>360){
            rotation=(360-Math.abs(neededRotation))-getAngle();
        }
        else if(neededRotation>0 && neededRotation+getAngle()<=360){
            rotation=neededRotation+getAngle();
        }
        else if(neededRotation<0 && neededRotation+getAngle()<0){
            rotation=(360-Math.abs(neededRotation))+getAngle();
        }
        else if(neededRotation<0 && neededRotation+getAngle()>=0){
            rotation=neededRotation+getAngle();
        }
        else{
            rotation=getAngle();
        }*/
        rotateSensor(neededRotation);
       
    }
}
