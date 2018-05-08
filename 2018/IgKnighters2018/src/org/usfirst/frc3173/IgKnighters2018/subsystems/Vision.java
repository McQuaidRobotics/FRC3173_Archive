// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3173.IgKnighters2018.subsystems;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import org.usfirst.frc3173.IgKnighters2018.RobotMap;
import org.usfirst.frc3173.IgKnighters2018.commands.*;
import edu.wpi.first.wpilibj.command.Subsystem;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 * @Author John Marangola
 */
public class Vision extends Subsystem {
	private final int portNumber = 5805;
	private final String hostName = "host";
	private Socket server;
	private DataInputStream visionInput;
	private BufferedReader reader;
	private Reader r;
	private long packetNumber;
	private long pixelsAway;
	
    public void initDefaultCommand() {
    	connectServer();
    	
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }
    public void connectServer() {
    	try {
    		server = new Socket(hostName, portNumber);
    		visionInput = new DataInputStream(server.getInputStream());
    		//reader = new BufferedReader();	
    	}
    	catch (IOException e) {
    		System.err.println(e.toString());
    	}
    	DatagramSocket server = null;
    	try {
    		server = new DatagramSocket(portNumber);
    	} catch (Exception e) {
    		
    	}
    }
    public boolean getStatus(){
    	//return server.isConnected();
    	return server.isConnected();
    }
    public String getData() {
    	if (getStatus()) {
        	try {
        		return reader.readLine();
    		} 
        	catch (IOException e) {
    			e.printStackTrace();
    			return e.getMessage();
    		}
        	}
		return "NotConnected";
    	
    }
    public void terminateConnection(){
    	try {
    		if (getStatus()) 	{server.close(); }
    	
    		else{
    			System.err.println("Server-Client connection was already closed before this method was called");;
    		}
    	}
    	catch (IOException e){
    		e.printStackTrace();
    	}
    }
    
    public void parseString(String data){
    	int index = data.indexOf(',');
    	packetNumber = Long.parseLong(data.substring(0, index - 1));
    	pixelsAway = Long.parseLong(data.substring(index + 1, data.length() - 1));
    }
    
    public long getPixelsAway(){
    	return pixelsAway;
    }
   
    

}

