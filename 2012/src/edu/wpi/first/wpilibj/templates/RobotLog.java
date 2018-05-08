/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.PrintStream;
import javax.microedition.io.Connector;

/**
 * Extends PrintStream for easy outputting.
 * NOTE: The specified file MUST already exist on the cRIO.
 * @author Benjamin
 */
public class RobotLog extends PrintStream{
    public static final long START_TIME = System.currentTimeMillis();
    /**
     * Constructs a new RobotLog object.
     * @param filename The file to output the log to.
     * @throws IOException Because it wouldn't let me use a try/catch.
     */
    public RobotLog(String filename) throws IOException{
        super(((FileConnection)Connector.open("file:///" + filename)).openOutputStream());
        
    }
    /**
     * Gets the current time formatted as hours:minutes:seconds:milliseconds.
     * @return 
     */
    public String getTimeStamp(){
       return (System.currentTimeMillis() - START_TIME) / 3600000 + ":" + ((System.currentTimeMillis() - START_TIME) % 3600000) / 60000 + ":" + ((System.currentTimeMillis() - START_TIME) % 60000) / 1000 + ":" + (System.currentTimeMillis() - START_TIME) % 1000;
    }
    public void print(Object obj){
        super.print(obj);
        super.print(" " + getTimeStamp());
    }
    public void print(String str){
        super.print(str + " " + getTimeStamp());
    }
    public void print(boolean b){
        super.print(b + " " + getTimeStamp());
    }
    public void print(char c){
        super.print(c + " " + getTimeStamp());
    }
    public void print(char[] c){
        super.print(c);
        super.print(" " + getTimeStamp());
    }
    public void print(double d){
        super.print(d + " " + getTimeStamp());
    }
    public void print(float f){
        super.print(f + " " + getTimeStamp());
    }
    public void print(int i){
        super.print(i + " " + getTimeStamp());
    }
    public void print(long l){
        super.print(l + " " + getTimeStamp());
    }
}
