package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Represents the Tilter subsystem on the robot.
 * This is represented as a finite state machine, which makes considerably more
 * sense than the tangle that was before. (Previously it was almost--but not
 * quite--a FSM, and what happened where when the status of which bit was what
 * was never quite clear. See Subversion if you're really that interested.
 * Making it a FSM also fixed a number of behavioural oddities in the system--
 * it's always quite clear what state it should be in, whereas before the state
 * was inferred from several different sources, with interesting (to say the least)
 * results if, for example, a limit switch was missing.)
 * @author (Benjamin Pylko) + (Wolfgang Faust)
 */
public class Tilter {
	public static final int STATUS_HALTED = 0;
	public static final int STATUS_UP = 1;
	public static final int STATUS_LOWERING = 2;
	public static final int STATUS_DOWN = 3;
	public static final int STATUS_UNCLAMPING = 4;
	public static final int STATUS_RAISING = 5;
	public static final String[] statusTexts = {"Halted","Retracted","Deploying","Deployed","Unclamping","Retracting"};

	private static final double M_JAGUAR_SPEED = 0.3;
	private static final double M_JAGUAR_LOWER = -M_JAGUAR_SPEED;
	private static final double M_JAGUAR_RAISE = M_JAGUAR_SPEED;
	private static final double M_JAGUAR_STOP = 0;
	private static final double M_CLAMP_POS_CLAMPED = 1;
	private static final double M_CLAMP_POS_UNCLAMPED = 0;
	private static final long M_CLAMP_DELAY = 100; //510; // Time to clamp in millis
	private static final long M_TIMEOUT = 5000; // Time in millis before the tilter times out & halts

	private RobotJaguar m_tilterJaguar;
	//private Servo m_clampSolenoid;
	protected Solenoid m_clampSolenoid;
	protected Relay m_clampRelay;
	private int m_state;
	private int m_lastUserSetState; // Last state which was set by the user.
	private int m_lastLoopState; // State last time doTilting() was called.
	private long m_time;

    /**
     * Constructs a new Tilter object.
     */
    public Tilter(){
        m_tilterJaguar = new RobotJaguar(ElectricalIDs.CANJaguars.TILTING_JAGUAR);
		if (ElectricalIDs.pneumatic_solenoid) {
			m_clampSolenoid = new Solenoid(ElectricalIDs.Solenoid.TILTER_CLAMP);
		} else {
			m_clampRelay = new Relay(ElectricalIDs.Relay.TILTER_CLAMP);
			m_clampRelay.setDirection(Relay.Direction.kForward);
		}
    }
	/**
	 * Cycle the state machine and go to the next state if needed. This MUST be
	 * called periodically (eg in a loop) for the Tilter to work properly.
	 */
	public void doTilting() {
		/* To future programmers: THESE SHOULD NOT BE ELSE-IFS!
		 * The state may change several times during the course of a cycle.
		 * (Trace what happens when m_state = STATUS_LOWERING and a limit switch
		 * is hit, for example.)
		 */
		if (m_state == STATUS_HALTED) {
			m_tilterJaguar.setX(M_JAGUAR_STOP);
			//m_clamp.setRaw(0); // Setting raw to 0 stops the signals
		}
		if (m_state == STATUS_LOWERING) {
			m_tilterJaguar.setX(M_JAGUAR_LOWER);
			if (tiltedOut()) m_state = STATUS_DOWN;
		}
		if (m_state == STATUS_DOWN) {
			m_tilterJaguar.setX(M_JAGUAR_STOP);
			//m_clamp.setPosition(M_CLAMP_POS_CLAMPED);
			if (ElectricalIDs.pneumatic_solenoid) {
				m_clampSolenoid.set(true);
			} else {
				m_clampRelay.set(Relay.Value.kOn);
			}
		}
		if (m_state == STATUS_UNCLAMPING) {
			//m_clamp.setPosition(M_CLAMP_POS_UNCLAMPED);
			if (ElectricalIDs.pneumatic_solenoid) {
				m_clampSolenoid.set(false);
			} else {
				m_clampRelay.set(Relay.Value.kOff);
			}
			if (System.currentTimeMillis() >= m_time + M_CLAMP_DELAY) m_state = STATUS_RAISING;
		}
		if (m_state == STATUS_RAISING) {
			m_tilterJaguar.setX(M_JAGUAR_RAISE);
			if (tiltedIn()) m_state = STATUS_UP;
		}
		if (m_state == STATUS_UP) {
			m_tilterJaguar.setX(M_JAGUAR_STOP);
		}
		/* Now: If the Tilter stays in the same state for more than M_TIMEOUT
		 * milliseconds, it will be halted. This way, if a limit switch is
		 * borken or something goes otherwise haywire, it can only do so for 5
		 * seconds before it stops.
		 */
		if (m_state == m_lastLoopState && m_state != STATUS_HALTED) {
			if (System.currentTimeMillis() >= m_time + M_TIMEOUT) halt();
			Log.out.println("Tilter timed out and was halted.");
		} else {
			m_lastLoopState = m_state;
			m_time=System.currentTimeMillis();
		}
	}
	/**
	 * Try to set the tilter position. Takes into account whether we are already
	 * in the state, whether the state should be entered currently, etc, then
	 * sets the state accordingly.
	 * @param out True if the tilter should be out. (Use M_TILTERPOS variables for readability)
	 * @return Whether the state was entered.
	 */
	private boolean setTilterPos(boolean out) {
		// If going out, then begin lowering; otherwise begin unclamping (unclamp must be applied before raise.)
		int state = out?STATUS_LOWERING:STATUS_UNCLAMPING;
		// Generally, there are no incompatible states since everything
		// sorts itself out. If incompatible states are found, put checks for
		// them here and return false.
		if (state == m_lastUserSetState) return false; // We set this state last time, no need to again
		// If we get here, we should set the state since we passed all of the
		// above tests.
		m_state = state;
		m_time = System.currentTimeMillis();
		m_lastUserSetState = state; // Remember that this was the last state set
		doTilting(); // Apply this state
		Log.out.println("State applied"+m_state);
		return true;
	}
	private static final boolean M_TILTERPOS_IN = false;
	private static final boolean M_TILTERPOS_OUT = true;
    /**
     * Initiate the tilting out sequence.
     */
    public void tiltOut(){
        setTilterPos(M_TILTERPOS_OUT);
    }
    /**
     * Initiate the tilting in seqence.
     */
    public void tiltIn(){
        setTilterPos(M_TILTERPOS_IN);
    }
	/**
	 * Get the current status of the tilter.
	 * @return The status of the tilter.
	 */
	public int status() {
		return m_state;
	}
	/**
	 * Get a description of the current Tilter status.
	 * @return A one-word description of the status.
	 */
	public String statusText() {
		return statusTexts[m_state];
	}
	/**
	 * Halts the tilter motor and servo in case of emergency.
	 * IMPORTANT: under most circumstances, the limit switches handle this
	 * automatically in hardware; this should only be called to E-stop.
	 */
	public void halt() {
		m_state = STATUS_HALTED;
		doTilting();
	}
    /**
     * Checks whether the tilter is all the way out.
	 * @return Whether the limit switch has been contacted.
     */
    private boolean tiltedOut() {
        return !m_tilterJaguar.getReverseLimitOK();
    }
    /**
     * Checks whether the tilter is all the way in.
	 * @return Whether the limit switch has been contacted.
     */
    private boolean tiltedIn(){
        return !m_tilterJaguar.getForwardLimitOK();
    }
}
