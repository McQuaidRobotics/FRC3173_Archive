package org.usfirst.frc3173.IgKnighters2013.utilities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * A generic interface for CANJaguars, both live and dummy.
 * @author Wolfgang Faust
 */
public interface CANJaguar {
	public void setX(double speed);
	public boolean getForwardLimitOK ();
	public boolean getReverseLimitOK ();
	public void changeControlMode(edu.wpi.first.wpilibj.CANJaguar.ControlMode mode);
	public void setPositionReference(edu.wpi.first.wpilibj.CANJaguar.PositionReference ref);
        public void setSpeedReference(edu.wpi.first.wpilibj.CANJaguar.SpeedReference ref);
	public void configEncoderCodesPerRev(int lines);
	public void setPID(double progressive, double integral, double differential);
	public double getP();
	public double getI();
	public double getD();
        public double getSpeed();
	public void configNeutralMode(edu.wpi.first.wpilibj.CANJaguar.NeutralMode mode);
	public double getPosition();
	public double getX();
	public double getOutputCurrent();
	public void setVoltageRampRate(double rampRate);
	public void configMaxOutputVoltage(double voltage);
	public void enableControl(double encoderInitalPosition);
	public void enableControl();
	public void disableControl();
	public boolean getMode();
}
