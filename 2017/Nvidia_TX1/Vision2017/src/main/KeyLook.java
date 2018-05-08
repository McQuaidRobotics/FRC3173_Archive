package main;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.features2d.*;

import java.util.Scanner;

public class KeyLook {
	private static VideoCapture camera;
	private static Mat frame;
	private static JFrame window;
	private static JLabel label;
	private static ImageIcon image;
	private static ArrayList<Rect> rects = new ArrayList<Rect>();
	private static Rect[] nrects;
	private static long lastTime;
	private static GripPipeline grip = new GripPipeline();
	private static double distance;
	public static void main(String[] args) {
		System.out.println("Hello, OpenCV");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		initDisplay();
		frame = new Mat();
		int cameraNum = 0;
		camera = new VideoCapture(cameraNum);
		sleep(500);
		while (camera.isOpened()) {
			lastTime = System.currentTimeMillis();
			camera.read(frame);
			Imgproc.resize(frame, frame, new Size(1280,960));
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
					Imgproc.rectangle(frame, new Point(rect.x,rect.y), new Point(rect.x+rect.width, rect.y+rect.height), new Scalar(255,0,0), 3);
					Imgproc.putText(frame, "Contour # " + i + ": " + rect.x + "," + rect.y + "," + rect.area(), new Point(50, 50 + 12 * i), Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,255,255));
					if (rect.area() > 10000) rects.add(rect);
					if (grip.filterContoursOutput().size() == 2 && rects.size() == 2 && i == 1) {
						nrects = sortRect(rects);
						distance = pixToInch(nrects[0].height);
						Imgproc.line(frame, new Point(nrects[0].x + nrects[0].width, nrects[0].y), new Point(nrects[1].x, nrects[1].y), new Scalar(255,0,0));
						Imgproc.putText(frame, "Distance to Center: " + distance  + " inches", new Point(400,50), Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,255,255));
						Imgproc.putText(frame, "Heights: " + nrects[0].height  + "px, " + nrects[1].height + "px", new Point(400,65), Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,255,255));
						Imgproc.putText(frame, "Ratio (right over left): " + (nrects[1].height / (double)nrects[0].height), new Point(400,80), Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,255,255));
						Imgproc.putText(frame, "", new Point(600,50), Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,255,255));
						rects.clear();
					}
				}
			} else {
				Imgproc.putText(frame, "Two contours not found.", new Point(50,50), Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,255,255));
			}
			Imgproc.putText(frame, " " + calculateFPS(lastTime), new Point(50,20), Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,255,255));
			displayImage(toBufferedImage(frame));
		}
		camera.release();
	}
	
	private static double pixToInch(double inPix) {
		return (((368 / 1652145.0) * Math.pow(inPix, 2)) - ((128076 / 550715.0) * inPix) + (2171632 / 30039.0));
	}
	
	private static void sleep(int i) {
		try {
		    Thread.sleep(i);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
	private static long calculateFPS(long lastTime) {
		long now = System.currentTimeMillis();
		long difference = now - lastTime;
		return (difference);
	}
	
	//precondition: is size of 2
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
	public static void initDisplay() {
		image = new ImageIcon();
		window = new JFrame("Give Me a Vision, I'll Give You a Pineapple");
		window.setLayout(new FlowLayout());
		window.setSize(1280,960);
		label = new JLabel();
		label.setIcon(image);
		window.add(label);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void displayImage(Image img) {
		image = new ImageIcon(img);
	    label.setIcon(image);
	}
	
	 public static Image toBufferedImage(Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
		    type = BufferedImage.TYPE_3BYTE_BGR;
		}
	    int bufferSize = m.channels()*m.cols()*m.rows();
	    byte [] b = new byte[bufferSize];
	    m.get(0,0,b); // get all the pixels
	    BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
	    final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	    System.arraycopy(b, 0, targetPixels, 0, b.length);  
	    return image;
	}
}
