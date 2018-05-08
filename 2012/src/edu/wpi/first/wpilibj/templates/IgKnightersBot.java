package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotCamera.*;
import java.io.*;

/**
 * The main class, these methods are called while the robot is running.
 * GYRO DRifT = 0.005518168°/ms
 * @author IgKnighters
 */
public class IgKnightersBot extends SimpleRobot {

	public static final int SHOOTER_MANUAL_SPEED_MAX = 75;
	public static final int SHOOTER_MANUAL_SPEED_MIN = 5;
	public static final double SHOOTER_AUTONOMOUS_SPEED = 0.55;
	/**
	 * The speed at which drives should be run during autonomous.
	 */
	public static final double AUTONOMOUS_SPEED = -0.5;
	public static final double AUTONOMOUS_ROTATION_SPEED = -0.6;

	public static final double HYBRID_MAX_SPEED = .50;

	/**
	 * Static block to warn about the fact of robots not quitting.
	 */
	static {
		Log.out.println("WARNING: Robots don't quit!");
	}
	/**
	 * Subsystems with classes written by us.
	 */
	Storage storage;
	Shooter shooter;
	Drives drives;
	Acquisition acquisition;
	ProximitySensor proximitySensor;
	/**
	 * Subsystems with classes written by FIRST.
	 */
	DigitalInput acquisitionInput, lowerStorageInput, upperStorageInput, autonomousEnabledSwitch, autonomousModeSwitch, hybridModeSwitch;
	Gyro horizontalGyro;//, verticalGyro;
	Relay signalLight1, signalLight2, lightSensors, cameraLights;
	Compressor compressor;
	/**
	 * HIDs with classes written by us.
	 */
	/**
	 * GamePad is static so it can be used in manualStop().
	 */
	static GamePad gamePad = new GamePad(ElectricalIDs.HIDs.GAMEPAD);
        Tilter tilter;
	/**
	 * HIDs with classes written by FIRST.
	 */
	Joystick leftStick, rightStick;
	KinectStick leftKinectStick, rightKinectStick;
	/**
	 * Utilities written by us.
	 */
	DriverStationOutput driverStation;
	DriverStation ds = DriverStation.getInstance();
	/**
	 * Instance variables of primitive data types.
	 */
	int ballCount;
	/**
	 * True if an exception occurred & everything should stop.
	 */
	static boolean halted = false;

	/**
	 * Called once when the robot starts up.
	 */
	public void robotInit() {
//		SmartDashboard.putString("Status", "Initializing...");
		driverStation = new DriverStationOutput();
		driverStation.println(0, "Initializing...");
		getWatchdog().setEnabled(false);
		//////   L I G H T S   //////
		signalLight1 = new Relay(ElectricalIDs.Relay.SIGNAL_LIGHT_ONE);
		signalLight1.setDirection(Relay.Direction.kForward);
		signalLight2 = new Relay(ElectricalIDs.Relay.SIGNAL_LIGHT_TWO);
		signalLight2.setDirection(Relay.Direction.kForward);
		lightSensors = new Relay(ElectricalIDs.Relay.LIGHT_SENSORS);
		lightSensors.setDirection(Relay.Direction.kForward);
		cameraLights = new Relay(ElectricalIDs.Relay.CAMERA_LIGHT);
		cameraLights.setDirection(Relay.Direction.kForward); // Set direction on relay for lights
		//////   D I G I T A L   I N P U T S   //////
		autonomousEnabledSwitch = new DigitalInput(ElectricalIDs.DigitalIn.AUTONOMOUS_IS_ENABLED);
		autonomousModeSwitch = new DigitalInput(ElectricalIDs.DigitalIn.AUTONOMOUS_IS_HYBRID);
		hybridModeSwitch = new DigitalInput(ElectricalIDs.DigitalIn.HYBRID_MODE);
		acquisitionInput = new DigitalInput(ElectricalIDs.DigitalIn.ACQUISITION_IN);
		lowerStorageInput = new DigitalInput(ElectricalIDs.DigitalIn.LOWER_STORAGE_IN);
		upperStorageInput = new DigitalInput(ElectricalIDs.DigitalIn.UPPER_STORAGE_IN);
		//////   H U M A N   I N P U T   D E V I C E S   //////
		leftStick = new Joystick(ElectricalIDs.HIDs.LEFT_JOYSTICK);
		rightStick = new Joystick(ElectricalIDs.HIDs.RIGHT_JOYSTICK);
		leftKinectStick = new KinectStick(ElectricalIDs.HIDs.LEFT_KINECT_STICK);
		rightKinectStick = new KinectStick(ElectricalIDs.HIDs.RIGHT_KINECT_STICK);
		//////   M I S C   S E N S O R S   //////
		horizontalGyro = new Gyro(ElectricalIDs.AnalogIn.HORIZONTAL_GYRO);
		//verticalGyro = new Gyro(ElectricalIDs.AnalogIn.VERTICAL_GYRO);
		proximitySensor = new ProximitySensor(horizontalGyro);
		//////   S U B S Y S T E M S   //////
		compressor = new Compressor(ElectricalIDs.DigitalIn.COMPRESSOR_PRESSURE,ElectricalIDs.Relay.COMPRESSOR);
		drives=new Drives();
		shooter = new Shooter(cameraLights, drives);
		storage = new Storage(acquisitionInput, lowerStorageInput, upperStorageInput);
		acquisition = new Acquisition();
		tilter = new Tilter();
		
		Log.out.println(">>> INITIALIZED");
	}

