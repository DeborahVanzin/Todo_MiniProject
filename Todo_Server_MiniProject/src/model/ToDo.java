package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents ToDo entry.
 */
public class ToDo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String title;
	private Priority priority;
	private String description;
	private LocalDate dueDate;
	
	public ToDo(String title, Priority priority, String description, LocalDate dueDate) {
		this.title = title;
		this.priority = priority;
		this.description = description;
		this.dueDate = dueDate;
	}

	public String getTitle() {
		return title;
	}

	public Priority getPriority() {
		return priority;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}
}
