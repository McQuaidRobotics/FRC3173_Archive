/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 * A simple toggle which is toggled by a digital input, such as a button.
 * Each time the button is pressed, the state is toggled. Every time through an
 * event loop, etc, call input() with the state of the digital input. Then when
 * you need to check whether the toggle is on or off, just get() it.
 * @author wolf
 */
public class Toggle {
	private boolean lastInput, output;
	/**
	 * Give the Toggle the input so that it can change when necessary. Call this
	 * periodically, for example in every iteration of a main event loop, so
	 * the Toggle can catch the state change.
	 * @param input The state of the input
	 */
	public void input(boolean input) {
		if (lastInput != input) {
			if (input) {
				output=!output;
			}
			lastInput=input;
		}
	}
	/**
	 * Get the current position of the toggle.
	 * @return The state of the toggle.
	 */
	public boolean get() {
		return output;
	}
	/**
	 * Set the current position of the toggle.
	 * @param to The state to set the toggle to.
	 */
	public void set(boolean to) {
		output=to;
	}
}