	/**
	 * Called when the robot enters disabled.
	 */
	public void disabled() {
		driverStation.println(0, "Ready To Enable");
//		SmartDashboard.putString("Status", "Disabled");
		Log.out.println(">>> DISABLED");
	}

	/**
	 * Kinetics-control if autonomouse mode is enable.
	 * @param shooting Whether we are shooting or tilting
	 */
	public void hybrid(boolean shooting) {
		int i;
		String kinectButtonText;
		driverStation.println(0, "Mode: Hybrid");
//		SmartDashboard.putString("Status", "Hybrid ("+(shooting?"":"not ")+"shooting)");
		Log.out.println(">>> HYBRID");
		// TODO Implement Kinect Gestures
		while (isAutonomous() && isEnabled()) {
			// Display the Kinect buttons
			kinectButtonText = "Buttons: ";
			for (i=1;i<10;i++) {
				if (leftKinectStick.getRawButton(i)) {
					kinectButtonText += i;
				} else {
					kinectButtonText += " ";
				}
			}
			driverStation.println(2,kinectButtonText);
			if (shooting && leftKinectStick.getRawButton(KinectButton.RIGHT_LEG_SIDE)) { // Shoot mode
				cameraLights.set(Relay.Value.kOn);
				shooter.setSpeed(SHOOTER_AUTONOMOUS_SPEED);
				if (rightKinectStick.getY() < 0) {
					storage.startPulleyMove(1,1f);
				} else {
					storage.stopPulleyMove();
				}
				drives.halt();
			} else { // Drive mode
				if (!shooting && leftKinectStick.getRawButton(KinectButton.RIGHT_LEG_SIDE)) {
					tilter.tiltOut();
				} else {
					tilter.tiltIn();
				}
				tilter.doTilting();
				shooter.stop();
				cameraLights.set(Relay.Value.kOff);
				drives.leftRightDrive(-leftKinectStick.getY(),rightKinectStick.getY(), HYBRID_MAX_SPEED);
				acquisition.startAcquisition();
				storage.stopPulleyMove();
			}
		}
	}

