package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;
import java.util.Random;

/**
 * McQuaid IgKnighters Robotics
 * FIRST Team 3173
 * 2011 Season
 * Logomotion
 *
 * Primary class for controlling the robot's signal lights subsystem
 *
 * @author Nick Brown and Joshua Kwiatkowski
 */
public class RobotSignalLighting {

    /**
     * Constants associated with particular lights
     */
    public static class Light {

        public int light;
        static final int kRedLight_val = 0;
        static final int kWhiteLight_val = 1;
        static final int kBlueLight_val = 2;
        public static final Light kRedLight = new Light(kRedLight_val);
        public static final Light kWhiteLight = new Light(kWhiteLight_val);
        public static final Light kBlueLight = new Light(kBlueLight_val);

        private Light(int light) {
            this.light = light;
        }
    }

    /**
     * Constant associated with particular states
     */
    public static class State {

        public boolean state;
        static final boolean kOn_val = true;
        static final boolean kOff_val = false;
        public static final State kOn = new State(kOn_val);
        public static final State kOff = new State(kOff_val);

        private State(boolean state) {
            this.state = state;
        }
    }
    private Relay m_redLight;
    private Relay m_whiteLight;
    private Relay m_blueLight;

    /**
     * Constructor for use with pre-initialized relays
     *
     * @param redLight Relay assigned to red light
     * @param whiteLight Relay assigned to white light
     * @param blueLight Relay assigned to blue
     */
    public RobotSignalLighting(Relay redLight, Relay whiteLight, Relay blueLight) {

        m_redLight = redLight;
        m_whiteLight = whiteLight;
        m_blueLight = blueLight;

        setupRelays();
    }

    /**
     * Constructor for use with relay IDs
     *
     * @param redLightID Red light relay ID
     * @param whiteLightID White light relay ID
     * @param blueLightID Blue light relay ID
     */
    public RobotSignalLighting(int redLightID, int whiteLightID, int blueLightID) {
        m_redLight = new Relay(redLightID);
        m_whiteLight = new Relay(whiteLightID);
        m_blueLight = new Relay(blueLightID);

        setupRelays();
    }

    /**
     * Properly configures each of the lighting relays
     */
    private void setupRelays() {

        m_redLight.setDirection(Relay.Direction.kForward);
        m_whiteLight.setDirection(Relay.Direction.kForward);
        m_blueLight.setDirection(Relay.Direction.kForward);
    }

    /**
     * Changes the state of a light
     *
     * @param light Light to change the state of
     * @param state State to switch the light to
     */
    public void set(Light light, State state) {
        if (state.state == State.kOn_val) {
            switch (light.light) {
                case Light.kRedLight_val:
                    m_redLight.set(Relay.Value.kOn);
                    break;

                case Light.kWhiteLight_val:
                    m_whiteLight.set(Relay.Value.kOn);
                    break;

                case Light.kBlueLight_val:
                    m_blueLight.set(Relay.Value.kOn);
                    break;
            }
        } else {
            switch (light.light) {
                case Light.kRedLight_val:
                    m_redLight.set(Relay.Value.kOff);
                    break;

                case Light.kWhiteLight_val:
                    m_whiteLight.set(Relay.Value.kOff);
                    break;

                case Light.kBlueLight_val:
                    m_blueLight.set(Relay.Value.kOff);
                    break;
            }
        }
    }

    /**
     * Changes the state of a light
     *
     * @param light Light to change the state of
     * @param state State to switch the light to (True = On; False = Off)
     */
    public void set(Light light, boolean state) {
        if (state) {
            set(light, State.kOn); // Set to on if state is true
        } else {
            set(light, State.kOff); // Set to off if state is false
        }
    }

    /**
     * Turns all of the lights on or off
     *
     * @param state State to switch the light to (True = on; False = Off)
     */
    public void setAll(State state) {
        if (state.state == State.kOn_val) {
            set(Light.kBlueLight, State.kOn);
            set(Light.kRedLight, State.kOn);
            set(Light.kWhiteLight, State.kOn);
        } else {
            set(Light.kBlueLight, State.kOff);
            set(Light.kRedLight, State.kOff);
            set(Light.kWhiteLight, State.kOff);
        }
    }

    /**
     * Turns all lights off and sets only the specified light to the specified state
     *
     * @param light Light to change the state of
     * @param state State to switch the light to (True = On; False = Off)
     */
    public void setOnly(Light light, State state) {
        setAll(State.kOff);
        set(light, state);
    }

    /**
     * Randomly turns on one of the lights
     *
     * *Beware of running this in a fast loop!
     */
    public void randomBling() {
        Random rnd = new Random();
        int rand = rnd.nextInt(3);
        if (rand == 0) {
            set(Light.kBlueLight, State.kOn);
            set(Light.kRedLight, State.kOff);
            set(Light.kWhiteLight, State.kOff);
        } else if (rand == 1) {
            set(Light.kBlueLight, State.kOff);
            set(Light.kRedLight, State.kOn);
            set(Light.kWhiteLight, State.kOff);
        } else {
            set(Light.kBlueLight, State.kOff);
            set(Light.kRedLight, State.kOff);
            set(Light.kWhiteLight, State.kOn);
        }
    }
    public void benjaminsrandombling(){
     Random rand = new Random();
        int whiternd =rand.nextInt(2);
        int bluernd =rand.nextInt(2);
        int redrnd =rand.nextInt(2);
        set(Light.kWhiteLight, new State(whiternd == 0));
        set(Light.kBlueLight, new State(bluernd == 0));
        set(Light.kRedLight, new State(redrnd == 0));
    }
}
