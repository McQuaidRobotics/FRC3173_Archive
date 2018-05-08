/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.parsing.ISensor;

/**
 * Handle operation of the accelerometer.
 * The accelerometer reads acceleration directly through the sensor. Many sensors have
 * multiple axis and can be treated as multiple devices. Each is calibrated by finding
 * the center value over a period of time.
 */
public class RobotAccelerometer extends SensorBase implements PIDSource, ISensor {

    static final int kOversampleBits = 0;
    static final int kAverageBits = 0;
    static final double kSamplesPerSecond = 50.0;
    static final double kCalibrationSampleTime = 5.0;
    static final double kDefaultVoltsPerMeterPerSecond = 3;
    private AnalogChannel m_analogChannel;
    private double m_voltsPerG = 1.0;
    private double m_zeroGVoltage = 2.5;
    private double m_offset;
    private boolean m_allocatedChannel;
    private double m_voltsPerMeterPerSecond;
    private AccumulatorResult result;

    /**
     * Create a new instance of an accelerometer.
     *
     * The accelerometer is assumed to be in the first analog module in the given analog channel. The
     * constructor allocates desired analog channel.
     * @param channel the port that the accelerometer is on on the default module
     */
    public RobotAccelerometer(final int channel) {
        m_allocatedChannel = true;
        m_analogChannel = new AnalogChannel(channel);
        initAccelerometer();
    }
    private void initAccelerometer() {
        result = new AccumulatorResult();
        if (m_analogChannel == null) {
            System.out.println("Null m_analog");
        }
        m_voltsPerMeterPerSecond = kDefaultVoltsPerMeterPerSecond;
        m_analogChannel.setAverageBits(kAverageBits);
        m_analogChannel.setOversampleBits(kOversampleBits);
        double sampleRate = kSamplesPerSecond * (1 << (kAverageBits + kOversampleBits));
        m_analogChannel.getModule().setSampleRate(sampleRate);

        Timer.delay(1.0);
        m_analogChannel.initAccumulator();

        Timer.delay(kCalibrationSampleTime);

        m_analogChannel.getAccumulatorOutput(result);

        int center = (int) ((double)result.value / (double)result.count + m_zeroGVoltage);

        m_offset = ((double)result.value / (double)result.count) - (double)center;

        m_analogChannel.setAccumulatorCenter(center);

        m_analogChannel.setAccumulatorDeadband(0); ///< TODO: compute / parameterize this
        m_analogChannel.resetAccumulator();
    }

    /**
     * Create new instance of accelerometer.
     *
     * Make a new instance of the accelerometer given a module and channel. The constructor allocates
     * the desired analog channel from the specified module
     * @param slot the slot that the module is in
     * @param channel the port that the Accelerometer is on on the module
     */
    public RobotAccelerometer(final int slot, final int channel) {
        m_allocatedChannel = true;
        m_analogChannel = new AnalogChannel(slot, channel);
    }

    /**
     * Create a new instance of Accelerometer from an existing AnalogChannel.
     * Make a new instance of accelerometer given an AnalogChannel. This is particularly
     * useful if the port is going to be read as an analog channel as well as through
     * the Accelerometer class.
     * @param channel an already initialized analog channel
     */
    public RobotAccelerometer(AnalogChannel channel) {
        m_allocatedChannel = false;
        if (channel == null)
            throw new NullPointerException("Analog Channel given was null");
        m_analogChannel = channel;
    }

    /**
     * Delete the analog components used for the accelerometer.
     */
    public void free() {
        if (m_analogChannel != null && m_allocatedChannel) {
            m_analogChannel.free();
        }
        m_analogChannel = null;
    }

    /**
     * Return the acceleration in Gs.
     *
     * The acceleration is returned units of Gs.
     *
     * @return The current acceleration of the sensor in Gs.
     */
    public double getAcceleration() {
        return (m_analogChannel.getAverageVoltage() - m_zeroGVoltage) / m_voltsPerG;
    }
    /**
     * Returns the velocity in meters/second.
     * 
     * The acceleration returned units of meters/second.
     * 
     * @return The current velocity of the sensor in meters/second.
     */
    public double getVelocity(){
        if (m_analogChannel == null) {
            return 0.0;
        } else {
            m_analogChannel.getAccumulatorOutput(result);

            long value = result.value - (long) (result.count * m_offset);

            double scaledValue = value * 1e-9 * m_analogChannel.getLSBWeight() * (1 << m_analogChannel.getAverageBits()) /
                    (m_analogChannel.getModule().getSampleRate() * m_voltsPerMeterPerSecond);

            return scaledValue;
        }
    }

    /**
     * Set the accelerometer sensitivity.
     *
     * This sets the sensitivity of the accelerometer used for calculating the acceleration.
     * The sensitivity varys by accelerometer model. There are constants defined for various models.
     *
     * @param sensitivity The sensitivity of accelerometer in Volts per G.
     */
    public void setSensitivity(double sensitivity) {
        m_voltsPerG = sensitivity;
    }

    /**
     * Set the voltage that corresponds to 0 G.
     *
     * The zero G voltage varys by accelerometer model. There are constants defined for various models.
     *
     * @param zero The zero G voltage.
     */
    public void setZero(double zero) {
        m_zeroGVoltage = zero;
    }

    /**
     * Get the Acceleration for the PID Source parent.
     *
     * @return The current acceleration in Gs.
     */
    public double pidGet() {
        return getAcceleration();
    }
}