	public void automatic() {
		double t;
		driverStation.println(0, "Mode: Automatic");
//		SmartDashboard.putString("Status", "Automatic");
		Log.out.println(">>> AUTOMATIC");
		t=System.currentTimeMillis();
		while (isEnabled() && isAutonomous() && (System.currentTimeMillis() < t + 8000)) {
			shooter.setSpeed(SHOOTER_AUTONOMOUS_SPEED);//percent speed; may need to change if ball overshoots
			Timer.delay(.05);
			driverStation.println(1,""+shooter.motorCurrent());
		}
		t=System.currentTimeMillis();
		while (isEnabled() && isAutonomous() && (System.currentTimeMillis() < t + 6000)) {
			shooter.setSpeed(SHOOTER_AUTONOMOUS_SPEED);//percent speed; may need to change if ball overshoots
			storage.startPulleyMove(1, .45f);
			Timer.delay(.05);
			driverStation.println(1,""+shooter.motorCurrent());
			driverStation.println(2,""+storage.motorCurrent());
		}
		shooter.stop();// Don't run shooter anymore, we don't need it
		storage.stopPulleyMove();
		// Tilt the bridge
		// BUT only straight-on
		// @todo tilt bridge at angle

		//driverStation.println(1, "Tilting bridge");
		// Drive halfway
		/*frontLeftDriveJaguar.setX(AUTONOMOUS_SPEED);
		backLeftDriveJaguar.setX(AUTONOMOUS_SPEED);
		frontRightDriveJaguar.setX(-AUTONOMOUS_SPEED);
		backRightDriveJaguar.setX(-AUTONOMOUS_SPEED);
		Timer.delay(3);

		horizontalGyro.reset();
		// Turn 180deg
		frontLeftDriveJaguar.setX(AUTONOMOUS_ROTATION_SPEED);
		backLeftDriveJaguar.setX(AUTONOMOUS_ROTATION_SPEED);
		frontRightDriveJaguar.setX(AUTONOMOUS_ROTATION_SPEED);
		backRightDriveJaguar.setX(AUTONOMOUS_ROTATION_SPEED);
		while (horizontalGyro.getAngle() > -180 && isAutonomous() && isEnabled()) {
			System.out.println(horizontalGyro.getAngle());
                        driverStation.println(DriverStationLCD.Line.kMain6, horizontalGyro.getAngle()+" degrees");
                       // horizontalGyro.getAngle check;
                        //it should output in degrees.
		}
                //robot noy not get t
		frontLeftDriveJaguar.setX(0);
		backLeftDriveJaguar.setX(0);
		frontRightDriveJaguar.setX(0);
		backRightDriveJaguar.setX(0);

		tilter.tiltOut();
		while (isAutonomous() && isEnabled() && !tilter.tiltedOut()) {
			tilter.doClamping();
		}

		frontLeftDriveJaguar.setX(-AUTONOMOUS_SPEED);
		backLeftDriveJaguar.setX(-AUTONOMOUS_SPEED);
		frontRightDriveJaguar.setX(AUTONOMOUS_SPEED);
		backRightDriveJaguar.setX(AUTONOMOUS_SPEED);
		Timer.delay(2);
		backLeftDriveJaguar.setX(0);
		backRightDriveJaguar.setX(0);
		frontLeftDriveJaguar.setX(0);
		frontRightDriveJaguar.setX(0);

		tilter.tiltIn();
		driverStation.println(1, "Autonomous complete. Idle.");
		driverStation.clearln(2); // Remove distance display
		while (isAutonomous() && isEnabled()) {
			tilter.doClamping();
		}*/
	}

	/**
	 * Called when the robot enters autonomous. Note that it checks the switches
	 * only once at the beginning. This is so a stray ball can't disable the
	 * robot.
	 */
	public void autonomous() {
		if (autonomousEnabledSwitch.get()) { // Autonomous enabled
			if (autonomousModeSwitch.get()) {  // Hybrid mode
				hybrid(hybridModeSwitch.get());
			} else { // Automatic
				automatic();
			}
		} else { // Autonomous disabled.
			driverStation.println(0, "Disabled during autonomous.");
			//SmartDashboard.putString("Status", "Autonomous (disabled)");
		}
	}

