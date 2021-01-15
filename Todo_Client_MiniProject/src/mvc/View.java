package mvc;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View {
	
	private Model model;
	private Stage stage;
	
	protected Label lblWebAddress = new Label("Server address:");
	protected TextField txtWebAddress = new TextField("localhost");
	protected Label lblPort = new Label("Port:");
	protected TextField txtPort = new TextField("50002");
	
	protected Label lblCommand = new Label("Command:");
	protected TextField txtCommand = new TextField();
	protected Button btnSubmit = new Button("Submit");
	
	protected TextField txtResponse = new TextField("Response:");
	
	public View(Stage stage, Model model) {
		this.stage = stage;
		this.model = model;
		
		stage.setTitle("ToDo Client MiniProject");
		
		VBox root = new VBox();
		HBox rowServerAddress = new HBox();
		Region spacer1 = new Region();
		spacer1.setPrefWidth(30);
		rowServerAddress.getChildren().addAll(lblWebAddress, txtWebAddress, spacer1, lblPort, txtPort);
		
		HBox rowCommand = new HBox();
		txtCommand.setPrefWidth(500);
		rowCommand.getChildren().addAll(txtCommand);
		rowCommand.getChildren().addAll(btnSubmit);
		
		root.getChildren().addAll(rowServerAddress, lblCommand, rowCommand, txtResponse);
		
		Scene scene = new Scene(root, 800, 600);
		scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
	
	public void start() {
		stage.show();
	}
	
	public Stage getStage() {
		return stage;
	}
}
