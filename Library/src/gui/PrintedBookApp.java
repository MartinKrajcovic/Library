package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PrintedBookApp extends Application{

	 public void start(Stage stage) throws Exception {
	        Parent root = FXMLLoader.load(getClass().getResource("book.fxml"));
	        
	        Scene scene = new Scene(root);
	        
	        stage.setScene(scene);
	        stage.setTitle("Library - New book");
	        stage.show();
	    }

//	    /**
//	     * @param args the command line arguments
//	     */
//	    public static void main(String[] args) {
//	        launch(args);
//	    }
}