	/**
	 * Called when the robot enters operator control.
	 */
	public void operatorControl() {
		driverStation.println(0, "Mode: Teleoperated");
		//SmartDashboard.putString("Status", "Teleoperated");
		Log.out.println(">>> TELEOPERATED");
		compressor.start();
		lightSensors.set(Relay.Value.kOn);
		signalLight1.set(Relay.Value.kOn);
		signalLight2.set(Relay.Value.kOn);
		driverStation.autoUpdate(false);
		double manualShooterPercent = 50;
		long loopNumber = 0;
		boolean oldAcquisitionValue = false;
		boolean oldShooterValue = false;
		long oldTime;
		while (isOperatorControl() && isEnabled()) {
			profile("Begin loop");
			oldTime = System.currentTimeMillis();
			System.out.println(gamePad.getAxis(GamePad.Axis.kDPadX) + ":" + gamePad.getAxis(GamePad.Axis.kDPadY));
			System.out.println("Sensors: " + !acquisitionInput.get() + "," + !upperStorageInput.get() + "," + !lowerStorageInput.get());

			updateBallCount(oldAcquisitionValue, oldShooterValue);

			if (leftStick.getRawButton(8) && leftStick.getRawButton(9)) {
				Log.out.println("Reset gyro");
				horizontalGyro.reset();
				//verticalGyro.reset();
			}
			if (manualStop()) { // Stop tilter
				tilter.halt();
				// Automatic shooting will check inside Shooter, since this code
				// is never called during automatic shooting.
			}
			profile("Point A");
			// Manual operation
			if (!gamePad.getButton(GamePad.Button.kFrontLeftShoulder)) {
				// Manual storage
				if (gamePad.getButton(GamePad.Button.kXButton)) {
					storage.startPulleyMove(1, 1f);
				} else if (gamePad.getButton(GamePad.Button.kYButton)) {
					storage.startPulleyMove(-1, 1f);
				} else {
					storage.stopPulleyMove();
				}
				profile("Storage");
				// Manual acquisition
				if (gamePad.getButton(GamePad.Button.kAButton)) {
					acquisition.startAcquisition();
				} else if (gamePad.getButton(GamePad.Button.kBButton)) {
					acquisition.reverseAcquisition();
				} else {
					acquisition.stopAcquisition(true, false);
				}
				profile("Acquisition");
				// Manual shooter
				if (gamePad.getButton(GamePad.Button.kFrontRightShoulder)) {
					shooter.setSpeed(manualShooterPercent / 100.00);
				} else {
					shooter.setSpeed(0);
				}
				profile("Shooter");
				// Manual shooter set speed
				if (gamePad.getButton(GamePad.Button.kLeftStick)) {
					manualShooterPercent -= 5;
				}
				if (gamePad.getButton(GamePad.Button.kRightStick)) {
					manualShooterPercent += 5;
				}
				if (manualShooterPercent > SHOOTER_MANUAL_SPEED_MAX) {
					manualShooterPercent = SHOOTER_MANUAL_SPEED_MAX;
				}
				if (manualShooterPercent < SHOOTER_MANUAL_SPEED_MIN) {
					manualShooterPercent = SHOOTER_MANUAL_SPEED_MIN;
				}
				profile("Shooter set speed");
				cameraLights.set(Relay.Value.kOn);
				proximitySensor.faceWall();
				profile("Camera lights & proximity sensor");
				driverStation.println(1, "Manual Control");
				driverStation.println(3, "Distance: " + proximitySensor.getDistance());
				driverStation.println(4, "Shooter Speed: " + manualShooterPercent + "%");
				profile("End manual");
			} else {
				if (gamePad.getButton(GamePad.Button.kAButton)) {
					Log.out.println("Started acquisition & storage");
					acquisition.startAcquisition();
					storage.startPulleyMove(1, 1f);
				} else if (gamePad.getButton(GamePad.Button.kBButton)) {
					Log.out.println("Reversed acquisition & storage");
					acquisition.reverseAcquisition();
					storage.startPulleyMove(-1, 1f);
				} else{
					acquisition.stopAcquisition(true, false);
					storage.stopPulleyMove();
                }
				if (acquisition.isAcquiring()) {
					if (getWhichBallsStored() <= 2) {
						Log.out.println("Stopped acquisition based on ball positions");
						acquisition.stopAcquisition(false, !acquisitionInput.get());
					}
				}

				if (getWhichBallsStored() == 0 && !lowerStorageInput.get()) {
					Log.out.println("Moved ball up from lower storage based on ball positions and lowerStorageInput.get()");
					acquisition.enableAcquisitionStop(false);
					acquisition.startAcquisition();
					storage.trackMove(1);
				} else {
					acquisition.enableAcquisitionStop(true);
				}
				cameraLights.set(Relay.Value.kOff);
				//driverStation.clearln(1);
				//driverStation.clearln(3);
				//driverStation.clearln(4);
/*				if (rightStick.getRawButton(2)) {
					Log.out.println("Fired at bottom basket");
					fire(RobotCamera.Basket.BOTTOM);
				}
				if (rightStick.getRawButton(3)) {
					Log.out.println("Fired at top basket");
					fire(RobotCamera.Basket.TOP);
				}
				if (rightStick.getRawButton(4)) {
					Log.out.println("Fired at left basket");
					fire(RobotCamera.Basket.LEFT);
				}
				if (rightStick.getRawButton(5)) {
					Log.out.println("Fired at right basket");
					fire(RobotCamera.Basket.RIGHT);
				}*/
				profile("End automatic");
			}
			// Tilter
			if (gamePad.getButton(GamePad.Button.kBackRightShoulder)) {
				tilter.tiltOut();
			} else {
				tilter.tiltIn();
			}
			tilter.doTilting(); // Clamp if tilter is out
			profile("Tilter");
			drives.sensitiveTankDrive(leftStick, rightStick);
			profile("Driving");
			loopNumber++;
			oldAcquisitionValue = !acquisitionInput.get();
			oldShooterValue = upperStorageInput.get();
			profile("Misc statements");
			driverStation.println(DriverStationOutput.LINE_5, "Shoolter current: " + shooter.motorCurrent());
			profile("Output");
			Log.out.println(System.currentTimeMillis() - oldTime);
			profile("End loop");
                        driverStation.update();
		}
		driverStation.autoUpdate(true);
		compressor.stop();
	}

