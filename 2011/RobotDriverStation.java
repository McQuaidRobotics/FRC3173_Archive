package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogModule;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.SolenoidBase;

/**
 *McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * This class combines the dashboard and the drivers station into one clean, easy to use, class.
 *
 * @author Joshua Kwiatkowski
 */
public class RobotDriverStation {

    DriverStation m_driverStation;
    DriverStationEnhancedIO m_enhancedIO;
    DriverStationLCD m_driverLCD;

    public RobotDriverStation() {
        m_driverStation = DriverStation.getInstance();
        m_enhancedIO = m_driverStation.getEnhancedIO();
        m_driverLCD = DriverStationLCD.getInstance();

        m_driverLCD.updateLCD();
    }

    /**
     * Updates the user messages on the Drive Station. After any changes to the user messages this
     * should be called, otherwise your changes will not show up.
     */
    public void update() {
        m_driverLCD.updateLCD();
        updateDashboard();
        clearMessages();
    }

    /**
     * Prints a line of text out to the user messages box on the drive station.
     *
     * @param The line on the LCD to print to. Must be proceeded by DriverStationLCD.Line.
     * @param text The string of text that will be printed. Only 21 characters will be printed.
     */
    public void outputLine(DriverStationLCD.Line line, String text) {
        m_driverLCD.println(line, 1, text);
    }

    /**
     * Sets a PWM value to appear on the Drive Station.
     *
     * @param channel the channel slot where the output value will show up.
     * @param value The value that will appear. Must be between 0 and 255 inclusive.
     */
    public void setPWM(int channel, int value) {
        DigitalModule.getInstance(4).setPWM(channel,value);
    }

    /**
     * Sets whether a relay is forward or not.
     *
     * @param relay The number of the relay to change.
     * @param value True forward, false not forward.
     */
    public void setRelayForward(int relay, boolean value) {
        DigitalModule.getInstance(4).setRelayForward(relay, value);
    }

    /**
     * Sets whether a relay is backward or not.
     *
     * @param relay the number of the relay to change
     * @param value True reverse, false not
     */
    public void setRelayReverse(int relay, boolean value) {
        DigitalModule.getInstance(4).setRelayReverse(relay, value);
    }

    /**
     * Gets all the values on the dashboard and updates the graphs on the dashboard.
     */
    private void updateDashboard() {
        Dashboard lowDashData = DriverStation.getInstance().getDashboardPackerLow();
        lowDashData.addCluster();
        {
            lowDashData.addCluster();
            {     //analog modules
                lowDashData.addCluster();
                {
                    for (int i = 1; i <= 8; i++) {
                        lowDashData.addFloat((float) AnalogModule.getInstance(1).getAverageVoltage(i));
                    }
                }
                lowDashData.finalizeCluster();
                lowDashData.addCluster();
                {
                    for (int i = 1; i <= 8; i++) {
                        lowDashData.addFloat((float) AnalogModule.getInstance(2).getAverageVoltage(i));
                    }
                }
                lowDashData.finalizeCluster();
            }
            lowDashData.finalizeCluster();

            lowDashData.addCluster();
            { //digital modules
                lowDashData.addCluster();
                {
                    lowDashData.addCluster();
                    {
                        int module = 4;
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
                        lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
                        lowDashData.addCluster();
                        {
                            for (int i = 1; i <= 10; i++) {
                                lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
                            }
                        }
                        lowDashData.finalizeCluster();
                    }
                    lowDashData.finalizeCluster();
                }
                lowDashData.finalizeCluster();

                lowDashData.addCluster();
                {
                    lowDashData.addCluster();
                    {
                        int module = 6;
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayReverse());
                        lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
                        lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
                        lowDashData.addCluster();
                        {
                            for (int i = 1; i <= 10; i++) {
                                lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
                            }
                        }
                        lowDashData.finalizeCluster();
                    }
                    lowDashData.finalizeCluster();
                }
                lowDashData.finalizeCluster();

            }
            lowDashData.finalizeCluster();

            lowDashData.addByte(SolenoidBase.getAllFromModule(8));

        }
        lowDashData.finalizeCluster();
        lowDashData.commit();
    }
    
    /**
     * Clears all the lines on the drive station.
     */
    private void clearMessages() {
        m_driverLCD.println(DriverStationLCD.Line.kMain6, 1, "                                               ");
        m_driverLCD.println(DriverStationLCD.Line.kUser2, 1, "                                               ");
        m_driverLCD.println(DriverStationLCD.Line.kUser3, 1, "                                               ");
        m_driverLCD.println(DriverStationLCD.Line.kUser4, 1, "                                               ");
        m_driverLCD.println(DriverStationLCD.Line.kUser5, 1, "                                               ");
        m_driverLCD.println(DriverStationLCD.Line.kUser6, 1, "                                               ");
    }
}

