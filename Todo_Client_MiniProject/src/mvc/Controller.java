package mvc;

import network.ServerCommunicator;

public class Controller {

	final private Model model;
	final private View view;

	private boolean portValid = true;

	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		
		// ChangeListener for the text-property of the port
		view.txtPort.textProperty().addListener(
			// Parameters of any PropertyChangeListener
			(observable, oldValue, newValue) -> {
				validatePort(newValue);
			});
		
		enableDisableSubmitButton();
		
		view.btnSubmit.setOnAction(e -> onBtnSubmitClicked());
	}
	
	private void onBtnSubmitClicked() {
		ServerCommunicator comm = new ServerCommunicator(view.txtWebAddress.getText(), Integer.parseInt(view.txtPort.getText()));
		String response = comm.sendCommand(view.txtCommand.getText());
		view.txtResponse.setText(response);
	}

	protected void validatePort(String newPort) {
		boolean valid = isPortNumberValid(newPort);

		view.txtPort.getStyleClass().remove("valid");
		view.txtPort.getStyleClass().remove("invalid");
		if (valid) {
			view.txtPort.getStyleClass().add("valid");
		} else {
			view.txtPort.getStyleClass().add("invalid");
		}
		
		// Speichern das Resultat
		portValid = valid;

		enableDisableSubmitButton();
	}

	// Beides muss gültig seint webAddressVAlid und portValid
	protected void enableDisableSubmitButton() {
		view.btnSubmit.setDisable(!portValid);
	}
	
	protected boolean isPortNumberValid(String portNumber) {
		try {
			int value = Integer.parseInt(portNumber);
			if (value < 1 || value > 65535)
				return false;
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
}