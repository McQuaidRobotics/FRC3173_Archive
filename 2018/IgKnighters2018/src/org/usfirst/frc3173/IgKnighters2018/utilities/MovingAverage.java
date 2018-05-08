package org.usfirst.frc3173.IgKnighters2018.utilities;

import java.util.ArrayList;

public class MovingAverage {
	private ArrayList<Double> list;
	private double lastAverage;
	private double deltaAverage;
	public MovingAverage() {
		list = new ArrayList<Double>();
		lastAverage = 0;
		deltaAverage = 0;
	}
	public void update(double val) {
		list.add(val);
		if (list.size() > 5) list.remove(0);
		double currentAverage = getAverage();
		deltaAverage = lastAverage - currentAverage;
		lastAverage = currentAverage;
	}
	public double getDelta() {
		return deltaAverage;
	}
	public double getAverage() {
		double sum = 0;
		for (double num : list)
			sum += num;
		return (sum / list.size());
	}
}
