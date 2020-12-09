package database;

import java.time.LocalDate;
import java.util.List;

import login.ILoginHandler;
import model.Priority;
import model.ToDo;
import model.User;

public interface IToDoDatabase {
	boolean createLogin(String email, String password);
	User getUserByEmail(String email);
	
	int createToDo(String token, String title, Priority priority, String description, LocalDate dueDate);
	List<Integer> listToDos(String token);
	ToDo getToDo(String token, int index);
	
	void setLoginHandler(ILoginHandler loginHandler);
}
