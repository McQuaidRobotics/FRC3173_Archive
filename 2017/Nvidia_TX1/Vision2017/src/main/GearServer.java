package main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class GearServer {
	private static VideoCapture camera;
	private static Mat frame;
	private static ArrayList<Rect> rects = new ArrayList<Rect>();
	private static Rect[] nrects;
	private static GripPipeline grip = new GripPipeline();
	private static double distance, ratio;
	private static int portNumber = 1185;
	private static ServerSocket server;
	private static Socket socket;
	private static DataOutputStream dOut;
	private static long lastConnected;
	
	public static void main(String[] args) {
		initCamera();
		connect();
		while(true) {
			if (socket.isConnected()) {
				update();
				send(distance, ratio);
				lastConnected = System.currentTimeMillis();
			} else {
				if (System.currentTimeMillis() - lastConnected >= 150000) {
					System.out.println("Timed out.");
					close();
					System.exit(0);
				} else {
					System.out.println("Attempting to reconnect..."); //league hehe
					connect();
				}
			}
		}
	}
	
	private static void connect() {
		try {;
			System.out.println("Listening on port: " + portNumber);	
			server = new ServerSocket(1185);
			socket = server.accept();
			System.out.println("Connection established.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void close() {
		try {
			socket.close();
			server.close();
			camera.release();
		} catch (Exception ex) {
			System.out.println("Error attempting to close.");
			ex.printStackTrace();
		}
	}
	
	private static void send(double d, double r) {
		try {
			dOut.writeInt((int)Math.round(d));
			dOut.writeDouble(r);
			dOut.flush();
		} catch (IOException ex) {
			System.out.println("Error sending double.");
			ex.printStackTrace();
		}
	}
	
	private static void initCamera() {
		System.out.println("Hello, OpenCV");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		frame = new Mat();
		camera = new VideoCapture(0);
		System.out.println("Camera opened successfully.");	
	}
	
	private static void update() {
		camera.read(frame);
		grip.process(frame);
		frame = grip.cvDilateOutput();
		MatOfPoint2f approxCurve = new MatOfPoint2f();
		List<MatOfPoint> contours = grip.filterContoursOutput();
		if (contours.size() >= 2) {
			for (int i = 0; i < contours.size(); i++) {
				MatOfPoint2f contour2f = new MatOfPoint2f(grip.filterContoursOutput().get(i).toArray());
				double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
				Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
				MatOfPoint points = new MatOfPoint(approxCurve.toArray());
				Rect rect = Imgproc.boundingRect(points);
				if (rect.area() > 10000) rects.add(rect);
				if (grip.filterContoursOutput().size() == 2 && rects.size() == 2 && i == 1) {
					nrects = sortRect(rects);
					distance = pixToInch((nrects[0].height + nrects[1].height)/2);
					ratio = nrects[1].height / nrects[0].height;
					rects.clear();
				}
			}
		} else {
			distance = -1;
			ratio = -1;
		}
	}
	
	private static double pixToInch(double inPix) {
		return (((368 / 1652145.0) * Math.pow(inPix, 2)) - ((128076 / 550715.0) * inPix) + (2171632 / 30039.0));
	}
	
	private static Rect[] sortRect(ArrayList<Rect> in) {
		Rect[] out = new Rect[2];
		if (in.get(0).x < in.get(1).x) {
			out[0] = in.get(0);
			out[1] = in.get(1);
		} else {
			out[0] = in.get(1);
			out[1] = in.get(0);
		}
		return out;
	}
}
