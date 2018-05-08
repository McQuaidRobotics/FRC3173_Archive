/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.RobotCamera.Target;
import edu.wpi.first.wpilibj.Timer;

 /**
   * The controlling class for the Shooter mechanism.
   * @todo Take out turret control
   * @author Jon
   */
public class Shooter {
	/**
	 * The height of the shooter off the ground in inches.
	 * (To the centre of the ball)
	 */
    private static final int SHOOTER_HEIGHT = 49;
	
    private RobotJaguar m_kMotor1, m_kMotor2;
	private Drives drives;
    private static final double M_ANGLE = Math.toRadians(60), M_GRAV = 32.17*12;
    //private RobotCamera robotCamera;
    /**
     * This constructor creates the object and initialises the motors.
	 * The Drive jaguars are needed to turn, since the lazy susan fell apart.
     * @todo autoramp for the CANJaguars, value of about 100
	 * @param cameraLights the relay for the LEDs surrounding the camera
	 * @param FLDriveJaguar The forward left drive jaguar
	 * @param FRDriveJaguar The forward right drive jaguar
	 * @param BLDriveJaguar The back left drive jaguar
	 * @param BRDriveJaguar The back right drive jaguar
	 */
    public Shooter(Relay cameraLights, Drives drives){
        m_kMotor1 = new RobotJaguar(ElectricalIDs.CANJaguars.FIRST_SHOOTER_JAGUAR);
        m_kMotor2 = new RobotJaguar(ElectricalIDs.CANJaguars.SECOND_SHOOTER_JAGUAR);
		this.drives=drives;
        //robotCamera = new RobotCamera(cameraLights);
    }
    /**
     * This method calculates the exit velocity needed to launch the ball a specific distance.
     * @param distance The sonar rangefinder mounted on the robot.
     * @param height Constant height plus changes from moving shooter mechanism.
	 * @return The optimal RPM to fire at
     */
    public double calculateExitVelocity(double distance, double height){
        return (distance/(Math.cos(M_ANGLE)))*Math.sqrt(M_GRAV/(2*distance*Math.tan(M_ANGLE) - 2*height));
    }
    /**
     * This method fires the ball at a specific basket.
     * @param basket the Basket ID to fire at.
	 * @throws Exception if something is wrong with Vision
     */
    
    public void startFiring(int basket) throws Exception {  
        throw new Exception("Auto firing disabled");
		/*double speed;
		baseTurn(basket);
		if (IgKnightersBot.manualStop()) throw new Exception("Manual stop.");
        Target target = robotCamera.getTarget(basket);
		if(target == null) {throw new Exception("No baskets seen.");}
        speed = calculateExitVelocity(robotCamera.getTarget(basket).getDistance(),target.getHeight()-SHOOTER_HEIGHT)*25;
        if (IgKnightersBot.manualStop()) throw new Exception("Manual stop.");
		m_kMotor1.setX(speed/10000);
		m_kMotor2.setX(speed/10000);*/
    }
    
    /**
     * Returns the motors to their lowest setting.
     */
    
    public void defaultSpeed(){ 
		m_kMotor1.setX(-.25);
		m_kMotor2.setX(-.25);
    }
	
	/**
	 * Stops the shooter motors.
	 */
	public void stop() {
		m_kMotor1.setX(0);
		m_kMotor2.setX(0);
	}
    
    /**
     * Initializes the flywheel at the beginning of the match.
     */
    
    public void initSpeed(){
		m_kMotor1.setX(-75);
		m_kMotor2.setX(-.75);
		Timer.delay(1); // @TODO replace this delay
		m_kMotor1.setX(-.25);
		m_kMotor2.setX(-.25);
    }
    
    /**
     * Turns the shooter until it lines up with the target.
     * @param target The basket being targeted.
	 * @throws Exception if the targets can't be seen
     */
    
    public void baseTurn(int target) throws Exception {
		throw new Exception("Auto firing disabled");
		/*int angle = (int) robotCamera.getTarget(target).getAngle();
		while(angle != 0){
			if(angle > 0){
			    drives.leftRightDrive(.2,-.2,1);
			} else if(angle < 0){
				drives.leftRightDrive(-.2,2,1);
			}
			if (IgKnightersBot.manualStop()) break; // Manual stop activated
			//angle = (int)robotCamera.getTarget(target).getAngle();
		}
		drives.halt();*/
    }
    /**
     * Manually sets the speed of the flywheels
     * @param vpercent The voltage percentage, between -1.0 and 1.0
     */
    public void setSpeed(double vpercent){
        m_kMotor1.setX(-vpercent);
        m_kMotor2.setX(-vpercent);
    }

	public double motorCurrent() {
		return m_kMotor1.getOutputCurrent();
	}
}
