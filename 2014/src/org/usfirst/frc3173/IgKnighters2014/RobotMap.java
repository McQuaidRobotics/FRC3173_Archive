// RobotBuilder Version: 1.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.
package org.usfirst.frc3173.IgKnighters2014;
    
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc3173.IgKnighters2014.utilities.CANJaguar;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static CANJaguar drivesDrivesRearLeft;
    public static CANJaguar drivesDrivesRearRight;
    public static CANJaguar drivesDrivesFrontLeft;
    public static CANJaguar drivesDrivesFrontRight;
    public static Relay drivesShifter;
    public static CANJaguar shooterShooterPinion;
    public static DoubleSolenoid shooterLatchPneumatic;
    public static DoubleSolenoid acquisitionAcquisitionPneumatic;
    public static AnalogChannel acquisitionBallDetector;
    public static CANJaguar elevationElevationMotor;
    public static Encoder elevationEncoder;
    public static AnalogChannel pneumagicsdistance;
    public static Compressor pneumagicsAlex;
    public static Relay pneumagicsCameraLight;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        try { 
            drivesDrivesRearLeft = new CANJaguar(2);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
	
        
        try { 
            drivesDrivesRearRight = new CANJaguar(3);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
	
        
        try { 
            drivesDrivesFrontLeft = new CANJaguar(4);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
	
        
        try { 
            drivesDrivesFrontRight = new CANJaguar(5);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
	
        
        drivesShifter = new Relay(1, 1);
	LiveWindow.addActuator("Drives", "Shifter", drivesShifter);
        
        try { 
            shooterShooterPinion = new CANJaguar(8);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
	
        
        shooterLatchPneumatic = new DoubleSolenoid(1, 3, 4);      
	
        
        acquisitionAcquisitionPneumatic = new DoubleSolenoid(1, 2, 1);      
	
        
        acquisitionBallDetector = new AnalogChannel(1, 1);
	LiveWindow.addSensor("Acquisition", "Ball Detector", acquisitionBallDetector);
        
        try { 
            elevationElevationMotor = new CANJaguar(6);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
	
        
        elevationEncoder = new Encoder(1, 1, 1, 2, true, EncodingType.k1X);
	LiveWindow.addSensor("Elevation", "Encoder", elevationEncoder);
        elevationEncoder.setDistancePerPulse(1.0);
        elevationEncoder.setPIDSourceParameter(PIDSourceParameter.kDistance);
        elevationEncoder.start();
        pneumagicsdistance = new AnalogChannel(1, 2);
	LiveWindow.addSensor("Pneumagics", "distance", pneumagicsdistance);
        
        pneumagicsAlex = new Compressor(1, 14, 1, 3);
	
        
        pneumagicsCameraLight = new Relay(1, 4);
	LiveWindow.addActuator("Pneumagics", "Camera Light", pneumagicsCameraLight);
        
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }
}
