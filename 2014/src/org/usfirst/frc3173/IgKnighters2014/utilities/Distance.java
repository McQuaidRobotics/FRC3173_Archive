/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.usfirst.frc3173.IgKnighters2014.utilities;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc3173.IgKnighters2014.Robot;
import org.usfirst.frc3173.IgKnighters2014.RobotMap;

/**
 *
 * @author Tyler
 */
public class Distance {
       static public final double CONVERS=512/5;
       static AnalogChannel distance = RobotMap.pneumagicsdistance;
       
       public static void printDistance(){
           //SmartDashboard.putNumber("Distance ",getDistance());
           SmartDashboard.putNumber("Distance",getDistance());
       }
        
       public static void initializeDist(){
            
       }
        
       public static double getDistance(int times){
            double result=0;
            for(int i=0;i<times;i++){
                result += distance.getVoltage()*CONVERS;
            }
            return result/times;
       }
       
       public static double getDistance(){
           return getDistance(2);
       }
        
}
