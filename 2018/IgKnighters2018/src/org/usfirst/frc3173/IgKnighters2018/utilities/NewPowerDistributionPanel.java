package org.usfirst.frc3173.IgKnighters2018.utilities;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.hal.PDPJNI;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * Class for getting voltage, current, temperature, power and energy from the Power Distribution
 * Panel over CAN.
 */
public class NewPowerDistributionPanel extends SensorBase implements Sendable {
  private final int m_module;

  /**
   * Constructor.
   *
   * @param module The CAN ID of the PDP
   */
  public NewPowerDistributionPanel(int module) {
    m_module = module;
    checkPDPModule(module);
    PDPJNI.initializePDP(module);
    setName("PowerDistributionPanel", module);
  }

  /**
   * Constructor.  Uses the default CAN ID (0).
   */
  public NewPowerDistributionPanel() {
    this(0);
  }

  /**
   * Query the input voltage of the PDP.
   *
   * @return The voltage of the PDP in volts
   */
  public double getVoltage() {
    return PDPJNI.getPDPVoltage(m_module);
  }

  /**
   * Query the temperature of the PDP.
   *
   * @return The temperature of the PDP in degrees Celsius
   */
  public double getTemperature() {
    return PDPJNI.getPDPTemperature(m_module);
  }

  /**
   * Query the current of a single channel of the PDP.
   *
   * @return The current of one of the PDP channels (channels 0-15) in Amperes
   */
  public double getCurrent(int channel) {
    double current = PDPJNI.getPDPChannelCurrent((byte) channel, m_module);

    checkPDPChannel(channel);

    return current;
  }

  /**
   * Query the current of all monitored PDP channels (0-15).
   *
   * @return The current of all the channels in Amperes
   */
  public double getTotalCurrent() {
    return 0; //PDPJNI.getPDPTotalCurrent(m_module); THIS LINE WAS THE ERROR INDUCING LINE AHHHH
  }

  /**
   * Query the total power drawn from the monitored PDP channels.
   *
   * @return the total power in Watts
   */
  public double getTotalPower() {
    return PDPJNI.getPDPTotalPower(m_module);
  }

  /**
   * Query the total energy drawn from the monitored PDP channels.
   *
   * @return the total energy in Joules
   */
  public double getTotalEnergy() {
    return PDPJNI.getPDPTotalEnergy(m_module);
  }

  /**
   * Reset the total energy to 0.
   */
  public void resetTotalEnergy() {
    PDPJNI.resetPDPTotalEnergy(m_module);
  }

  /**
   * Clear all PDP sticky faults.
   */
  public void clearStickyFaults() {
    PDPJNI.clearPDPStickyFaults(m_module);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("PowerDistributionPanel");
    for (int i = 0; i < kPDPChannels; ++i) {
      final int chan = i;
      builder.addDoubleProperty("Chan" + i, () -> getCurrent(chan), null);
    }
    builder.addDoubleProperty("Voltage", this::getVoltage, null);
    builder.addDoubleProperty("TotalCurrent", this::getTotalCurrent, null);
  }
}