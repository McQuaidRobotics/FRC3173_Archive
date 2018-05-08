package org.usfirst.frc3173.IgKnighters2018.deprecated;

public class Vector2 {
	private float x, y;
	public Vector2(float x, float y) {
		this.x = x; this.y = y;
	}
	public float y() { return y; }
	public float x() { return x; }
	public void add(Vector2 in) {
		x += in.x(); y += in.y();
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
