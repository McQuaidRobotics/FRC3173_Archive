package org.usfirst.frc3173.IgKnighters2018.deprecated;

public class RigidBody {
	
	private Vector2 position;
	
	public RigidBody(Vector2 pos) {
		this.position = pos;
	}
	
	public static int distanceInTicks(float distance, float wheelDiameter, float tickPerRev) {
		float ticksPerWheelDiameter = tickPerRev / wheelDiameter;
		return (int)(ticksPerWheelDiameter * distance);
	}
	
	/*
	 * Returns the ideal delta angle to be turned by robot to head towards target.
	 */
	public float calculateDeltaIdealHeading(double currentHeading, Vector2 target) {
		//first calculate the angle from here to the target
		double delta_x = target.x() - position.x();
		double delta_y = target.y() - position.y();
		double idealAngle = Math.toDegrees(Math.atan(delta_x / delta_y));
		double deltaAngle = idealAngle - currentHeading;
		return (float)deltaAngle;
	}
	
	public float calculateIdealHeading(double currentHeading, Vector2 target) {
		//first calculate the angle from here to the target
		double delta_x = target.x() - position.x();
		double delta_y = target.y() - position.y();
		double idealAngle = Math.toDegrees(Math.atan(delta_x / delta_y));
		return (float)idealAngle;
	}
	
	/*
	 * calculates distance to target using the distance formula
	 */
	public float distanceToTarget(Vector2 target) {
		double underRoot = Math.pow(target.y() - position.y(), 2) + Math.pow(target.x() - position.x(), 2);
		return (float)Math.sqrt(underRoot);
	}
	
	public Vector2 pos() { return position; }
	
	public void add(Vector2 amount) { position.add(amount); }
	
	@Override
	public String toString() {
		return "[RigidBody with pos(" + position.x() + ", " + position.y() + ")]";
	}
}