	/**
	 * Fires the ball into the hoop
	 *
	 * @param basket the RobotCamera.Basket to fire at @todo stop the shooter
	 * after firing.
	 */
	public void fire(int basket) {
		boolean shooterRunning = true;
		storage.startPulleyMove(-1, 1);
		// We don't need a Timer.delay here because shooter.startFiring should take long enough already
		try {
			shooter.startFiring(basket);
		} catch (Exception e) {
			shooterRunning = false;
			driverStation.println(DriverStationOutput.LINE_5, e.toString());
			shooter.stop();
			e.printStackTrace();
		}
		if (shooterRunning) {
			storage.toShoot();
			// TODO detect ball fired
			Log.out.println("Fired Ball");
			//decrement balls in storage
			//move balls up in storage
		}
	}
	static long time = System.currentTimeMillis();
	public static void profile(String at) {
		Log.out.println(at+": "+(System.currentTimeMillis()-time)+"ms");
		time = System.currentTimeMillis();
	}

	/**
	 * The following method will return the number of
	 * balls which have been properly stored within the robot. There may be more
	 * balls inside the robot than this method indicated because it only counts
	 * balls that are properly stored.
	 *
	 * @return Where balls are stored.
	 */
	public int getWhichBallsStored() {
		int r = 0;
		if (upperStorageInput.get()) {
			r = 1;
			if (!lowerStorageInput.get()) {
				r = 2;
				if (!acquisitionInput.get()) {
					r = 3;
				}
			}
		}
		return r;
	}

	/**
	 * Update the total number of balls within the robot at this time.
	 *
	 * @param oldAcquisitionValue
	 * @param oldShooterValue
	 */
	public void updateBallCount(boolean oldAcquisitionValue, boolean oldShooterValue) {
		if (!acquisitionInput.get() && !oldAcquisitionValue) {
			ballCount++;
		}

		if (upperStorageInput.get() && !oldShooterValue) {
			ballCount--;
		}
	}

	/**
	 * Whether a manual stop has been initiated by pressing the Gamepad start
	 * button. (Static so it can be called from other subsystems.)
	 *
	 * @return Whether subsystems should be manually stopped.
	 */
	public static boolean manualStop() {
		return gamePad.getButton(GamePad.Button.kStart) || halted;
	}
	/**
	 * Stop all subsystems.
	 */
	public void halt() {
		Log.out.println("Halted.");
		// Stop everything
		tilter.halt();
		shooter.stop();
		storage.stopPulleyMove();
		acquisition.stopAcquisition(true, false);
		drives.halt();
		compressor.stop();
	}
	/**
	 * Run the robot, calling the appropriate methods at the correct times, and
	 * catching any exceptions when they occur. This way, exceptions will not
	 * require a power-cycle. if an exception occurs, the robot will stop and
	 * all subsystems will be halted. (if an exception occurs while halting, the
	 * robot *will* need a power cycle.) The robot can be reactivated by
	 * disabling and re-enabling. Operator control can also be restarted by
	 * pressing buttons 10 & 11 on the left joystick. Exceptions during
	 * robotInit are not caught, because robotInit cannot be re-called anyway
	 * (more exceptions will be thrown as subsystems attempt to re-instantiate
	 * already instantiated variables).
	 */
	public void startCompetition() {
		// This code was taken from SimpleRobot.startCompetition() and modified to catch exceptions.
		// Display errors on the Driver Station
		//Utility.sendErrorStreamToDriverStation(true);
		robotInit();
		while (true) {
			try {
				driverStation.clearMessages();
				if (isDisabled()) {
					disabled();
					while (isDisabled()) {
						Timer.delay(0.05);
					}
				} else if (isAutonomous()) {
					autonomous();
					while (isAutonomous() && !isDisabled()) {
						Timer.delay(0.05);
					}
				} else {
					operatorControl();
					while (isOperatorControl() && !isDisabled()) {
						Timer.delay(0.05);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.out.println("EX: "+e.getMessage());
				SmartDashboard.putString("Status", "ERROR: Halted!");
				// Stop everything
				halt();
				// Wait for acknowledgement or state change
				while ((isAutonomous() && isEnabled())
						|| (isOperatorControl() && isEnabled() && !leftStick.getRawButton(10) && !leftStick.getRawButton(11))
						|| isDisabled()) {
					Timer.delay(0.05);
				}
			}
		}
	}
}
