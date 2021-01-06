package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

import database.IToDoDatabase;
import login.ILoginHandler;
import model.Priority;
import model.ToDo;
import model.Token;

/**
 * Helper class that extends Thread and handles communication with one client.
 */
public class ClientThread extends Thread {
	private static Logger logger = Logger.getLogger(ClientThread.class.getName());
	
	private static int clientNumberStatic = 0; // Number of next connected client
	private Socket socket; // Socket used for communication with the client
	private int clientNumber; // Assigned number of the client
	private IToDoDatabase toDoDatabase; // Database of ToDos
	private ILoginHandler loginHandler; // Handler of logins and users

	public ClientThread(Socket socket, IToDoDatabase toDoDatabase, ILoginHandler loginHandler) {
		super("Client thread " + clientNumberStatic);
		clientNumber = clientNumberStatic++;
		this.socket = socket;
		this.toDoDatabase = toDoDatabase;
		this.loginHandler = loginHandler;
	}

	/**
	 * Main method of the client, that opens input and output streams
	 * and handles the request from the client.
	 */
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
	
	/**
	 * Handles request from the client. Invokes appropriate command
	 * for a valid request coming or logs that the command is not valid.
	 * @param request Plain-text unprocessed request from the client
	 */
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

	/**
	 * Gets text that includes client number. Used in multiple parts
	 * of this class, therefore it's extracted into this function.
	 * @return Formatted text with client number.
	 */
	private String getClientNumberText() {
		return "client number #" + clientNumber;
	}
	
	/**
	 * Handles Ping command from the client.
	 * @param parts Parsed request from client (array of individual strings)
	 */
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
	
	/**
	 * Handles CreateLogin command from the client.
	 * @param parts Parsed request from client (array of individual strings)
	 */
	private String commandCreateLogin(String[] parts) {
		if(parts.length < 3) {
			logger.severe("The command was not in a correct format! It should be CreateLogin|email|password!");
			return "Result|false";
		}
		
		String email = parts[1];
		String password = parts[2];
		if(!toDoDatabase.createLogin(email, password)) {
			return "Result|false";
		}
		
		return "Result|true";
	}
	
	/**
	 * Handles Login command from the client.
	 * @param parts Parsed request from client (array of individual strings)
	 */
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
	
	/**
	 * Handles CreateToDo command from the client.
	 * @param parts Parsed request from client (array of individual strings)
	 */
	private String commandCreateToDo(String[] parts) {
		if(parts.length < 6) {
			System.out.println("The command was not in a correct format! It should be CreateToDo|Token|Title|Priority|Description|DueDate");
			return "Result|false";
		}
		
		String token = parts[1];
		String title = parts[2];
		String priority = parts[3];
		String description = parts[4];
		String dueDate = parts[5];
		Priority parsedPriority = null;
		LocalDate parsedDueDate = null;
		try
		{
			parsedPriority = Priority.valueOf(priority);
		}
		catch(IllegalArgumentException ex)
		{
			System.out.println("Priority was not in correct format! Please select one of [low, medium, high]!");
			return "Result|false";
		}
		
		try
		{
			parsedDueDate = LocalDate.parse(dueDate);
		}
		catch(DateTimeParseException ex)
		{
			System.out.println("Due date was not in correct format! Please enter due date in YYYY-MM-DD format!");
			return "Result|false";
		}
		
		int index = toDoDatabase.createToDo(token, title, parsedPriority, description, parsedDueDate);
		if(index == -1) {
			System.out.println("Validation of given token failed! Please provide valid, non-expired token!");
			return "Result|false";
		}

		return "Result|true|" + index;
	}
	
	/**
	 * Handles ListToDos command from the client.
	 * @param parts Parsed request from client (array of individual strings)
	 */
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
	
	/**
	 * Handles GetToDo command from the client.
	 * @param parts Parsed request from client (array of individual strings)
	 */
	private String commandGetToDo(String[] parts) {
		if(parts.length < 3) {
			System.out.println("The command was not in a correct format! It should be GetToDo|Token|Index");
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
				
		return "Result|true|" + index + "|" + toDo.getTitle() + "|" + toDo.getPriority() + "|" + toDo.getDescription() + "|" + toDo.getDueDate().toString();
	}
	
	/**
	 * Handles Logout command from the client.
	 * @param parts Parsed request from client (array of individual strings)
	 */
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