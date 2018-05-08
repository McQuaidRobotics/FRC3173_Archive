/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * A replacement for CANJaguar which attempts to resurrect any dead Jaguars
 * automagically. Jaguars can be uninstantiated in the middle of a match if one
 * of them gets accidentally turned off (eg. if a breaker trips momentarily);
 * this will catch that happening and attempt to re-instantiate the Jaguar.
 * Not all (or even most) of the methods are implemented here; if more methods
 * are needed they can be easily included by following the example of an existing
 * wrapper method. All JavaDoc for wrapper methods is copied from the CANJaguar
 * code.
 * @author Wolfgang Faust
 */
public class RobotJaguar {
	/**
	 * Whether to output debug messages about the current usage.
	 */
	private static boolean DEBUG_CURRENT = false;
	/**
	 * The Jaguar represented by this class.
	 */
	protected CANJaguar m_jaguar;
	/**
	 * The ID of the CANJaguar represented by this class (used to re-instantiate).
	 */
	protected int m_id;
	public RobotJaguar (int id) {
		this(id, true);
	}
	/**
	 * Make an instance of a Jaguar motor controller which can be resurrected.
	 * @param id The jaguar's ID on the CAN bus.
	 * @param slowRamp Whether the ramping should be fast or slow.
	 */
	public RobotJaguar (int id, boolean slowRamp) {
		m_id = id;
		instantiate();
		if (slowRamp) {
			setVoltageRampRate(48);
		} else {
			setVoltageRampRate(192);
		}
	}
	/**
	 * Handle a CANTimeoutException by attempting to reinstantiate the Jaguar
	 * @param e The original CANTimeoutException that caused jaguarErr() to be called.
	 */
	protected void jaguarErr(CANTimeoutException e) {
		Log.out.println("Exception on Jaguar ID "+m_id+", attempting to reinstantiate.");
		if (!instantiate()) {
			Log.out.println("Failed to instantiate. Original exception:");
			e.printStackTrace();
		}
	}
	/**
	 * Attempt to (re-?)instantiate the Jaguar, printing the Exception if it fails.
	 * @return Whether the Jaguar was successfully instantiated.
	 */
	protected boolean instantiate() {
		try {
			m_jaguar = new CANJaguar(m_id);
		} catch (CANTimeoutException e) {
			/// *** IMPORTANT: Do NOT call jaguarErr()!!!
			/// Doing so may cause an infinite loop!
			Log.out.println("Could not instantiate jaguar id "+m_id);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
     * Set the output set-point value.
     *
     * The scale and the units depend on the mode the Jaguar is in.
     * In PercentVbus Mode, the outputValue is from -1.0 to 1.0 (same as PWM Jaguar).
     * In Voltage Mode, the outputValue is in Volts.
     * In Current Mode, the outputValue is in Amps.
     * In Speed Mode, the outputValue is in Rotations/Minute.
     * In Position Mode, the outputValue is in Rotations.
     *
     * @param outputValue The set-point to sent to the motor controller.
     */

	public void setX(double speed) {
		try {
			m_jaguar.setX(speed);
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
	}
	/**
     * Get the status of the forward limit switch.
     *
     * @return The motor is allowed to turn in the forward direction when true.
     */
	public boolean getForwardLimitOK () {
		try {
			return m_jaguar.getForwardLimitOK();
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		return false;
	}
   /**
     * Get the status of the reverse limit switch.
     *
     * @return The motor is allowed to turn in the reverse direction when true.
     */
	public boolean getReverseLimitOK () {
		try {
			return m_jaguar.getReverseLimitOK();
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		return false;
	}
	public void changeControlMode(CANJaguar.ControlMode mode) {
		try {
			m_jaguar.changeControlMode(mode);
			return;
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		try {
			m_jaguar.changeControlMode(mode);
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
	}
	public void setPositionReference(CANJaguar.PositionReference ref) {
		try {
			m_jaguar.setPositionReference(ref);
			return;
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		try {
			m_jaguar.setPositionReference(ref);
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
	}
	public void configEncoderCodesPerRev(int lines) {
		try {
			m_jaguar.configEncoderCodesPerRev(lines);
			return;
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		try {
			m_jaguar.configEncoderCodesPerRev(lines);
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
	}
	public void setPID(double progressive, double integral, double differential) {
		try {
			m_jaguar.setPID(progressive, integral, differential);
			return;
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		try {
			m_jaguar.setPID(progressive, integral, differential);
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
	}
	public void enableControl(double encoderInitalPosition) {
		try {
			m_jaguar.enableControl(encoderInitalPosition);
			return;
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		try {
			m_jaguar.enableControl(encoderInitalPosition);
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
	}
	public void enableControl() {
		try {
			m_jaguar.enableControl();
			return;
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		try {
			m_jaguar.enableControl();
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
	}
	public void configNeutralMode(CANJaguar.NeutralMode mode) {
		try {
			m_jaguar.configNeutralMode(mode);
			return;
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		try {
			m_jaguar.configNeutralMode(mode);
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
	}
	public double getPosition(){
		try{
			return m_jaguar.getPosition();
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
				try{
			return m_jaguar.getPosition();
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
			return 0;

	}
    /**
     * Get the recently set outputValue setpoint.
     *
     * The scale and the units depend on the mode the Jaguar is in.
     * In PercentVbus Mode, the outputValue is from -1.0 to 1.0 (same as PWM Jaguar).
     * In Voltage Mode, the outputValue is in Volts.
     * In Current Mode, the outputValue is in Amps.
     * In Speed Mode, the outputValue is in Rotations/Minute.
     * In Position Mode, the outputValue is in Rotations.
     *
     * @return The most recently set outputValue setpoint.
     */
	public double getX() {
		try {
			return m_jaguar.getX();
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		// Retry on failure
		try {
			return m_jaguar.getX();
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		return 0; // Falls back to 0
	}
    /**
     * Get the current through the motor terminals of the Jaguar.
     *
     * @return The output current in Amps.
     */
	public double getOutputCurrent () {
		try {
			return m_jaguar.getOutputCurrent();
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		// Retry on failure
		try {
			return m_jaguar.getOutputCurrent();
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		// No luck, return a bogus value.
		return -1;
	}
	public void setVoltageRampRate(double rampRate) {
		try {
			m_jaguar.setVoltageRampRate(rampRate);
			return;
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		try {
			m_jaguar.setVoltageRampRate(rampRate);
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
	}
	public CANJaguar.ControlMode getControlMode() {
		try {
			return m_jaguar.getControlMode();
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		try {
			return m_jaguar.getControlMode();
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		return null;
	}
	public void configMaxOutputVoltage(double voltage) {
		try {
			m_jaguar.configMaxOutputVoltage(voltage);
			return;
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
		try {
			m_jaguar.configMaxOutputVoltage(voltage);
		} catch (CANTimeoutException e) {
			jaguarErr(e);
		}
	}
}
