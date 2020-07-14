package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import books.PrintedBook;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.PrintedLibrary;

public class PrintedLibraryController implements Initializable {

    public static PrintedLibrary printedLibrary;
    private ObservableList<PrintedBook> tableList;
    @FXML private AnchorPane anchorPane;
	@FXML private TableView<PrintedBook> table;
    @FXML private TableColumn<PrintedBook, String> table_author;
    @FXML private TableColumn<PrintedBook, String> table_title;
    @FXML private TableColumn<PrintedBook, String> table_genre;
    @FXML private TableColumn<PrintedBook, String> table_publisher;
    @FXML private TableColumn<PrintedBook, String> table_language;
    @FXML private TableColumn<PrintedBook, String> table_binding;
    @FXML private TableColumn<PrintedBook, String> table_dimensions;
    @FXML private TableColumn<PrintedBook, Number> table_orderInSeries;
    @FXML private TableColumn<PrintedBook, Number> table_pages;
    @FXML private TableColumn<PrintedBook, Number> table_published;
    @FXML private TableColumn<PrintedBook, Number> table_weight;
    @FXML private TableColumn<PrintedBook, String> table_mainHero;
    @FXML private TableColumn<PrintedBook, String> table_read;
    
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		printedLibrary = PrintedLibrary.getInstance();
		update();
		
		table_author.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getAuthor()));
		table_title.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getTitle()));
		table_genre.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getGenre()));
		table_publisher.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getPublisher()));
		table_language.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getLanguage().toString()));
		table_binding.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBinding().toString()));
		table_dimensions.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getFormat().toString()));
		table_orderInSeries.setCellValueFactory(param -> new ReadOnlyIntegerWrapper(param.getValue().getOrderInSeries()));
		table_pages.setCellValueFactory(param -> new ReadOnlyIntegerWrapper(param.getValue().getPages()));
		table_published.setCellValueFactory(param -> new ReadOnlyIntegerWrapper(param.getValue().getPublished()));
		table_weight.setCellValueFactory(param -> new ReadOnlyIntegerWrapper(param.getValue().getWeight()));
		table_mainHero.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getMainHero()));
		table_read.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getReadStatus().toString()));
	}
	
	@FXML
	private void openBookCard(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("book.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Library - New book");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner((Stage) anchorPane.getScene().getWindow());
        stage.showAndWait();
        update();
	}
	
	@FXML
	private void deleteBook(ActionEvent event) {
		PrintedBook pb = table.getSelectionModel().getSelectedItem();
		printedLibrary.dropBook(pb);
		update();
	}

	@FXML
	private void saveLibrary(ActionEvent event) {
		try {
			printedLibrary.saveLibrary();
			Alerts.infoAlert("The library has been saved");
		} catch (IOException e) {
			Alerts.errorAlert("The library could not be saved");
		}
	}

	@FXML
	private void deleteLibrary(ActionEvent event) {
		if (Alerts.customConfirmAlert("You are about to delete the whole content of the library.\n"
				+ "Are you sure you want to proceed?", ButtonType.NO, ButtonType.YES).equals(ButtonType.YES)) {
			printedLibrary.dropLibrary();
			update();
		}
	}
	
	@FXML
	private void refreshTable(ActionEvent event) {
		update();
	}
	
	private void update() {
		tableList = FXCollections.observableArrayList(printedLibrary.getLibrary());
        table.setItems(tableList);
	}	
}
