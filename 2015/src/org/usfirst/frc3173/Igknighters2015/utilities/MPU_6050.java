package org.usfirst.frc3173.Igknighters2015.utilities;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.DoubleStream;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class MPU_6050 {
	/*
	 * Class for the InvenSense MPU_6050 IMU sensor
	 * 
	 * Provides filtered output from both the gyroscope
	 * and accelerometer and integrates this output
	 * to get orientation, velocity, and position.
	 * 
	 * Automatically re-calibrates if enough time has
	 * gone by that the sensor is stationary.
	 */
	
	I2C sensor; //Synchronize usages!
	
	//I2C address
	final char ADDRESS =  0x68;
	
	//All your registers are belong to us. Defined in RS-MPU-6000A-00.pdf

	/*
	 * In the MPU documentation, both gyro and accel offset registers are denoted
	 * XG - this causes conflicts, so accel registers have been changed to XA.
	*/
	
	/*
	 * Registers
	 * Unused registers commented out
	 */
	//private final char NOMASK = 0b00000000; //In case a setting needs to be written to a register that doesn't have reserved bits
	private final char XA_OFFS_USRH = 0x06;	 //Accel X-axis offset cancellation register high byte
	private final char XA_OFFS_USRL = 	0x07;	 //Accel X-axis offset cancellation register low byte
	private final char YA_OFFS_USRH = 0x08;	 //Accel Y-axis offset cancellation register high byte
	private final char YA_OFFS_USRL = 0x09;//Accel Y-axis offset cancellation register low byte
	private final char ZA_OFFS_USRH = 0x0A; //Accel Z-axis offset cancellation register high byte
	private final char ZA_OFFS_USRL = 0x0B;	 //Accel Z-axis offset cancellation register low byte
	private final char XG_OFFS_USRH = 0x13; // Gyro X-axis offset cancellation register high byte
	private final char XG_OFFS_USRL = 0x14; //Gyro X-axis offset cancellation register low byte
	private final char YG_OFFS_USRH =  0x15; //Gyro Y-axis offset cancellation register high byte
	private final char YG_OFFS_USRL = 0x16; //Gyro Y-axis offset cancellation register low byte
	private final char ZG_OFFS_URSH = 0x17; //Gyro Z-axis offset cancellation register high byte
	private final char ZG_OFFS_USRL = 0x18; //Gyro Z-axis offset cancellation register low byte
	//private final char SELF_TEST_X = 0x0D;
	//private final char SELF_TEST_Y = 0x0E;
	//private final char SELF_TEST_Z = 0x0F;
	//private final char SELF_TEST_A = 0x10;
	//private final char SELF_TEST_A_MASK = 0b11000000;
	//private final char SMPRT_DIV = 0x19;
	private final char CONFIG = 0x1A;
	private final char CONFIG_MASK = 0b11000000;
	private final char GYRO_CONFIG = 0x1B;
	private final char GYRO_CONFIG_MASK = 0b00000111;
	private final char ACCEL_CONFIG = 0x1C;
	private final char ACCEL_CONFIG_MASK = 0b00000111;
	//private final char MOT_THR = 0x1F;
	//private final char INT_ENABLE = 0x38;  
	//private final char INT_ENABLE_MASK = 0b10100110; //(5&7 not reserved, but not specified. Masking anyways)
	//private final char INT_STATUS = 0x3A;
	private final char ACCEL_START = 0x3B;
	//private final char ACCEL_XOUT_H = 0x3B;
	//private final char ACCEL_XOUT_L = 0x3C;
	//private final char ACCEL_YOUT_H = 0x3D;
	//private final char ACCEL_YOUT_L = 0x3E;
	//private final char ACCEL_ZOUT_H = 0x3F;
	//private final char ACCEL_ZOUT_L = 0x40;
	private final char GYRO_START = 0x43;
	//private final char GYRO_XOUT_H = 0x43;
	//private final char GYRO_XOUT_L = 0x44;
	//private final char GYRO_YOUT_H = 0x45;
	//private final char GYRO_YOUT_L = 0x46;
	//private final char GYRO_ZOUT_H = 0x46;
	//private final char GYRO_ZOUT_L = 0x47;
	private final char SIGNAL_PATH_RESET = 0x68;
	private final char TEMP_OUT_START = 0x41;
	//private final char TEMP_OUT_H = 0x41;
	//private final char TEMP_OUT_L = 0x42;
	
	//Masks for changing settings without altering reserved bits
	private final char SIGNAL_PATH_RESET_MASK = 0b11111000;
	//private final char MOT_DETECT_CTRL = 0x69;
	//private final char MOT_DETECT_CTRL_MASK = 0b11001111; //TODO: 0-3 needed?
	private final char PWR_MGMT_1 = 0x6B;
	private final char PWR_MGMT_1_MASK = 0b00010000;
	private final char WHO_AM_I = 0x75; //TODO: 0 and 7 reserved, but read only?
	
	//Other constants
	private final double gravity = 9.8;
	private double gyroDivider;
	private double accelDivider;
	
	//Other variables
	private boolean debug; //Set by constructor. When true, many of the methods print extra info
	
	
	//Variables used in calibration
	//Most recent sensor data/integrated values
	private long readTime = 0;
	private long lastReadTime = 0;
	
	//Array index
	private int currentIndex = 0;
	
	//TODO: create predefined configurations (see sparkfun code)
	//see datasheets
	//Write all zeroes to take sensor out of sleep mode
	private final char WAKE_UP = 0b00000000;
	
	//Settings for low pass filter
	//See datasheet for more info
	private final char[] DLPF = {0b00000000,0b00000001,0b00000010,0b00000011,0b00000100,0b00000101,0b00000110};

	
	private final char RESET_ALL = 0b0000111;
	/* 
	 * "Low level" settings- changing these may have major impacts on the
	 * way that the sensor functions.
	 */
	
	//Axes
	private gyroAxis gyroX;
	private gyroAxis gyroY;
	private gyroAxis gyroZ;
	private accelAxis accelX;
	private accelAxis accelY;
	private accelAxis accelZ;
	
	//Integration config
	//TODO: rename
	private final int N = 10;
	private double dt;
	
	/*
	 * LSB divider (int from 0 to 3)
	 * See datasheet and/or setDivider() for more info
	 * on what these values mean
	 */
	private final int GYRO_SENSITIVITY_LEVEL = 0; 
	private final int ACCEL_SENSITIVITY_LEVEL = 0;
	
	//Calibration & filter config
	//Number of reads that the sensor should 
	//be stationary for before calibrating
	private final int CALIBRATION_DELAY = 30;
	
	/*
	 * Range for mechanical filter and checking if stationary (sensor output +- sensitivity)
	 * Note that this is different from sensitivity levels, which are part of the hardware
	 */
	//Calculated by averaging standard deviations //TODO: recalculate?
	private final double GYRO_CALIBRATION_SENSITIVITY = 0.113;
	private final double ACCEL_CALIBRATION_SENSITIVITY = 0.0387;
	
	//enable or disable filtering
	private boolean FILTER = true;
	private int filterLevel = 0;
	
	public MPU_6050(Port port, int deviceAddress, boolean debugMode, int filterLevel) {
		sensor = new I2C(port, deviceAddress); //Create I2C device using WPIlib code
		debug = debugMode;
		this.filterLevel = filterLevel;
		initalize();
		startThread();
	}
	
	private void startThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true)
				{
					update();
					
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	//Setup and config methods

	//Setup methods
	private void initalize()
	{
		reset();
		
		if(debug)
		{
			System.out.println("IMU initalizing...");
			System.out.println("Not checking whether reads or writes fails. If WPI doesn't, we don't either.");
		}
		
		if(filterLevel < 0 || filterLevel > DLPF.length - 1)
		{
			filterLevel = 0;
		}
		ping();
		
		/*
		 * When the device powers on, it starts up in sleep mode, which
		 * must be explicitly disabled in order to begin normal operations.
		 * To do this, simply write a 0 to the PWR_MGMT_1 (0x6B) register.
		 */
		
		writeSettings(PWR_MGMT_1, PWR_MGMT_1_MASK, WAKE_UP);
		//if(debug)
		//{
			System.out.println("IMU is now awake.");
		//}
	
		if(FILTER)
		{
			writeSettings(CONFIG,CONFIG_MASK,DLPF[filterLevel]);
		}
		
		setGyroDivider(GYRO_SENSITIVITY_LEVEL);
	
		setAccelDivider(ACCEL_SENSITIVITY_LEVEL);
		
		createAxes();
		
		if(debug)
		{
			System.out.println("Initialization complete");
		}
	}

	private void reset()
	{
		//Resets sensor settings to default
		writeSettings(SIGNAL_PATH_RESET,SIGNAL_PATH_RESET_MASK,RESET_ALL);
	}
	
	private void createAxes()
	{
		gyroX = new gyroAxis(XG_OFFS_USRL,XG_OFFS_USRH);
		gyroY = new gyroAxis(YG_OFFS_USRL,YG_OFFS_USRH);
		gyroZ = new gyroAxis(ZG_OFFS_USRL,ZG_OFFS_URSH);
		accelX = new accelAxis(XA_OFFS_USRL,XA_OFFS_USRH);
		accelY = new accelAxis(YA_OFFS_USRL,YA_OFFS_USRH);
		accelZ = new accelAxis(ZA_OFFS_USRL,ZA_OFFS_USRH);
	}
		
	//Configuration methods
	
	/*
	 * Divider setters
	 * Set LSB divider for accel and gyro so that real
	 * values can be acquired at the desired accuracy.
	 * Meanings of option can be seen in comments in specific
	 * methods (copied from datasheets).
	 */
	private void setGyroDivider(int option){
		/*
		   FS_SEL  Full Scale Range LSB Sensitivity
		     0       ± 250 °/s       131 LSB/°/s
		     1       ± 500 °/s       65.5 LSB/°/s
		     2       ± 1000 °/s      32.8 LSB/°/s
		     3       ± 2000 °/s      16.4 LSB/°/s
		 */
		double[] divider = {131,65.5,32.8,16.4};
		char[] FS_SEL = {0b00000000,0b00001000,0b00010000,0b00011000};
		int[] range = {250,500,1000,2000};
		
		if(debug)
		{
			System.out.println("Setting gyro range...");
		}

		gyroDivider = divider[option];
		writeSettings(GYRO_CONFIG, GYRO_CONFIG_MASK, FS_SEL[option]);
	
		if(debug)
		{
			System.out.println("Gyro range set to +-" + range[option] +"deg/s");
		}
	}
	
	private void setAccelDivider(int option){
		/*
		 AFS_SEL Full Scale Range LSB Sensitivity
			0         ±2g           16384 LSB/g
			1         ±4g           8192 LSB/g
			2         ±8g           4096 LSB/g
			3         ±16g          2048 LSB/g
		 */
		
		char[] FS_SEL = {0b00000000,0b00001000,0b00010000,0b00011000};
		int[] range = {2,4,8,16};
		double[] divider = {16834,8192,4096,2048};
		
		
		if(debug)
		{
			System.out.println("Setting accel range...");
		}

		accelDivider = divider[option];
		writeSettings(ACCEL_CONFIG, ACCEL_CONFIG_MASK, FS_SEL[option]);
	
		if(debug)
		{
			System.out.println("accel range set to +-" + range[option] +"m/s");
		}
	}

	/*
	 * WARNING: make sure robot is stationary before calling this method.
	 */
	public synchronized void calibrate()
	{	
		gyroX.calibrate();
		gyroY.calibrate();
		gyroZ.calibrate();
		accelX.calibrate();
		accelY.calibrate();
		accelZ.calibrate();
			
		
		//if(debug)
		//{
			System.out.println("Calibration successful.");
			System.out.println("gyroZeroX = " + gyroX.getZero());
			System.out.println("gyroZeroY = " + gyroY.getZero());
			System.out.println("gyroZeroZ = " + gyroZ.getZero());
			System.out.println("accelZeroX = " + accelX.getZero());
			System.out.println("accelZeroY = " + accelY.getZero());
			System.out.println("accelZeroZ = " + accelZ.getZero());
		//}
			
		
	}
	
	//Runtime methods
	/*
	 * Call every tick. Reads accel and gyro and tries to calibrate.
	 */
	private synchronized void update()
	{
		//Read sensors
		//Populate with unmodified sensor values
		int[] rawGyro = burstRead(GYRO_START);
		int[] rawAccel = burstRead(ACCEL_START); 
		
		/*
		 * If currentPosition pointing at the end, point to the beginning
		 */
		if(currentIndex == N)
		{
			currentIndex = 0;
		}
		
		//System.out.println("readTime: " + readTime + " lastReadTime: " + lastReadTime);
		dt = (readTime - lastReadTime)/1000.0;
		//System.out.println("calculated dt: " + dt);
		
		gyroX.update(rawGyro[0]);
		gyroY.update(rawGyro[1]);
		gyroZ.update(rawGyro[2]);
		accelX.update(rawAccel[0]);
		accelY.update(rawAccel[1]);
		accelZ.update(rawAccel[2]);
		
		currentIndex++;
		
		calibrateIfStationary();
		
		lastReadTime = readTime;
	}
	
	/*
	 * Averages passed in number of sensor reads to get zero.
	 * Only calibrates if all axes are stable.
	 */
	public void calibrateIfStationary(){
		if(accelX.getTicksNotMoving() >= CALIBRATION_DELAY 
				&& accelY.getTicksNotMoving() >= CALIBRATION_DELAY 
				&& accelZ.getTicksNotMoving() >= CALIBRATION_DELAY 
				&& gyroX.getTicksNotMoving() >= CALIBRATION_DELAY
				&& gyroY.getTicksNotMoving() >= CALIBRATION_DELAY 
				&& gyroZ.getTicksNotMoving() >= CALIBRATION_DELAY)
		{
			calibrate();
		}
		else 
		{
			if(debug)
			{
				System.out.println("IMU not stationary enough to calibrate");
			}
		}
	}

	//Testing methods
	
	/*
	 * Check whether or not the IMU can be accessed at the expected address
	 */
	public void ping(){
		byte readAddress = readByte(WHO_AM_I);
		
		if(readAddress == this.ADDRESS)
		{
			System.out.println("IMU alive! Address: 0x" + Integer.toHexString(readAddress));
		}
		else
		{
			System.out.println("Failed to ping IMU. Reported address: 0x" + Integer.toHexString(readAddress));
		}

	}

	//TODO: Not implemented
	/*
	private void selfTest(){
		//There is stuff from Invensense about doing this without the DMP
		Do some research sometime
	}
	*/
	
	//Utility & helper methods
	
	/*
	 * Read multiple bytes from sequential registers,
	 * combine high and low bytes into ints
	 * 
	 * Returns raw sensor output
	 */
	private int[] burstRead(int registerStartAddress)
	{
		byte[] buffer = new byte[6]; //high and low bytes of x,y, and z
		int[] rawValues = new int[3]; //x,y,z as ints formed from combining high and low bytes
		
		readTime = System.currentTimeMillis();
		
		if(lastReadTime == 0)
		{
			lastReadTime = readTime;
		}
		
		synchronized(sensor)
		{
			sensor.read(registerStartAddress, 6, buffer);	
		}
		
		byte[] x = {buffer[0],buffer[1]};
		byte[] y = {buffer[2],buffer[3]};
		byte[] z = {buffer[4],buffer[5]};
		
		//Combine high and low bytes into ints
		ByteBuffer bb = ByteBuffer.wrap(x);
		rawValues[0] = bb.getShort();
		
		bb = ByteBuffer.wrap(y);
		rawValues[1] = bb.getShort();
	
		bb = ByteBuffer.wrap(z);
		rawValues[2] = bb.getShort();
		
		return rawValues;
	}
	
	//Utility methods
	
	/*
	 * returns single byte rather than buffer.
	 *
	 *registerAddress -> address of register to read
	*/
	private byte readByte(int registerAddress){
		byte[] buffer = new byte[1];
		synchronized(sensor)
		{
			sensor.read(registerAddress, 1, buffer); //WPI doesn't error check so we don't either.
				
		}
		return buffer[0];
	}	

	/*
	 * Change writable registers using read-modify-write principle
	 * Prevents reserved bits from being changed
	 * 
	 * registerAddress -> address of register to read
	 * reservedMask -> byte mask to prevent accidential change of reserved bits
	 * settings -> binary string to write to register
	 */
	private void writeSettings(int registerAddress, int reservedMask, int settings){
		
		int currentValue = 0; //Stores value that is currently in the register 
		
		if(debug)
		{
			System.out.println("Settings will be written to 0x" + Integer.toHexString(registerAddress));
		}
		
		//read current register value
		currentValue = readByte(registerAddress);
		
		//Look up masking if this doesn't make sense
		//             Set non-reserved bits to 0   then set desired bits to 1
		int toWrite = ((currentValue & reservedMask) ^ settings);

		if(debug)
		{
			System.out.println("settings to write: " + Integer.toBinaryString(settings));
			System.out.println("currentValue:      " + Integer.toBinaryString(currentValue));
			System.out.println("mask to use:       " + Integer.toBinaryString(reservedMask));
			System.out.println("masked setting:    " + Integer.toBinaryString(toWrite));
		}
		
		synchronized(sensor)
		{
			sensor.write(registerAddress, toWrite); //Write changed binary string to register		
		}
		if(debug)
		{
			System.out.println("Settings successfully written to 0x" + Integer.toHexString(registerAddress));	
		}
	}
	
	//Getters
	/*
	 * Rotation methods return deg/sec
	 * Acceleration methods return m/sec^2
	 * Orientation methods return degrees
	 * Velocity methods return m/sec
	 * Position methods return m from starting position
	 */
	
	public synchronized double getXRotation(){
		return gyroX.getLastRead();
	}
	
	public synchronized double getYRotation()
	{
		return gyroY.getLastRead();
	}
	
	public synchronized double getZRotation()
	{
		return gyroZ.getLastRead();
	}
	
	public synchronized double getXAcceleration()
	{
		return accelX.getLastRead();
	}
	
	public synchronized double getYAcceleration()
	{
		return accelY.getLastRead();
	}
	
	public synchronized double getZAcceleration()
	{
		return accelZ.getLastRead();
	}
	
	/*
	public synchronized double getXVelocity (){
		return netVelocityX;
	}
	public synchronized double getYVelocity(){
		return netVelocityY;	
	}
	public synchronized double getZVelocity(){
		return netVelocityZ;
	}
	public synchronized double getXOrientation(){
		return netOrientationX;
	}
	*/
	public synchronized double getYOrientation(){
		return gyroY.getOrientation();
	}
	public synchronized double getZOrientation(){
		return gyroZ.getOrientation();
	}
	/*
	public synchronized double getXPosition(){
		return netPositionX;
	}
	public synchronized double getYPosition(){
		return accelY.getPosition();
	}	
	public synchronized double getZPosition(){
		return netPositionZ;
	}
	*/
	
	//Returns temperature of sensor in Celcius
	public double getTemp()
	{	
		//Formula from datasheet to get temperature in Celcius
		return readByte(TEMP_OUT_START)/340 + 36.53;
	}
	
	/*
	 * Sets orientation to specified value. This should
	 * probably only be used during robot initialization
	 * in case the robot is not facing directly away from
	 * the control station to start
	 */
	public void setOrientation(int orientation)
	{
		gyroZ.setOrientation(orientation);
	}
	
	private abstract class Axis
	{
		protected double zero = 0;
		protected double lastRead = 0;
		protected int ticksNotMoving = 0;
		protected double[] humanReadableZeroed = new double[N];
		protected double[] humanReadableNonZeroed = new double[N];
		protected double calibrationSensitivity;
		protected int lowOffsetRegister;
		protected int highOffsetRegister;
		
		public Axis(int lowOffsetRegister, int highOffsetRegister)
		{
			this.lowOffsetRegister = lowOffsetRegister;
			this.highOffsetRegister = highOffsetRegister;
		}
		
	    //convert back to LSB. This probably loses accuracy.
		public abstract short toBiasLSB(double data);
		
		public abstract double toHumanReadable(int read);
		
		public abstract void integrate(double data);
		
		public double filter(double read)
		{	
		    //	smoothed += (read - smoothed)/(SMOOTHING / dt);
			//System.out.println("dt:" + dt);
			
			float RC = 0.35f;
			float alpha = (float) (dt / (RC + dt));
			  
			if(dt == 0)
			{	
				//smoothed = (read - lastRead)/(SMOOTHING);		
				return read;
			}
			else
			{
				//return lastRead + ((read - lastRead)/(SMOOTHING / dt));	
				//return 0 + dt*((read - lastRead)/(SMOOTHING));	
				 return ((alpha * read) + (1.0 - alpha) * lastRead);
			}
			
		    /* Low pass filter */
		    
		}
		
		public void update(int read)
		{
			double data = toHumanReadable(read);
			humanReadableNonZeroed[currentIndex] = data;
			data -= zero; //TODO: -zero not needed once biases work
			
			//check if stationary
			if(data >= lastRead - 2*calibrationSensitivity && data <= lastRead + 2*calibrationSensitivity)
			{
				ticksNotMoving++;
			}
			else
			{
				ticksNotMoving = 0;
			}
		
			//Add data to correct position of its array.			 	
			humanReadableZeroed[currentIndex] = data; 
			
			lastRead = humanReadableZeroed[currentIndex];
			integrate(lastRead);
		}
		
		//Note: this should only be called when it is appropriate to calibrate
		public void calibrate()
		{
			//Sets axis zero to human readable data array
			System.out.println(Arrays.toString(humanReadableNonZeroed));
			zero = DoubleStream.of(humanReadableNonZeroed).sum() / humanReadableNonZeroed.length;
			ticksNotMoving = 0;
		}
		
		public double[] getHumanReadable()
		{
			return humanReadableZeroed;
		}
		
		public double getZero()
		{
			return zero;
		}
		
		public double getLastRead()
		{
			return lastRead;
		}
		
		public int getTicksNotMoving()
		{
			return ticksNotMoving;
		}
	}		
	
	public class gyroAxis extends Axis
	{
		private double orientation = 0;
		
		public gyroAxis(int lowOffsetRegister, int highOffsetRegister)
		{
			super(lowOffsetRegister, highOffsetRegister);
			calibrationSensitivity = GYRO_CALIBRATION_SENSITIVITY;
		}
		
		
		@Override
		public double toHumanReadable(int read) {
			return read/gyroDivider;
		}

		@Override
		public void integrate(double data) {
							//current     //data      //dt                    //in sec  //0 to 360 
			//System.out.println("orientation:" + orientation + "lastRead:" + lastRead + "dt: " + dt);
			orientation = (orientation + (lastRead * dt));
			
			//lol don't we all wish Java's modulus actually worked (it doesn't)
			if(orientation < 0)
			{
				orientation += 360;
			}
			else if(orientation >= 360)
			{
				orientation -= 360;
			}
		}
		
		public double getOrientation()
		{
			return orientation;
		}
		
		public void setOrientation(double newOrientation)
		{
			orientation = newOrientation;
		}


		@Override
		public short toBiasLSB(double data) {
			return (short)(zero * 32.8);
		}


		/*
		@Override
		public void calibrate() {
			//Sets axis zero to human readable data array
			System.out.println(Arrays.toString(humanReadable));
			zero = DoubleStream.of(humanReadable).sum() / humanReadable.length;
			System.out.println("calculated gyro zero:" + zero);
			ticksNotMoving = 0;
			
			byte[] buffer = new byte[2];
			
			/*
			 * The Gyro registers at boot up will have a default value of 0. The value of the bias inputted needs to be in
			 * +-1000dps sensitivity range. This means each 1 dps = 32.8 LSB
			 *                                                                 
			ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).putShort(toBiasLSB(zero));
			
			//Write biases to bias registers
			//0 has low byte, 1 has high byte
			writeSettings(lowOffsetRegister, NOMASK, buffer[0]);
			writeSettings(highOffsetRegister, NOMASK, buffer[1]);
		}*/
	}
	
	public class accelAxis extends Axis
	{
		private double velocity = 0;
		private double position = 0;
		
		public accelAxis(int lowOffsetRegister, int highOffsetRegister)
		{
			super(lowOffsetRegister,highOffsetRegister);
			calibrationSensitivity = ACCEL_CALIBRATION_SENSITIVITY;
		}
		
		@Override
		public double toHumanReadable(int read) {
			return (read * gravity)/accelDivider;
		}

		@Override
		public void integrate(double data) { //TODO: synchronize?
			            //data      //dt                      //in sec
			velocity += lastRead * (readTime - lastReadTime) / 1000;
			           //newest value   //dt                 //in sec
			position += velocity * (readTime - lastReadTime) / 1000;
		}
		
		public double getVelocity()
		{
			return velocity;
		}
		
		public double getPosition()
		{
			return position;
		}

		@Override
		public short toBiasLSB(double data) {
			return (short)(zero * 4096);
		}

		/*
		@Override
		public void calibrate() {
			//Sets axis zero to human readable data array
			System.out.println(Arrays.toString(humanReadable));
			zero = DoubleStream.of(humanReadable).sum() / humanReadable.length;
			System.out.println("calculated accel zero:" + zero);
			ticksNotMoving = 0;
			
			byte[] previousBiasLSB = new byte[2];
			byte[] toWriteLSB = new byte[2];
			short previousBias;
			int toWrite;
			int lastBit;
			/*
			    Unlike the Gyro, the accel offset registers are not as straight forward to use.
				1.Initial values contain the OTP values of the Accel factory trim. Therefore at bootup there will be a
				non-zero value in these registers. Users will need to first read the register and apply the biases to
				that value.
				2.Format is in +-8G in which 1mg = 4096 LSB
				3.Bit 0 on the low byte of each axis is a reserved bit and needs to be preserved.
			 /
			previousBiasLSB[0] = readByte(lowOffsetRegister);
			previousBiasLSB[1] = readByte(highOffsetRegister);
			
			previousBias = ByteBuffer.wrap(previousBiasLSB).order(ByteOrder.LITTLE_ENDIAN).getShort();
			
			toWrite = previousBias + toBiasLSB(zero);
			
			lastBit = previousBias & 1;
								
								//= !1
			toWrite = toWrite & 0xFFFFFFFE;
			
			toWrite = toWrite | lastBit;
			
			ByteBuffer.wrap(toWriteLSB).order(ByteOrder.LITTLE_ENDIAN).putShort((short)toWrite);
			
			//Write biases to bias registers
			//0 has low byte, 1 has high byte
			synchronized(sensor)
			{
				sensor.write(lowOffsetRegister, toWriteLSB[0]);	
				sensor.write(highOffsetRegister, toWriteLSB[1]);
			}
		}*/
	}
}		