package database;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import login.ILoginHandler;
import model.Priority;
import model.ToDo;
import model.User;
import utility.PasswordValidator;

public class InMemoryToDoDatabase implements IToDoDatabase {
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
				System.out.println("User with email " + email + " is already registered!");
				return false;
			}
		}
		
		System.out.println("User with email " + email + " has been registered!");
		users.add(new User(email, password));
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
	public int createToDo(String token, String title, Priority priority, String description) {
		String email = loginHandler.validateToken(token);
		if(email == null) {
			return -1;
		}
		
		userToDoLists.putIfAbsent(email, new ArrayList<ToDo>());
		
		List<ToDo> toDoList = userToDoLists.get(email);
		int index = toDoList.size();
		toDoList.add(new ToDo(title, priority, description, LocalDate.now()));
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
	public void setLoginHandler(ILoginHandler loginHandler) {
		this.loginHandler = loginHandler;
	}
}
