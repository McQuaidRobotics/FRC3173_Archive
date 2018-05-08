/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Drives subsystem.
 * The code in IgKnightersBot was becoming larger and larger, so I moved it all
 * here.
 * @author wolf
 */
public class Drives {
	/**
	 * The drive jaguars which control the motors.
	 */
	protected RobotJaguar frontLeftDriveJaguar, backLeftDriveJaguar, frontRightDriveJaguar, backRightDriveJaguar;
	protected Solenoid brakesOnSolenoid, brakesOffSolenoid;
	protected Relay brakesOnRelay, brakesOffRelay;
	protected Toggle pneumaticBrakeToggle,activeBrakeToggle;
	public static final int STATE_DRIVING = 0, STATE_PNEUMATIC = 1, STATE_COASTING = 2, STATE_APPLYING = 3, STATE_BRAKED = 4;
	/**
	 * Whether braking mode is currently active.
	 */
	protected boolean m_braking = false, m_lastPneumaticState;
	protected int pneumaticBrakeCount;
	protected int m_state;
	protected long m_time;
	protected static final long M_COAST_TIME = 100; // Time to coast when braking
	protected static final long M_BRAKE_WAIT = 200; // Time the robot must stay still
	/**
	 * Instantiate all of the drive jaguars.
	 */
	public Drives() {
		frontLeftDriveJaguar = instantiateJaguar(ElectricalIDs.CANJaguars.FORWARD_LEFT_DRIVE_JAGUAR, true);
		backLeftDriveJaguar = instantiateJaguar(ElectricalIDs.CANJaguars.BACK_LEFT_DRIVE_JAGUAR, true);
		frontRightDriveJaguar = instantiateJaguar(ElectricalIDs.CANJaguars.FORWARD_RIGHT_DRIVE_JAGUAR, false);
		backRightDriveJaguar = instantiateJaguar(ElectricalIDs.CANJaguars.BACK_RIGHT_DRIVE_JAGUAR, false);
		if (ElectricalIDs.pneumatic_solenoid) {
			brakesOnSolenoid=new Solenoid(ElectricalIDs.Solenoid.PNEUMATIC_BRAKES_ON);
			brakesOffSolenoid=new Solenoid(ElectricalIDs.Solenoid.PNEUMATIC_BRAKES_OFF);
		} else {
			brakesOnRelay=new Relay(ElectricalIDs.Relay.PNEUMATIC_BRAKES_ON);
			brakesOnRelay.setDirection(Relay.Direction.kForward);
			brakesOffRelay=new Relay(ElectricalIDs.Relay.PNEUMATIC_BRAKES_OFF);
			brakesOffRelay.setDirection(Relay.Direction.kForward);
		}
		pneumaticBrakeToggle=new Toggle();
		activeBrakeToggle=new Toggle();
		setState(STATE_DRIVING);
	}
	/**
	 * Instantiate a RobotJaguar with the correct settings for active braking.
	 * @param id The ID of the Jaguar on the CAN bus.
	 * @param leftSide True if this jaguar is on the left side of the robot, false if it is on the right.
	 * @return An instantiated and configured Jaguar.
	 */
	private RobotJaguar instantiateJaguar(int id,boolean leftSide) {
		RobotJaguar jaguar = new RobotJaguar(id,false);
		configJaguar(jaguar);
		return jaguar;
	}
	private void configJaguar(RobotJaguar jaguar) {
		jaguar.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
		jaguar.configEncoderCodesPerRev(360);
		jaguar.configNeutralMode(CANJaguar.NeutralMode.kBrake);
		jaguar.setPID(1000,.2,20);
		jaguar.enableControl(jaguar.getPosition());
	}
	/**
	 * Tank drive with some extra features. Adds various buttons to change the
	 * maximum speed, reverse the forward end of the robot, and only drive in a
	 * straight line using the right joystick (ignoring the left one).
	 * @param leftStick The left joystick.
	 * @param rightStick The right joystick.
	 */
	public void sensitiveTankDrive(Joystick leftStick, Joystick rightStick) {
		pneumaticBrakeToggle.input(rightStick.getTrigger());
		// Active brake always disabled
		//activeBrakeToggle.input(rightStick.getRawButton(11));
		boolean stopped = (Math.abs(leftStick.getY())+Math.abs(rightStick.getY())) < 0.005;
		if (pneumaticBrakeToggle.get()) {
			stopped = true;
			setState(STATE_PNEUMATIC);
		}
		if (stopped != isBraking() && !pneumaticBrakeToggle.get()) { // State change
			if (stopped && activeBrakeToggle.get()) setState(STATE_COASTING);
			if (!stopped) setState(STATE_DRIVING);
		}
		if (m_state == STATE_PNEUMATIC && !pneumaticBrakeToggle.get()) setState(STATE_DRIVING);
		IgKnightersBot.profile("Drives: FSM state transitions");
		if (m_state == STATE_PNEUMATIC) {
			if (!m_lastPneumaticState) pneumaticBrakeCount++;
			if (ElectricalIDs.pneumatic_solenoid) {
				brakesOnSolenoid.set(true);
				brakesOffSolenoid.set(false);
			} else {
				brakesOnRelay.set(Relay.Value.kOn);
				brakesOffRelay.set(Relay.Value.kOff);
			}
			leftRightDrive(0,0,0);
			IgKnightersBot.profile("Drives: Pneumatic");
		} else {
			if (ElectricalIDs.pneumatic_solenoid) {
				brakesOnSolenoid.set(false);
				brakesOffSolenoid.set(true);
			} else {
				brakesOnRelay.set(Relay.Value.kOff);
				brakesOffRelay.set(Relay.Value.kOn);
			}
			IgKnightersBot.profile("Drives: Pneumatic disable");
			if (m_state == STATE_DRIVING) {
				double driveSensitivity;
				if (leftStick.getRawButton(3)) {
					driveSensitivity = 0.30;
				} else if (leftStick.getRawButton(5)) {
					driveSensitivity = 1;
				} else {
					driveSensitivity = 0.60;
				}
				IgKnightersBot.profile("Drives: Joystick read");
				if (!leftStick.getRawButton(2)) {
					if (leftStick.getTrigger()) {
						leftRightDrive(rightStick.getY(),-leftStick.getY(),driveSensitivity);
					} else {
						leftRightDrive(-leftStick.getY(),rightStick.getY(), driveSensitivity);
					}
				} else {
					if (leftStick.getTrigger()) {
						leftRightDrive(rightStick.getY(),-rightStick.getY(),driveSensitivity);
					} else {
						leftRightDrive(-rightStick.getY(),rightStick.getY(), driveSensitivity);
					}
				}
			}
			IgKnightersBot.profile("Drives: STATE_DRIVING");
			if (m_state == STATE_COASTING) {
				leftRightDrive(0,0,0);
				// Reset the encoder positions to 0
				frontLeftDriveJaguar.enableControl(0);
				backLeftDriveJaguar.enableControl(0);
				frontRightDriveJaguar.enableControl(0);
				backRightDriveJaguar.enableControl(0);
				frontLeftDriveJaguar.configMaxOutputVoltage(9.8); // 70%
				backLeftDriveJaguar.configMaxOutputVoltage(9.8);
				frontRightDriveJaguar.configMaxOutputVoltage(9.8);
				backRightDriveJaguar.configMaxOutputVoltage(9.8);
				if (System.currentTimeMillis() >= m_time + M_COAST_TIME) setState(STATE_APPLYING);
			}
			IgKnightersBot.profile("Drives: COASTING");
			if (m_state == STATE_APPLYING) {
				// TODO apply until haven't moved for time
				if (System.currentTimeMillis() >= m_time + M_BRAKE_WAIT) {
					if (Math.abs(frontLeftDriveJaguar.getPosition()) < 1 && Math.abs(frontRightDriveJaguar.getPosition()) < 1) {
						setState(STATE_BRAKED);
					}
					setBrake(true);
					m_time = System.currentTimeMillis();
				}
			}
			if (m_state == STATE_BRAKED) {
				// No need to do anything here; the brakes were already applied during STATE_APPLYING
			}
			if (isBraking()) leftRightDrive(0,0,0);
		}
		m_lastPneumaticState = m_state == STATE_PNEUMATIC;
		IgKnightersBot.profile("Drives done");
	}
	/**
	 * Run the left and right sides of the drive mechanism, with a maximum speed
	 * boundary, applying braking when stopped.
	 * The left & right parameters are relative to this maximum.
	 * @param left The speed for the left side of the drive system.
	 * @param right The speed for the left side of the drive system.
	 * @param maxSpeed The maximum speed for the drives.
	 */
	protected void leftRightDrive(double left, double right, double maxSpeed) {
		//boolean stopped = (Math.abs(left)+Math.abs(right))*maxSpeed < 0.005;
		frontLeftDriveJaguar.setX(left * maxSpeed);
		backLeftDriveJaguar.setX(left * maxSpeed);
		frontRightDriveJaguar.setX(right * maxSpeed);
		backRightDriveJaguar.setX(right * maxSpeed);
		IgKnightersBot.profile("Drives: LeftRightDrive()");
	}
	protected void setBrake (boolean brake) {
		CANJaguar.ControlMode controlMode;
		if (brake && activeBrakeToggle.get()) { // Apply brakes
			controlMode=CANJaguar.ControlMode.kPosition;
			Log.out.println("Brakes applied.");
		} else { // Release brakes
			controlMode=CANJaguar.ControlMode.kPercentVbus;
			Log.out.println("Brakes released.");
		}
		m_braking=brake;
		setControlMode(frontLeftDriveJaguar,controlMode);
		//setControlMode(backLeftDriveJaguar,controlMode);
		setControlMode(frontRightDriveJaguar,controlMode);
		//setControlMode(backRightDriveJaguar,controlMode);
	}
	protected void setControlMode (RobotJaguar jaguar, CANJaguar.ControlMode controlMode) {
		jaguar.changeControlMode(controlMode);
		configJaguar(jaguar);
	}
	/**
	 * Stop the drives.
	 */
	public void halt() {
		leftRightDrive(0,0,0);
	}
	private void setState(int state) {
		Log.out.println("Drives state: "+state);
		if (state == STATE_DRIVING) {
			setBrake(false);
			frontLeftDriveJaguar.configMaxOutputVoltage(14); // 100%
			backLeftDriveJaguar.configMaxOutputVoltage(14);
			frontRightDriveJaguar.configMaxOutputVoltage(14);
			backRightDriveJaguar.configMaxOutputVoltage(14);
		} else if (state == STATE_APPLYING) {
			setBrake(true);
		}
		m_state = state;
		m_time = System.currentTimeMillis();
	}

	private boolean isBraking() {
		return m_state != 0;
	}
}
