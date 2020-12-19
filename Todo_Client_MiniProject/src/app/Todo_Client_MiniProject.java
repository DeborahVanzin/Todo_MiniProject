package app;

import javafx.application.Application;
import javafx.stage.Stage;
import mvc.Controller;
import mvc.Model;
import mvc.View;

public class Todo_Client_MiniProject extends Application {
	
	private View view;
	private Controller controller;
	private Model model;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		model = new Model();
		view = new View(primaryStage, model);
		controller = new Controller(model, view);
	}
	
	public static void main (String[] args) {
		launch(args);
	}
}
