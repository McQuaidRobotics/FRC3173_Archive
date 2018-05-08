/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc3173.IgKnighters2013.utilities;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Control the state of the LEDs via the Pi.
 * Creates a NetworkTable on the robot which the LED daemon on the Pi connects
 * to and reads from.
 * @author Wolfgang Faust
 */
public class Blinky {
	/**
	 * The NetworkTable containing the LED states. Each LED is a boolean pair in
	 * the table, named led# (where # is the id of the LED)
	 */
	protected NetworkTable table=NetworkTable.getTable("leds");
	/**
	 * How many LEDs are available. Change this number if more LEDs are added, etc.
	 */
	public static final int length = 1;
	/**
	 * Puts the pairs in the table, setting all lights to off by default.
	 */
	public Blinky(){
		for (int i = 0; i < Blinky.length; i++) {
			setLed(i,false);
		}
	}
	/**
	 * Turn an LED on or off.
	 * @param led The number of the LED
	 * @param to  the state to set it to (true=on)
	 */
	public void setLed(int led, boolean to) {
		if (led < 0 || led >= this.length) {
			System.out.println("LED index out of bounds: "+led+" >= "+this.length);
			return;
		}
		table.putBoolean("led"+led, to);
	}
}
