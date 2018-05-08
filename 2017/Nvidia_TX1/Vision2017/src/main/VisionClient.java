package main;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class VisionClient {
	private static final int PORT = 1185;
	private static final String HOST = "localhost";
	private static Socket socket;
	private static JFrame window;
	private static JLabel label;
	private static ImageIcon icon;
	public static void main(String[] args) {
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			window = new JFrame("Client");
			icon = new ImageIcon();
			label = new JLabel(icon);
			window.add(label);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setVisible(true);
			socket = new Socket(HOST, PORT);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			System.out.println("Connected.");
			//long size = in.readLong();
			//System.out.println("Received: " + b.length);
			sleep(5000);
			while (socket.isConnected()) {
				byte[] b = new byte[640 * 480 * 3];
				in.readFully(b, 0, b.length);
				Mat mat = new Mat(640, 480, CvType.CV_8UC3);
				mat.put(0, 0, b);
				icon = new ImageIcon(toBufferedImage(mat));
				label.setIcon(icon);
			}
			in.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	private static void sleep(int i) {
		try {
		    Thread.sleep(i);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
}
