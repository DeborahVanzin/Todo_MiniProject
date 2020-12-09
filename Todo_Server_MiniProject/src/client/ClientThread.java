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
import login.ILoginHandler;
import model.Priority;
import model.ToDo;
import model.Token;

public class ClientThread extends Thread {
	private static int clientNumberStatic = 0;
	private Socket socket;
	private int clientNumber;
	private IToDoDatabase toDoDatabase;
	private ILoginHandler loginHandler;

	public ClientThread(Socket socket, IToDoDatabase toDoDatabase, ILoginHandler loginHandler) {
		super("Client thread " + clientNumberStatic);
		clientNumber = clientNumberStatic++;
		this.socket = socket;
		this.toDoDatabase = toDoDatabase;
		this.loginHandler = loginHandler;
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
			case "CreateToDo":
				result = commandCreateToDo(parts);
				break;
			case "ListToDos":
				result = commandListToDos(parts);
				break;
			case "GetToDo":
				result = commandGetToDo(parts);
				break;
			case "Logout":
				result = commandLogout(parts);
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
		if(parts.length >= 2) {
			// Now the token is present and must be validated
			String token = parts[1];
			if(loginHandler.validateToken(token) == null) {
				return "Result|false";
			}
		}
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
		if(parts.length < 3) {
			System.out.println("The command was not in a correct format! It should be Login|email|password!");
			return "Result|false";
		}
		
		String email = parts[1];
		String password = parts[2];
		Token token = loginHandler.login(email, password);
		if(token == null) {
			return "Result|false";
		}
		
		return "Result|true|" + token;
	}
	
	private String commandCreateToDo(String[] parts) {
		if(parts.length < 5) {
			System.out.println("The command was not in a correct format! It should be CreateToDo|Token|Title|Priority|Description");
			return "Result|false";
		}
		
		String token = parts[1];
		String title = parts[2];
		String priority = parts[3];
		String description = parts[4];
		Priority parsedPriority = null;
		try
		{
			parsedPriority = Priority.valueOf(priority);
		}
		catch(IllegalArgumentException ex)
		{
			System.out.println("Priority was not in correct format! Please select one of [low, medium, high]!");
			return "Result|false";
		}
		
		int index = toDoDatabase.createToDo(token, title, parsedPriority, description);
		if(index == -1) {
			System.out.println("Validation of given token failed! Please provide valid, non-expired token!");
			return "Result|false";
		}

		return "Result|true|" + index;
	}
	
	private String commandListToDos(String[] parts) {
		if(parts.length < 2) {
			System.out.println("The command was not in a correct format! It should be ListToDos|Token");
			return "Result|false";
		}
		
		String token = parts[1];
		List<Integer> todos = toDoDatabase.listToDos(token);
		if(todos == null) {
			System.out.println("Validation of given token failed! Please provide valid, non-expired token!");
			return "Result|false";
		}
		
		String result = "Result|true";
		for(int toDoIndex : todos) {
			result += "|" + toDoIndex;
		}
		
		return result;
	}
	
	private String commandGetToDo(String[] parts) {
		if(parts.length < 3) {
			System.out.println("The command was not in a correct format! It should be ListToDos|Token");
			return "Result|false";
		}
		
		String token = parts[1];
		String index = parts[2];
		int parsedIndex = 0;
		try
		{
			parsedIndex = Integer.parseInt(index);
		}
		catch(NumberFormatException ex)
		{
			System.out.println("Index was not in correct format! Please provide a valid index!");
			return "Result|false";
		}
		
		ToDo toDo = toDoDatabase.getToDo(token, parsedIndex);
		if(toDo == null) {
			System.out.println("Such ToDo doesn't exist! Please provide a valid index of ToDo entry!");
			return "Result|false";
		}
				
		return "Result|true|" + index + "|" + toDo.getTitle() + "|" + toDo.getPriority() + "|" + toDo.getDescription();
	}
	
	private String commandLogout(String[] parts) {
		if(parts.length < 2) {
			System.out.println("The command was not in a correct format! It should be Logout|Token");
			return "Result|false";
		}
		
		String token = parts[1];
		if(!loginHandler.logout(token)) {
			return "Result|false";
		}
				
		return "Result|true";
	}
}