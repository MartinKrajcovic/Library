package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public final class Alerts {
	
	private static Alert alert;
	
	public static void warningAlert(String message) {
		alert = new Alert(AlertType.WARNING, message);
		alert.setHeaderText("");
		alert.showAndWait();
	}
	
	public static void infoAlert(String message) {
		alert = new Alert(AlertType.INFORMATION, message);
		alert.setHeaderText("");
		alert.showAndWait();
	}
	
	public static void errorAlert(String message) {
		alert = new Alert(AlertType.ERROR, message);
		alert.setHeaderText("");
		alert.showAndWait();
	}
	
	public static ButtonType confirmAlert(String message) {
		alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.setHeaderText("");
		alert.showAndWait();
		return alert.getResult();
	}	
	
	public static ButtonType customConfirmAlert(String message, ButtonType... buttons) {
		alert = new Alert(AlertType.CONFIRMATION, message, buttons);
		alert.setHeaderText("");
		
		Button yesButton= (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
		yesButton.setDefaultButton(false);
		
		Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
	    noButton.setDefaultButton(true);
		
	    alert.showAndWait();
		return alert.getResult();
	}
}
