package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

/**
 * McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * Primary class for controlling the robot's deployment subsystem
 *
 * @author Nick Brown
 */
public class RobotDeployment {

    // Class for storing final variables of possible deployment positions
    public static class Direction {

        static final boolean kDeploy = true;
        static final boolean kRetract = false;
    }
    private Solenoid m_deploymentSolenoidStageOneIn;
    private Solenoid m_deploymentSolenoidStageOneOut;
    private Solenoid m_deploymentSolenoidStageTwoIn;
    private Solenoid m_deploymentSolenoidStageTwoOut;

    /**
     * Controls the deployment system for the mini-bot (pre-initialize objects constructor)
     * 
     * @param deploymentSolenoidIn "In" valve of the double solenoid
     * @param deploymentSolenoidOut "Out" valve of the double solenoid
     */
    public RobotDeployment(Solenoid deploymentSolenoidStageOneIn, Solenoid deploymentSolenoidStageOneOut, Solenoid deploymentSolenoidStageTwoIn, Solenoid deploymentSolenoidStageTwoOut) {
        m_deploymentSolenoidStageOneIn = deploymentSolenoidStageOneIn;
        m_deploymentSolenoidStageOneOut = deploymentSolenoidStageOneOut;
        m_deploymentSolenoidStageTwoIn = deploymentSolenoidStageTwoIn;
        m_deploymentSolenoidStageTwoOut = deploymentSolenoidStageTwoOut;

        this.setOff();
    }

    /**
     * Controls the deployment system for the mini-bot (Object IDs constructor)
     * 
     * @param deploymentSolenoidInID "In" valve ID of the double solenoid
     * @param deploymentSolenoidOutID "Out" valve ID of the double solenoid
     */
    public RobotDeployment(int deploymentSolenoidStageOneInID, int deploymentSolenoidStageOneOutID, int deploymentSolenoidStageTwoInID, int deploymentSolenoidStageTwoOutID) {
        m_deploymentSolenoidStageOneIn = new Solenoid(deploymentSolenoidStageOneInID);
        m_deploymentSolenoidStageOneOut = new Solenoid(deploymentSolenoidStageOneOutID);
        m_deploymentSolenoidStageTwoIn = new Solenoid(deploymentSolenoidStageTwoInID);
        m_deploymentSolenoidStageTwoOut = new Solenoid(deploymentSolenoidStageTwoOutID);

        this.setOff();
    }

    /**
     * Initializes the Solenoids.
     */
    public void setOff() {
        m_deploymentSolenoidStageOneIn.set(true);
        m_deploymentSolenoidStageOneOut.set(false);
        m_deploymentSolenoidStageTwoIn.set(true);
        m_deploymentSolenoidStageTwoOut.set(false);

    }

    /**
     * Used in an emergency to deploy if for some reason the two stages do not work.
     * 
     * @param deploy Direction to set pneumatic (True = Deploy; False = Retract) 
     */
    public void emergencyDeploy(boolean deploy) {
        setStageOne(deploy);
        Timer.delay(0.5);
        setStageTwo(deploy);
    }

    /**
     * Sets the first stage of deployment.
     *
     * @param deploy Direction to set pneumatic (True = Deploy; False = Retract)
     */
    public void setStageOne(boolean deploy) {
        m_deploymentSolenoidStageOneIn.set(!deploy);
        m_deploymentSolenoidStageOneOut.set(deploy);
    }

    /**
     * Sets the second stage of deployment.
     *
     * @param deploy Direction to set pneumatic (True = Deploy; False = Retract)
     */
    public void setStageTwo(boolean deploy) {
        m_deploymentSolenoidStageTwoIn.set(!deploy);
        m_deploymentSolenoidStageTwoOut.set(deploy);
    }
}
