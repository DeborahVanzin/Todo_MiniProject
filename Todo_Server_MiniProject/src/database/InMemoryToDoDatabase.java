package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import login.ILoginHandler;
import model.Priority;
import model.ToDo;
import model.User;
import utility.PasswordValidator;

public class InMemoryToDoDatabase implements IToDoDatabase {
	private static Logger logger = Logger.getLogger(InMemoryToDoDatabase.class.getName());
	private static final String DATABASE_FILENAME = "todo_database.dat";

	private List<User> users = new ArrayList<>();
	private Map<String, List<ToDo>> userToDoLists = new HashMap<>();
	private ILoginHandler loginHandler;

	@Override
	public boolean createLogin(String email, String password) {

		if(!PasswordValidator.validate(password)) {
			return false;
		}
		
		// Wenn jemand sich mit gleichem Email registriert, sollen wir das nicht erlauben
		for (int i =0; i < users.size(); i++) {
			if(users.get(i).getEmail().equals(email)) {
				logger.severe("User with email " + email + " is already registered!");
				return false;
			}
		}
		
		logger.info("User with email " + email + " has been registered!");
		users.add(new User(email, password));
		saveToFile();
		return true;
	}

	@Override
	public User getUserByEmail(String email) {
		for(User user : users) {
			if(user.getEmail().equals(email)) {
				return user;
			}
		}
		
		return null;
	}

	@Override
	public int createToDo(String token, String title, Priority priority, String description, LocalDate dueDate) {
		String email = loginHandler.validateToken(token);
		if(email == null) {
			return -1;
		}
		
		userToDoLists.putIfAbsent(email, new ArrayList<ToDo>());
		
		List<ToDo> toDoList = userToDoLists.get(email);
		int index = toDoList.size();
		toDoList.add(new ToDo(title, priority, description, dueDate));
		saveToFile();
		return index;
	}
	
	@Override
	public List<Integer> listToDos(String token) {
		String email = loginHandler.validateToken(token);
		if(email == null) {
			return null;
		}
		
		userToDoLists.putIfAbsent(email, new ArrayList<ToDo>());
		
		List<ToDo> toDoList = userToDoLists.get(email);
		List<Integer> result = new ArrayList<>();
		for(int i = 0; i < toDoList.size(); i++) {
			result.add(i);
		}

		return result;
	}
	
	@Override
	public ToDo getToDo(String token, int index) {
		String email = loginHandler.validateToken(token);
		if(email == null) {
			return null;
		}
		
		userToDoLists.putIfAbsent(email, new ArrayList<ToDo>());
		
		List<ToDo> toDoList = userToDoLists.get(email);
		if(index < 0 || index >= toDoList.size()) {
			return null;
		}

		return toDoList.get(index);
	}

	@Override
	public void setLoginHandler(ILoginHandler loginHandler) {
		this.loginHandler = loginHandler;
	}

	@Override
	public void loadFromFile() {		
		try {
			FileInputStream fileStream = new FileInputStream(DATABASE_FILENAME);
			ObjectInputStream objStream = new ObjectInputStream(fileStream);
			users = (List<User>) objStream.readObject();
			userToDoLists = (Map<String, List<ToDo>>) objStream.readObject();
			objStream.close();
			fileStream.close();
			logger.info("Database loaded successfully! Loaded " + users.size() + " user(s).");
		} catch (FileNotFoundException e) {
			logger.warning("Could not read from database because file does not exist!");
		} catch (IOException e) {
			logger.severe("Could not read from database! " + e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.severe("Could not read from database! " + e.getMessage());
		}
	}
			
	private void saveToFile() {
		try {
			FileOutputStream fileStream = new FileOutputStream(DATABASE_FILENAME);
			ObjectOutputStream objStream = new ObjectOutputStream(fileStream);
			objStream.writeObject(users);
			objStream.writeObject(userToDoLists);
			objStream.close();
			fileStream.close();
			logger.info("Database saved to file successfully!");
		} catch (FileNotFoundException e) {
			logger.severe("Could not write database! " + e.getMessage());
		} catch (IOException e) {
			logger.severe("Could not write database!" + e.getMessage());
		}
	}
}
