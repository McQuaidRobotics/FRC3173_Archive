package org.usfirst.frc3173.IgKnighters2016.subsystems;


//import org.usfirst.frc3173.IgKnighters.utilities.BetterCameraServer;
//import org.usfirst.frc3173.IgKnighters2016.OI;
import org.usfirst.frc3173.IgKnighters2016.RobotMap;
import org.usfirst.frc3173.IgKnighters2016.Robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.vision.USBCamera;

public class Vision extends Subsystem {

	public static NetworkTable visionTable=NetworkTable.getTable("RoboRealm");
	private CameraServer camDashboard;
	//private CameraServer camDashboard1;
	//private BetterCameraServer camDashboard2;
	private int shootCam,acquireCam,currentCam;
	//private USBCamera shootCam,acquireCam;
	private String shoot="cam0";
	private String acquire="cam1";
	private Image frame;

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}
	
	public double width,height,cogX,cogY;
	double defaultValue=0.0;
	public double IMAGE_WIDTH, IMAGE_HEIGHT;
	
	public Vision(){
		//turn on LED
		RobotMap.visionLED.set(true);
		// Get camera ids by supplying camera name ex 'cam0', found on roborio web interface
        try{
        /*shootCam = NIVision.IMAQdxOpenCamera(shoot, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        acquireCam = NIVision.IMAQdxOpenCamera(acquire, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        //DriverStation.reportWarning("Created cameras!", false);
        currentCam = shootCam;
		NIVision.IMAQdxStopAcquisition(currentCam);
    	NIVision.IMAQdxConfigureGrab(currentCam);
    	NIVision.IMAQdxStartAcquisition(currentCam);
        //DriverStation.reportWarning("Started Acquisition!", false);
        // Img that will contain camera img
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        // Server that we'll give the img to
        camDashboard = CameraServer.getInstance();
        camDashboard.setQuality(50);*/
		Vision.visionTable=NetworkTable.getTable("RoboRealm");
		IMAGE_WIDTH=visionTable.getNumber("IMAGE_WIDTH",defaultValue);
		IMAGE_HEIGHT=visionTable.getNumber("IMAGE_HEIGHT",defaultValue);
		getValues();
		//System.out.println("Distance is "+getDistance(height));
		//NetworkTable.globalDeleteAll();
        }catch(Exception e){
        	e.printStackTrace();
        }

	}
	/**
	 * this method checks the various vision buttons and calls the associated methods. Use Commands instead
	 */
	public void checkButtons(){
		try{
		//if(Robot.oi.acquireButton.get()) switchCamera(acquireCam);
		//if(Robot.oi.shootButton.get()) switchCamera(shootCam);
		//if(Robot.oi.visionLED.get())  RobotMap.visionLED.set(!RobotMap.visionLED.get());
		//if(Robot.oi.autoShoot.get()) autoShoot(8,0.5);
		
        //DriverStation.reportWarning("Checked Buttons!", false);
		//updateCamera();
		}catch(Exception e){
        	e.printStackTrace();
        }
	}
	/** stop acquiring the current camera,
	 * start acquiring the new camera,
	 * set the current cam to the cam we just switched to
	 * @param newCam the camera id to switch to
	 */
	private void switchCamera(int newCam){
		NIVision.IMAQdxStopAcquisition(currentCam);
	    NIVision.IMAQdxUnconfigureAcquisition(currentCam);
    	NIVision.IMAQdxConfigureGrab(newCam);
    	NIVision.IMAQdxStartAcquisition(newCam);
    	currentCam = newCam;
	}
	/** grab a frame from the current camera,
	 * send that frame to the smart dashboard using camera server
	 */
    private void updateCamera()
    {
    	try{
    	NIVision.IMAQdxGrab(currentCam, frame, 1);
        camDashboard.setImage(frame);
        //DriverStation.reportWarning("Image updated!", false);
    	}catch(Exception e){
        	e.printStackTrace();
        }
    }
	/**
	 * update the height and width variables with the NetworkTables values
	 */
	public void getValues(){
		try{
		double defaultValue=0.0;
		height=visionTable.getNumber("HEIGHT",defaultValue);
		width=visionTable.getNumber("WIDTH",defaultValue);
		cogX=visionTable.getNumber("COG_X",defaultValue);
		cogY=visionTable.getNumber("COG_Y",defaultValue);
		IMAGE_WIDTH=visionTable.getNumber("IMAGE_WIDTH",defaultValue);
		IMAGE_HEIGHT=visionTable.getNumber("IMAGE_HEIGHT",defaultValue);
//		System.out.println("get value height" + height);
//		System.out.println("get value width" + width);
//		System.out.println("get value cog x" + cogX);
//		System.out.println("get value cog y" + cogY);
//		System.out.println("get value image height" + IMAGE_HEIGHT);
//		System.out.println("get value image width" + IMAGE_WIDTH);

		}catch(Exception e){
        	e.printStackTrace();
        }
	}
	/**
	 * calculate approx distance using horizontal FOV
	 * @return distance in feet
	 */
	private double getDistanceY(){
		try{
		getValues();
		
		}catch(Exception e){
        	e.printStackTrace();
        }
		double distance=(1.0*IMAGE_HEIGHT)/(2*height*0.306447);
		return distance;
	}
	//calc. distance using Y, then put to smart dashboard
	private void printDistanceY(){
		try{
			SmartDashboard.putNumber("DistanceY",getDistanceY());
		}catch(Exception e){
        	e.printStackTrace();
        }
		
	}
	/**
	 * calculate approx distance using vertical FOV
	 * @return distance in feet
	 */
	private double getDistanceX(){
		try{
			getValues();
			
		}catch(Exception e){
        	e.printStackTrace();
        }
		double distance=(1.0*IMAGE_WIDTH)/(2*width*0.263964);
		return distance;
	}
	//calc. distance using X, then put to smart dashboard
	private void printDistanceX(){
		try{
			SmartDashboard.putNumber("DistanceX",getDistanceX());
		}catch(Exception e){
        	e.printStackTrace();
        }
	}
	/**
	 * average X and Y distances, then compare to an ideal distance idealDistanceFt, + or - a double maxError
	 * @param idealDistanceFt the ideal distance in feet
	 * @param maxError the maximum error (ideal distance +- error)
	 * @return true if it a good shot, false if it is not
	 */
	private boolean isGoodShot(double idealDistanceFt, double maxError){
			double average = getAverageDistance();
			if(average>=idealDistanceFt-maxError && average<=idealDistanceFt+maxError) return true;
			return false;
	}
	/**
	 * grab isGoodShot and put it to the smart dashboard
	 * @param idealDistanceFt the ideal distance in feet
	 * @param maxError the maximum error (ideal distance +- error)
	 */
	private void printGoodShot(double idealDistanceFt, double maxError){
		try{
			SmartDashboard.putBoolean("Good Shot?", isGoodShot(idealDistanceFt,maxError));
		}catch(Exception e){
        	e.printStackTrace();
        }
	}
	//compute the average distance
	private double getAverageDistance(){
			getValues();
			return (getDistanceX()+getDistanceY())/2;

	}
	public void printAllVisionInfo(double idealDistanceFt, double maxError){
		getValues();
		printDistanceX();
		printDistanceY();
		printGoodShot(idealDistanceFt, maxError);
	}
	private boolean autoShootX(){
		getValues();
		double deltaX=cogX-(IMAGE_WIDTH/2);
		double speed=deltaX/640;
		if(deltaX>10){
			Robot.drives.drivePercentVbus(-speed, speed);
			return false;
		}
		if(deltaX<10){
			Robot.drives.drivePercentVbus(speed, -speed);
			return false;
		}
		return true;
	}
	private boolean autoShootY(double idealDistanceFt, double maxError){
		if(isGoodShot(idealDistanceFt,maxError)){
			Robot.drives.drivePercentVbus(0,0);
			Robot.shooter.spinPercentVBus(1);
			try{Thread.sleep(500);}catch(Exception e){e.printStackTrace();}
			Robot.acquisition.moveAcquisitionBelt(1);
			try{Thread.sleep(500);}catch(Exception e){e.printStackTrace();}
			Robot.shooter.spinPercentVBus(0);
			Robot.acquisition.moveAcquisitionBelt(0);
			return true;
		}
		else{
			Robot.drives.drivePercentVbus(0.5, 0.5);
			return false;
		}
	}
	public void autoShoot(double idealDistanceFt, double maxError){
		boolean x=false;
		boolean y=false;
		while(!x){
			x=autoShootX();
		}
//		while(!y){
//			y=autoShootY(idealDistanceFt,maxError);
//		}
	}
	public void calculateAngleToTarget(){
		// TODO find distance between center of the screen and the target in feet or whatever
		double deltaX=Math.abs(cogX-(IMAGE_HEIGHT/2));
		//double deltaDistance=
	}

}
