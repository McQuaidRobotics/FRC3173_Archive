package frc.robot.util;

public class Timekeeper {
    long lastTime;
    public Timekeeper() {
        lastTime = System.currentTimeMillis();
    }
    public double tick() {
        double dt = (double) System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        return dt;
    }
}