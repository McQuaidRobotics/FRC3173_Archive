package org.usfirst.frc3173.IgKNIGHTers.Utilities;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;

import edu.wpi.first.wpilibj.I2C; 
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class IMU {
	private final char REG_SIGNAL_PATH_RESET = 0x68;
	private final char MASK_SIGNAL_PATH_RESET = 0b11111000;
	private final char SETTING_RESET_ALL = 0b0000111;
	private final char REG_PWR_MGMT_1 = 0x6B;
	private final char MASK_PWR_MGMT_1 = 0b00010000;
	private final char SETTING_WAKE_UP = 0b00000000;
	private final char REG_WHO_AM_I = 0x75; //TODO: 0 and 7 reserved, but read only? 
	private final char REG_CONFIG = 0x1A;
	private final char MASK_CONFIG = 0b11000000;
	private final char REG_GYRO_CONFIG = 0x1B;
	private final char MASK_GYRO_CONFIG = 0b00000111;
	private final char REG_ACCEL_CONFIG = 0x1C;
	private final char MASK_ACCEL_CONFIG = 0b00000111;
	private final char REG_GYRO_START = 0x43;
	private final char I2Cadress=0x68;
	private long lastTime;
	private final int CalibrateSample=699;
	private double[][] humanReadNonZero=new double[CalibrateSample][3];
	private int indexValue =0;
	private DecimalFormat df = new DecimalFormat("#.00");
	public I2C main= new I2C(I2C.Port.kOnboard, I2Cadress);
	public double[] orientation=new double[3];//First index X orientation Second index Y orientation Third index Z orientation 
	public double[] zeroed=new double[3];//First index X zeroed Second index Y zeroed Third index Z zeroed

	public void IMUIni(){
		regWrite(REG_SIGNAL_PATH_RESET,MASK_SIGNAL_PATH_RESET,SETTING_RESET_ALL);//Resetting the IMU
		if (readReg(REG_WHO_AM_I)!=I2Cadress){
			System.out.println("Problems with IMU pinging failed");
		} else {
			System.out.println("IMU pinged OK");
		}
		regWrite(REG_PWR_MGMT_1,MASK_PWR_MGMT_1,SETTING_WAKE_UP);//Wake the IMU		
		regWrite(REG_CONFIG,MASK_CONFIG,6);//Enable the low pass filter		
		regWrite(REG_GYRO_CONFIG,MASK_GYRO_CONFIG,0);//Set gyro divider to 250 
		regWrite(REG_ACCEL_CONFIG,MASK_ACCEL_CONFIG,0);//Set accelerometer divider to 2
		//calibration();
		}
	public byte readReg(int reg){
		//Reading the registers 
		//System.out.println("Reading");
		byte buff[]=new byte[1]; 
		main.read(reg,1,buff);
		return buff[0];
	}
	public void regWrite(int currReg,int mask,int newValue){
		//finding the data to write and writing to the register 
		byte currData=readReg(currReg);
		main.write(currReg,((currData&mask)^newValue));
	}
	public void updateOrientation(){
		if(indexValue==CalibrateSample){
			indexValue=0;
		}
		byte[] readOut=new byte[6];
		byte[] xx=new byte[2];
		byte[] yy=new byte[2];
		byte[] zz=new byte[2];
		int value[]=new int [3];
		main.read(REG_GYRO_START,6,readOut);
		long readTime = System.currentTimeMillis();
		xx[0]=readOut[0];
		xx[1]=readOut[1];
		yy[0]=readOut[2];
		yy[1]=readOut[3];
		zz[0]=readOut[4];
		zz[1]=readOut[5];
		value[0] =ByteBuffer.wrap(xx).getShort();
		value[1] =ByteBuffer.wrap(yy).getShort();
		value[2] =ByteBuffer.wrap(zz).getShort();
		for(int z=0;z<3;z++){	
			humanReadNonZero[indexValue][z]=(value[z]/131.0);
		}
		//System.out.println(humanReadNonZero[indexValue][0]);

		if(lastTime==0){
			lastTime = readTime;
		}
		for(int i=0;i<3;i++){
			
			double RT=(readTime-lastTime)/1000.0;
			double zeoredHuman=humanReadNonZero[indexValue][i]-zeroed[i];
			double TimedZeored=RT*zeoredHuman;
			orientation[i]=orientation[i]+TimedZeored;
			if(orientation[i]<0){
				orientation[i]=orientation[i]+360;
			}
			else if(orientation[i]>360){
				orientation[i]=orientation[i]-360;
			}
		}
		//printOrientation();
		SmartDashboard.putNumber("X orientation ",orientation[0]);
		SmartDashboard.putNumber("X orientation ",orientation[0]);
		SmartDashboard.putNumber("Y orientation ",orientation[1]);
		SmartDashboard.putNumber("Z orientation ",orientation[2]);
		/*System.out.println("X orientation "+orientation[0]);
		System.out.println("Y orientation "+orientation[1]);
		System.out.println("Z orientation "+orientation[2]);*/
		indexValue++;
		lastTime=readTime;
	}
	public double getOrientationX(){
		return orientation[0];
	}
	public double getOrientationY(){
		return orientation[1];
	}
	public double getOrientationZ(){
		return orientation[2];
	}
	public void printOrientation(){
		System.out.println(" X "+df.format(orientation[0])+" Y "+df.format(orientation[1])+" Z "+df.format(orientation[2]));
		
	}
	public void zeroGyro(){
		for (int i=0;i<3;i++){
			orientation[i]=0;
		}
	}
	public void zeroArray(double[][] zeroArray, int placeIndex){
		int sum =0;
		for(int x=0;x<CalibrateSample;x++){
			sum+=zeroArray[x][placeIndex];
		}
		zeroed[placeIndex]=sum/CalibrateSample;
	}
	public void calibration(){
		//System.out.println("CALIBARATING!!!!!!");
		for(int i=0;i<3;i++){
			zeroArray(humanReadNonZero,i);
		}
	}
}

