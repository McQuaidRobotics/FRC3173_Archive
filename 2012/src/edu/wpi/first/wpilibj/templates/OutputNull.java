/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An OutputStream whose output goes nowhere. Useful when an OutputStream is
 * need as a parameter, but the output isn't.
 * @author wolf
 */
public class OutputNull extends OutputStream {

	public void write(int b) throws IOException {
		// Do nothing
	}

}
