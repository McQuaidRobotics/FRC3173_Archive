package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * Custom class for the sound-based Proximity Sensors
 *
 * @author Nick Brown
 */
public class ProximitySensor {

    private AnalogChannel m_analog;
    private final double VCC = 5.0; // Voltage to sensor [Measured in Volts]
    private final double SCALING_FACTOR = VCC / 512; // Scaling factor is (Vcc / 512) per inch [Measured in Volts/Inch)

    /**
     * Initializes a Proximity Sensor at the specified channel number
     *
     * @param channel Number of the analog channel the sensor is attached to
     */
    public ProximitySensor(int channel) {
        m_analog = new AnalogChannel(channel);
    }

    /**
     * Initializes a Proximity Sensor using a pre-initialized AnalogChannel
     *
     * @param channel Initialized AnalogChannel at the channel the sensor is attached to
     */
    public ProximitySensor(AnalogChannel channel) {
        m_analog = channel;
    }

    /**
     * Gets the distance to any solid object currently oriented in front of the proximity sensor
     * 
     * @return Double: Distance to the object in inches
     */
    public double getDistance() {
        return m_analog.getVoltage() / SCALING_FACTOR; // Divides the current voltage returned from the sensor by the scaling factor
    }
}