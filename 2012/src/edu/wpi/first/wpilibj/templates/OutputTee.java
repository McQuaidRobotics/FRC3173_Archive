/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An OutputStream which outputs to two separate OutputStreams. Useful when
 * output needs to go to two different places (for example, System.out and a file.)
 * @author wolf
 */
public class OutputTee extends OutputStream {
	protected OutputStream streamA, streamB;
	public OutputTee(OutputStream a, OutputStream b) {
		streamA=a;
		streamB=b;
	}
	public void write(int b) throws IOException {
		streamA.write(b);
		streamB.write(b);
	}
	public void close() throws IOException {
		streamA.close();
		streamB.close();
	}
	public void flush() throws IOException {
		streamA.flush();
		streamB.flush();
	}
	public void setStreams(OutputStream a, OutputStream b) {
		streamA=a;
		streamB=b;
	}
	public OutputStream getStreamA() {
		return streamA;
	}
	public OutputStream getStreamB() {
		return streamB;
	}
}
