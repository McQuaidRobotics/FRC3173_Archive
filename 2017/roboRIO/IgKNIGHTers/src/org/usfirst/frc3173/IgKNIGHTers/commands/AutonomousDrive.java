package org.usfirst.frc3173.IgKNIGHTers.commands;

import org.usfirst.frc3173.IgKNIGHTers.Robot;
import org.usfirst.frc3173.IgKNIGHTers.RobotMap;

import com.ctre.CANTalon;
import org.usfirst.frc3173.IgKNIGHTers.Utilities.IMU;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 *@author JacobLaptop
 */
public class AutonomousDrive extends Command {
	private final CANTalon leftMotorTop = RobotMap.drivesLeftMotorTop;
    private final CANTalon rightMotorTop = RobotMap.drivesRightMotorTop;
    private final CANTalon leftMotorBottom = RobotMap.drivesLeftMotorBottom;
    private final CANTalon rightMotorBottom = RobotMap.drivesRightMotorBottom;
    final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	 public static int kMaxNumberOfMotors=4;
	public static double iniOrienation=0.0;
	public static double target=0.0;
	private static double x,y,rot;
	public static IMU RRIMU=null;
	public static boolean gyroMovement=true;
	
    public AutonomousDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis)
    }


    // Called just before this Command runs the first time
    protected void initialize() {
    	RRIMU.calibration();
		iniOrienation=RRIMU.getOrientationX();
    }
    

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	/*System.out.println("AutoDrive");
    	if (gyroMovement=true){
    		if (RRIMU.getOrientationX()!=target){
    			System.out.println("Turn");
    			if(iniOrienation>target){
    				drive();}
    			if(iniOrienation<target){
        			x=-x;		
    				drive();}
    		}
    		else{
    			isFinished();
    		}
    	}
    	System.out.println("Drive");
    	drive();*/
    	double gyroAngle=0.0;
    	double rotation;
    	double xIn;
    	double yIn; 
    	rotation=0.0;
    	xIn = 0.0;
    	yIn = 0.5;
    	
	 	double m_maxOutput=1.0;
	    // Negate y for the joystick.
	    yIn = -yIn;
	    // Compenstate for gyro angle.
	    double[] rotated = rotateVector(xIn, yIn, gyroAngle);
	    xIn = rotated[0];
	    yIn = rotated[1];

	    double[] wheelSpeeds = new double[4];
	    wheelSpeeds[MotorType.kFrontLeft.value] = xIn + yIn + rotation;
	    wheelSpeeds[MotorType.kFrontRight.value] = -xIn + yIn - rotation;
	    wheelSpeeds[MotorType.kRearLeft.value] = -xIn + yIn + rotation;
	    wheelSpeeds[MotorType.kRearRight.value] = xIn + yIn - rotation;

	    normalize(wheelSpeeds);
	    leftMotorTop.set(wheelSpeeds[MotorType.kFrontLeft.value] * m_maxOutput);
	    rightMotorTop.set(wheelSpeeds[MotorType.kFrontRight.value] * (m_maxOutput));
	    leftMotorBottom.set(wheelSpeeds[MotorType.kRearLeft.value] * m_maxOutput);
	    rightMotorBottom.set(wheelSpeeds[MotorType.kRearRight.value] * (m_maxOutput));
    }
    protected static double[] rotateVector(double x, double y, double angle) {
	    double cosA = Math.cos(angle * (3.14159 / 180.0));
	    double sinA = Math.sin(angle * (3.14159 / 180.0));
	    double[] out = new double[2];
	    out[0] = x * cosA - y * sinA;
	    out[1] = x * sinA + y * cosA;
	    return out;
	  }
	protected static void normalize(double[] wheelSpeeds) {
	    double maxMagnitude = Math.abs(wheelSpeeds[0]);
	    for (int i = 1; i < kMaxNumberOfMotors; i++) {
	      double temp = Math.abs(wheelSpeeds[i]);
	      if (maxMagnitude < temp) {
	        maxMagnitude = temp;
	      }
	    }
	    if (maxMagnitude > 1.0) {
	      for (int i = 0; i < kMaxNumberOfMotors; i++) {
	        wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
	      }
	    }
	  }

    // Make this return true when this Command no longer needs to run execute()
   protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

	
	
}
