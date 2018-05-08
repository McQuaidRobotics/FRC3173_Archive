package org.usfirst.frc3173.IgKNIGHTers.subsystems;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Vision extends Subsystem {
	private final String HOST = "localhost"; //to change
	private final int PORT = 1185;
	private DataInputStream dIn;
	private Socket sock; 
	private double ratio;
	private int distance;
	
	/*
	 * @author Jonathan Abbott
	 * 
	 * This subsystem serves as a gateway to retrieve information from a host
	 * this year, specifically the Jetson Tx1. Implementing getter methods,
	 * this boosts efficiency of autonomous and assists tele-op.
	 * 
	 * TODO make this work, i guess. test? maybe. lemme try later.
	 * 
	 * NOTE TO SELF - NEVER USE WHILE LOOPS OR FOR LOOPS.
	 */
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	/*
	 * Connects to default host as listed above.
	 */
	public void connect() {
		//TODO have to change the reaction to the exceptions.
		try {
			sock = new Socket(HOST, PORT);
			dIn = new DataInputStream(sock.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * @return
	 * A boolean of whether or not the socket is connected to the host.
	 */
	public boolean isConnected() {
		return sock.isConnected();
	}
	
	/*
	 * @purpose
	 * Updates information from the TCP connection.
	 * 
	 * 
	 */
	public void update() {
		//TODO have to change the reaction to IOException.
		try {
			if (isConnected()) {
				ratio = dIn.readDouble();
				distance = dIn.readInt();
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * @return 
	 * A positive double of the ratio of the two heights of tapes, if connected.
	 * Ranges inclusively on interval (0,2), if connected.
	 */
	public double getRatio() {
		if (isConnected())
			return ratio;
		else
			return -1.0;
	}
	
	/*
	 * @precondition
	 * Error is minimized when viewing target straight-on, or viewing
	 * angle is zero degrees.
	 * 
	 * @return
	 * A positive integer of an estimated distance to the tape, if connected.
	 */
	public int getDistance() {
		if (isConnected())
			return distance;
		else
			return -1;
	}
}
