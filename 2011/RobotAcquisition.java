package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * Primary class for controlling the robot's acquisition subsystem
 *
 * @author Nick Brown and Ben Pylko
 */
public class RobotAcquisition {

    /**
     * Pre-set values for rotation directions
     */
    public static class Direction {

        public boolean direction;
        static final boolean kUp_val = true;
        static final boolean kDown_val = false;
        public static final Direction kUp = new Direction(kUp_val);
        public static final Direction kDown = new Direction(kDown_val);

        private Direction(boolean direction) {
            this.direction = direction;
        }
    }
    private CANJaguar m_topAcquisitionCANJaguar;
    private CANJaguar m_bottomAcquisitionCANJaguar;
    private DigitalInput m_acquisitionPressureSwitch;
    private final double EJECT_SPEED = 0.45;
    private final double ACQUIRE_SPEED = -0.4;
    private final double ROTATE_SPEED = 0.3;

    /**
     * Creates object using pre-initialized components
     *
     * @param topAcquisitionCANJaguar CANJaguar for controlling the upper motor on the acquisition unit
     * @param bottomAcquisitionCANJaguar CANJaguar for controlling the lower motor on the acquisition unit
     * @param acquisitionPressureSwitch Contact sensor on the acquisition unit
     */
    public RobotAcquisition(CANJaguar topAcquisitionCANJaguar, CANJaguar bottomAcquisitionCANJaguar, DigitalInput acquisitionPressureSwitch) {
        m_topAcquisitionCANJaguar = topAcquisitionCANJaguar;
        m_bottomAcquisitionCANJaguar = bottomAcquisitionCANJaguar;
        m_acquisitionPressureSwitch = acquisitionPressureSwitch;
    }

    /**
     * Creates objects with the inputted component IDs
     *
     * @param topAcquisitionCANJaguar CANJaguar ID for controlling the upper motor on the acquisition unit
     * @param bottomAcquisitionCANJaguar CANJaguar ID for controlling the lower motor on the acquisition unit
     * @param acquisitionPressureSwitch Contact sensor ID on the acquisition unit
     */
    public RobotAcquisition(int topAcquisitionCANJaguarID, int bottomAcquisitionCANJaguarID, int acquisitionPressureSwitchID) {
        m_topAcquisitionCANJaguar = RobotCANJaguar.initializeCANJaguar(topAcquisitionCANJaguarID);
        m_bottomAcquisitionCANJaguar = RobotCANJaguar.initializeCANJaguar(bottomAcquisitionCANJaguarID);
        m_acquisitionPressureSwitch = new DigitalInput(acquisitionPressureSwitchID);
    }

    /**
     * Method for controlling the acquisition of the tube
     */
    public void acquire() {
        // Acquire until the pressure sensor trips (signals that the tube has been successfully acquired)
        if (!isAcquired()) {
            RobotCANJaguar.setX(m_topAcquisitionCANJaguar, ACQUIRE_SPEED);
            RobotCANJaguar.setX(m_bottomAcquisitionCANJaguar, ACQUIRE_SPEED);
        } else {
            stop();
        }
    }

    /**
     * Method for controlling the ejection of the tube
     */
    public void eject() {
        // Eject the tube
        RobotCANJaguar.setX(m_topAcquisitionCANJaguar, EJECT_SPEED);
        RobotCANJaguar.setX(m_bottomAcquisitionCANJaguar, EJECT_SPEED);
    }

    /**
     * Method for controlling the rotation of the tube in the acquisition unit
     *
     * @param direction Direction to rotate the tube
     */
    public void rotate(Direction direction) {
        if (direction.direction == Direction.kUp_val) {
            RobotCANJaguar.setX(m_topAcquisitionCANJaguar, ROTATE_SPEED);
            RobotCANJaguar.setX(m_bottomAcquisitionCANJaguar, -ROTATE_SPEED);
        } else {
            RobotCANJaguar.setX(m_topAcquisitionCANJaguar, -ROTATE_SPEED);
            RobotCANJaguar.setX(m_bottomAcquisitionCANJaguar, ROTATE_SPEED);
        }
    }

    /**
     * Method for stopping all motors in the acquisition unit
     */
    public void stop() {
        RobotCANJaguar.setX(m_topAcquisitionCANJaguar, 0);
        RobotCANJaguar.setX(m_bottomAcquisitionCANJaguar, 0);
    }

    public boolean isAcquired() {
        return m_acquisitionPressureSwitch.get();
    }
}