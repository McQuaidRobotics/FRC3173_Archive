/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A static logfile which prints both to System.out and a RobotLog, if possible.
 * Instead of using System.out, use Log.out. This logs the message to a file and
 * prints it in real-time simultaneously for debugging.
 * @author wolf
 */
public class Log extends PrintStream {
	public static final Log out = new Log();
	private static final String LOGFILE="/tmp/log.txt";
	private static OutputTee tee;
	private Log() {
		/* This hocus-pocus is so the IOException coming from RobotLog can be
		 * caught when calling from super. After super is called, the OutputStream
		 * that the PrintStream prints to can't be changed, so instead we give
		 * OutputTee a null Output so it can go somewhere, then try to change
		 * streamB of the tee to a RobotLog. If RobotLog doesn't work, the
		 * second stream will simply go harmlessly down the drain. */
		super(tee = new OutputTee(System.out,new OutputNull()));
		try {
			tee.setStreams(tee.getStreamA(), new RobotLog(LOGFILE));
		} catch (IOException e) {
			System.out.println("Error: Couldn't write to the RobotLog ("+LOGFILE+")");
			e.printStackTrace();
		}
	}
}
