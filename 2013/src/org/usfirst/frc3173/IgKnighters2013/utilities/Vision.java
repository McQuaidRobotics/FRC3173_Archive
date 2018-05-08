/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc3173.IgKnighters2013.utilities;

import edu.wpi.first.wpilibj.networktables2.stream.IOStream;
import edu.wpi.first.wpilibj.networktables2.stream.SocketStreams;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Interface with the vision processing system on the Pi.
 * Currently, just a lot of stub methods that don't do anything; however, the
 * API itself should be reasonably stable, barring unexpected developments.
 * @todo make it work
 * @author Wolfgang Faust
 */
public class Vision {
	protected VisionDataStream vds;
	public Vision() {
		vds=new VisionDataStream();
		new Thread(vds).start();
	}
	/**
	 * Get the position of the target at this moment in time.
	 * @return The position of the target at this moment in time.
	 * @throws org.usfirst.frc3173.IgKnighters2013.utilities.Vision.NoTargetException when the target cannot be tracked
	 */
	public Position getPosition() throws NoTargetException {
		return new Position(vds.getData());
	}
	/**
	 * The position of a target at one moment in time.
	 */
	protected class Position {
		protected double distance, offsetH, offsetV;
		protected Position(double[] data) throws NoTargetException {
			this(data[0],data[1],data[2]);
		}
		protected Position(double distance, double offsetH, double offsetV) throws NoTargetException {
			if (distance < 0) throw new NoTargetException("Distance reported was < 0");
			this.distance=distance;
			this.offsetH=offsetH;
			this.offsetV=offsetV;
		}
		/**
		 * @return the distance (in feet) from the camera to the target.
		 */
		public double getDistance() {
			return this.distance;
		}
		/**
		 * Get the offset (in degrees) of the left of the target relative to the
		 * center of the image, horizontally. Left is positive, right is negative.
		 * @return The angle of the offset.
		 */
		public double getHorizontalOffset() {
			return this.offsetH;
		}
		/**
		 * Get the offset of the top of the target relative to the position it
		 * ought to be in (vertical).
		 * @return The angle of the offset
		 */
		public double getVerticalOffset() {
			return this.offsetV;
		}
	}
	/**
	 * Thrown when the target disappears from the vision system, and can no
	 * longer be tracked.
	 */
	protected class NoTargetException extends Exception {
		public NoTargetException(String e) {
			super(e);
		}
	}
	/**
	 * A stream of data from the Pi, giving data about the target.
	 * Really should be UDP/IP, but apparently FRC Java doesn't support that.
	 */
	protected class VisionDataStream implements Runnable {
		/**
		 * The network stream.
		 */
		protected IOStream stream;
		/**
		 * The input stream, derived from IOStream stream.
		 */
		protected DataInputStream input;
		/**
		 * The latest data.
		 */
		protected double distance, offsetH, offsetV;
		/**
		 * A flag indicating whether new data is available since the last call to getData().
		 * Must be an object so that it can be notified and waited on.
		 */
		boolean newData=false;
		/**
		 * (Re-)connect to the Pi if disconnected.
		 */
		public void reconnect() {
			if (stream != null) stream.close();
			System.out.println("Reconnect");
			try {
				stream = SocketStreams.newStreamFactory("10.31.73.15", 3173).createStream();
				if(stream==null)
						return;
				input = new DataInputStream(new BufferedInputStream(stream.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
				stream.close();
			}
		}
		/**
		 * Loop which reads the data.
		 */
		public void run() {
			byte header;
			reconnect();
			while (true) {
				synchronized(this) {
					try {
						header=input.readByte();
						if (header != 0x42) {
							throw new IOException("Bad header! Expected: 0x42 Got: 0x"+Integer.toHexString(header));
						}
						distance=input.readDouble();
						offsetH=input.readDouble();
						offsetV=input.readDouble();
						newData=true;
						this.notify();
					} catch (IOException ex) {
						ex.printStackTrace();
						reconnect();
					}
				}
			}
		}
		/**
		 * Get the latest data from the stream.
		 * @return the data as an array, direct from the stream.
		 * @throws org.usfirst.frc3173.IgKnighters2013.utilities.Vision.NoTargetException If the data is stale and can't be refreshed within 1 second.
		 */
		public double[] getData() throws NoTargetException {
			synchronized (this) {
				if (newData == false) {
					try {
						this.wait(1000);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
						throw new NoTargetException("getData() interrupted");
					}
				}
				if (newData == false) {
					throw new NoTargetException("No new data after 1000 ms");
				}
				newData = false;
				// For some reason Java doesn't like it if I create the array in the return statement
				double[] ret = {distance, offsetH, offsetV};
				return ret;
			}
		}
	}
}
