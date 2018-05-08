package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class GearClient {
	private static Socket socket;
	private static int portNumber = 1185;
	private static String hostName = "0.0.0.0";
	private static double distance;
	private static void main(String[] args) throws IOException {
		socket = new Socket(hostName, portNumber);
		DataInputStream dIn;
		while(true) {
			dIn = new DataInputStream(socket.getInputStream());
			distance = dIn.readDouble();
			System.out.println(distance);
		}
	}
}
