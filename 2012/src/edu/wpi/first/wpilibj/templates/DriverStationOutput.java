package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 * Provides a simple interface to the driver station and LCD display.
 * 
 * Based loosely on the 2011 code by Josh Kwiatkowski.
 * 
 * McQuaid Jesuit IgKnighters -- FIRST Robotics Team 3173
 * http://igknighters.com
 * 2012 Season
 * 
 * @author Wolfgang Faust
 */
public class DriverStationOutput {
    /**
     * Whether or not to automatically update the display, etc.
     */
    private boolean m_autoUpdate=true;
    /**
     * Whether or not to automatically clear the message display.
     */
    private boolean m_autoClearMessages=false;
    /**
     * The LCD
     */
    private static DriverStationLCD m_LCD = DriverStationLCD.getInstance();
    /**
     * Normalized lines of the LCD
     * @todo document ALL of these!
     * @todo is this a good name?
     */
    public static final DriverStationLCD.Line LINE_MAIN=DriverStationLCD.Line.kMain6;
    public static final DriverStationLCD.Line LINE_0=LINE_MAIN;
    public static final DriverStationLCD.Line LINE_1=DriverStationLCD.Line.kUser2;
    public static final DriverStationLCD.Line LINE_2=DriverStationLCD.Line.kUser3;
    public static final DriverStationLCD.Line LINE_3=DriverStationLCD.Line.kUser4;
    public static final DriverStationLCD.Line LINE_4=DriverStationLCD.Line.kUser5;
    public static final DriverStationLCD.Line LINE_5=DriverStationLCD.Line.kUser6;
    /**
     * This array holds the same normalized LCD lines as the list above, only as an array.
     */
    public static final DriverStationLCD.Line[] LINES={LINE_0,LINE_1,LINE_2,LINE_3,LINE_4,LINE_5};
    /**
     * Print a line to the message display.
     *
     * @param line the DriverStationLCD line to print to
     * @param col the column begin printing at.
     * @param text The string to print. Only the first 21 characters will echo.
     */
    public void println(DriverStationLCD.Line line, int col, String text) {
        doClearln(line);
        doPrintln(line,col,text);
        autoUpdate();
    }
    /**
     * Actually print the line; don't update
     * @param line the DriverStationLCD line to print to
     * @param col the column begin printing at.
     * @param text The string to print. Only the first 21 characters will echo.
     */
    private void doPrintln(DriverStationLCD.Line line, int col, String text) {
        m_LCD.println(line, col, text);
    }
    /**
     * Print a line to the message display, beginning at column 1
     * @param line the DriverStationLCD line to print to
     * @param text The string to print. Only the first 21 characters will echo.
     */
    public void println(DriverStationLCD.Line line, String text) {
        println(line,1,text);
    }
    /**
     * Print a line to the message display
     * @param lineNo The line to print the message on, as an int
     * @param col the column begin printing at.
     * @param text The string to print. Only the first 21 characters will echo.
     */
    public void println(int lineNo,int col, String text){
        println(LINES[lineNo],col,text);
    }
    /**
     * Print a line to the message display, beginning at column 1
     * @param lineNo The line to print the message on, as an int
     * @param text The string to print. Only the first 21 characters will echo.
     */
    public void println(int lineNo,String text){
        println(LINES[lineNo],text);
    }
    /**
     * Clear a message line
     * @param line The DriverStationLCD line to clear
     */
    public void clearln(DriverStationLCD.Line line) {
        doClearln(line);
        autoUpdate();
    }
    /**
     * Actually clear the line, but don't update.
     * @param line 
     */
    private void doClearln(DriverStationLCD.Line line) {
        doPrintln(line,1,"                                               ");
    }
    /**
     * Clear a message line
     * @param lineNo The line to clear, as an int
     */
    public void clearln(int lineNo) {
        clearln(LINES[lineNo]);
    }
    /**
     * Clear all of the displayed messages.
     * Actually just calls another function to clear, then updates display.
     */
    public void clearMessages() {
        doClearMessages();
        autoUpdate();
    }
    /**
     * Actually clear the messages; don't autoUpdate().
     */
    private void doClearMessages() {
        clearln(LINE_0);
        clearln(LINE_1);
        clearln(LINE_2);
        clearln(LINE_3);
        clearln(LINE_4);
        clearln(LINE_5);
    }
    /**
     * Clear the messages if autoClearMessages is true
     */
    private void autoClearMessages() {
        if (m_autoClearMessages) {
            doClearMessages();
        }
    }
    /**
     * Set whether the message display should be cleared automatically
     * Note: on autoClearMessages(true) the display is cleared
     * @param clear update whether to clear automatically
     */
    public void autoClearMessages(boolean clear) {
        m_autoClearMessages=clear;
        autoClearMessages();
    }
    /**
     * Set whether the driver station should update automatically
     * Note: on autoUpdate(true) the station is updated
     * @param update whether to update automatically
     */
    public void autoUpdate(boolean update) {
        m_autoUpdate=update;
        autoUpdate();
    }
    /**
     * Update the driver station if autoUpdate is true
     */
    private void autoUpdate() {
        if (m_autoUpdate) {
            update();
        }
    }
    /**
     * Update the driver station.
     * Note that the 2011 code required this to be called after every change.
     * This code does this automatically if you call autoUpdate(true); this is on by default as well.
     */
    public void update() {
        m_LCD.updateLCD();
        updateDashboard();
        autoClearMessages();
    }
    /**
     * Some magic code from the example which supposedly works & updates
     * the driver station.
     * Changed from private to private because update() should be used instead.
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
                        int module = 1;
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
                        int module = 2;
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

            lowDashData.addByte(Solenoid.getAllFromDefaultModule());
        }
        lowDashData.finalizeCluster();
        lowDashData.commit();

    }
}
