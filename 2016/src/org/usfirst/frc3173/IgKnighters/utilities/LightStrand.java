package org.usfirst.frc3173.IgKnighters.utilities;

import edu.wpi.first.wpilibj.DigitalOutput;

public class LightStrand {
	
	DigitalOutput lightData,lightClock;
	byte pos,red,green,blue;
	
	public LightStrand(int dataChannel,int clockChannel){
		lightData=new DigitalOutput(dataChannel);
		lightClock=new DigitalOutput(clockChannel);
	}
	
	public void setColorAtPos(int p, int red, int green, int blue){
		byte pos = (byte) p;
		byte r = (byte) red;
		byte g = (byte) green;
		byte b = (byte) blue;
		send(getBooleanArray(pos));
		send(getBooleanArray(r));
		send(getBooleanArray(g));
		send(getBooleanArray(b));
		
	}
	
	public boolean[] getBooleanArray(byte b){
		boolean[] boolArray=new boolean[8];
		for(int i=0;i<8;i++){
			boolArray[i]=(1==(b&1));
			b=(byte)(b>>1);
		}
		return boolArray;
	}
	
	public void send(boolean[] b){
		//TODO: check to see if pulse causes the robot to hang while pulsing
		for(boolean e:b){
			lightData.set(e);
			lightClock.pulse(-42, 1f);
		}
	}
	
	
}
