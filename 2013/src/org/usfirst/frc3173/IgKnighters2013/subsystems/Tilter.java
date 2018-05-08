/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc3173.IgKnighters2013.subsystems;
import org.usfirst.frc3173.IgKnighters2013.RobotMap;
import org.usfirst.frc3173.IgKnighters2013.commands.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANJaguar;
/* DO NOT CHANGE THIS COMMENT!
 * It's needed so the RobotBuilder doesn't put the import back in
import edu.wpi.first.wpilibj.can.*;
*/
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Jon Aho, Thomas Skysword and Rachel Bluethorn
 */

public class Tilter extends Subsystem {
    public double angles = .6;
    public static double min=.06;
    public static double max=.265;
    protected void initDefaultCommand() {
        setDefaultCommand(new TilterTilt());
    }
    /**
     * 
     * @return potentiometer position
     */
    public double getAngle(){
        return RobotMap.tilterTilter.getPosition();
    }   
    /**
     * 
     * @param angle sets Speed from -1 to 1;
     */
    public void setAngle(double angle){
        RobotMap.tilterTilter.setX(angle);
    }
    /**
     * Position mode only;
     * @deprecated use tilterTilter.setPID instead
     * @param position set a potentiometer P value
     */
    public void setP(double position){
        RobotMap.tilterTilter.setPID(position, RobotMap.tilterTilter.getI(), RobotMap.tilterTilter.getD());
    }
    /**
     * Position mode only;
     * @deprecated use tilterTilter.setPID instead
     * @param position set a potentiometer PID value
     */
    public void setPID(double position,double integ,double diff){
        RobotMap.tilterTilter.setPID(position, integ, diff);
    }
    /**
     * position mode, sets presets
     * @param type name of presets(Autonomous,Midfield,Feeder,Climber)
     * @return if valid
     */
    public boolean setAngle(String type){//.04-.63
        if(type.equalsIgnoreCase("Autonomous")){
            angles =max+((min-max)*.867);//.3;///(1-0.867);
        }else if(type.equalsIgnoreCase("Midfield")){
            angles =max+((min-max)*.867);//.3;//(1-0.867);
        }else if(type.equalsIgnoreCase("Feeder")){
            angles =max+((min-max)*0.5);//.44;//(0.5);
        }else if(type.equalsIgnoreCase("Climb")){
            angles =max;//.6;//.63;
        }else{
            return false;
        }
        RobotMap.tilterTilter.setX(angles);
        return true;
    }
    /**
     * sets min value to the potentiometer, AKA: Top of tilter.
     * @param mins in potentiometer
     */
    public void setMin(double mins){
        min=mins;
    }
    /**
     * sets max value to the potentiometer, AKA: bottom of tilter.
     * @param maxs in potentiometer
     */
    public void setMax(double maxs){
        max=maxs;
    }
    /**
     * 
     * @return max value/ bottom
     */
    public static double getMax(){
        return max;
    }
    /**
     * 
     * @return min value/top
     */
    public static double getMin(){
        return min;
    }
}
