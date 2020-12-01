package client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import database.IToDoDatabase;

public class ClientThread extends Thread {
	private static int clientNumberStatic = 0;
	private Socket socket;
	private int clientNumber;
	private IToDoDatabase toDoDatabase;

	public ClientThread(Socket socket, IToDoDatabase toDoDatabase) {
		super("Client thread " + clientNumberStatic);
		clientNumber = clientNumberStatic++;
		this.socket = socket;
		this.toDoDatabase = toDoDatabase;
	}

	@Override
	public void run() {
		System.out.println("A " + getClientNumberText() +" has connected!");
		
		try (BufferedReader inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			OutputStream outBinary = socket.getOutputStream();
	        PrintWriter outText = new PrintWriter(outBinary);
			String request = inReader.readLine();
			System.out.println("Request '" + request + "' received from " + getClientNumberText());
			String answer = handleRequest(request);
			
			outText.println(answer);
			outText.flush();
			socket.close();
			
		} catch (IOException e) {

		}
		System.out.println("Handling for client #" + clientNumber + " has ended!");
	}
	
	private String handleRequest(String request) {
		String[] parts = request.split("\\|");
		if(parts.length == 0) {
			System.out.println("Invalid format of message! It doesn't contain any command!");
			return "Result|false";
		}
		
		String command = parts[0];
		System.out.println("Command for " + getClientNumberText() + " is " + command);

		String result = "";
		switch(command)
		{
			case "Ping":
				result = commandPing(parts);
				break;
			case "CreateLogin":
				result = commandCreateLogin(parts);
				break;
			case "Login":
				result = commandLogin(parts);
				break;
			default:
				System.out.println("Unknown command!");
				return "Result|false";
		}
		
		System.out.println("Request from " + getClientNumberText() + " handled successfully!");
		return result;
	}
	
	private String getClientNumberText() {
		return "client number #" + clientNumber;
	}
	
	private String commandPing(String[] parts) {
		return "Result|true";
	}
	
	private String commandCreateLogin(String[] parts) {
		if(parts.length < 3) {
			System.out.println("The command was not in a correct format! It should be CreateLogin|email|password!");
			return "Result|false";
		}
		
		String email = parts[1];
		String password = parts[2];
		if(!toDoDatabase.createLogin(email, password)) {
			return "Result|false";
		}
		
		return "Result|true";
	}
	
	private String commandLogin(String[] parts) {
		return null;
	}
}
