package main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class VisionServer {
	private static final int PORT = 1185;
	private static DataOutputStream out;
	private static ServerSocket server;
	private static Socket socket;
	private static VideoCapture camera;
	private static Mat frame;
	public static void main(String[] args) {
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			System.out.println("Hello, OpenCV");
			frame = new Mat();
			camera = new VideoCapture(0);
			sleep(1000);
			camera.read(frame);
			System.out.println("Took image.");
			sleep(1000);
			System.out.println("Listening...");
			server = new ServerSocket(PORT);
			socket = server.accept();
			out = new DataOutputStream(socket.getOutputStream());
			System.out.println("Connected.");
			//out.writeLong(frame.total() * frame.channels());
			//System.out.println("Wrote: " + frame.total() * frame.channels());
			while (socket.isConnected()) {
				byte[] b = new byte[(int)frame.total() * frame.channels()];
				frame.get(0, 0, b);
				out.write(b, 0, b.length);
				out.flush();
			}
			out.close();
			socket.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	private static void sleep(int i) {
		try {
		    Thread.sleep(i);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
}
