package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import books.Binding;
import books.Language;
import books.PrintedBook;
import books.PrintedFormat;
import books.ReadStatus;
import connections.ImageDownloader;
import exceptions.RefusedConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

public class PrintedBookController implements Initializable {

	@FXML private TextField authorField;
	@FXML private TextField titleField;
	@FXML private TextField genreField;
	@FXML private TextField publisherField;
	@FXML private TextArea plotArea;
	@FXML private ChoiceBox<Language> languageBox;
	@FXML private ChoiceBox<Binding> bindingBox;
	@FXML private ChoiceBox<PrintedFormat> formatBox;
	@FXML private TextField pagesField;
	@FXML private TextField chaptersField;
	@FXML private TextField publishedField;
	@FXML private TextField orderInSeriesField;
	@FXML private TextField editionField;
	@FXML private TextField priceField;
	@FXML private TextField ISBNField;
	@FXML private TextField mainHeroField;
	@FXML private TextField imageLocationField;
	@FXML private ChoiceBox<ReadStatus> readStatusBox;
	@FXML private TextField weightField;
	@FXML private ImageView bookImage;
	private final Tooltip tooltipDragDrop = new Tooltip("You can use drag and drop here");
	private PrintedBook myBook;
	
	ObservableList<Language> languageList = FXCollections.observableArrayList(Language.values());
	ObservableList<Binding> bindingList = FXCollections.observableArrayList(Binding.values());
	ObservableList<PrintedFormat> formatList = FXCollections.observableArrayList(PrintedFormat.values());
	ObservableList<ReadStatus> statusList = FXCollections.observableArrayList(ReadStatus.values());
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		languageBox.setValue(Language.Undefined);
		languageBox.setItems(languageList);
		bindingBox.setValue(Binding.Undefined);
		bindingBox.setItems(bindingList);
		formatBox.setValue(PrintedFormat.Undefined);
		formatBox.setItems(formatList);
		readStatusBox.setValue(ReadStatus.Unread);
		readStatusBox.setItems(statusList);
		imageLocationField.setTooltip(tooltipDragDrop);
	}

	@FXML
	private void allowPlotDrop(DragEvent event) {
		if (event.getDragboard().hasFiles())
			event.acceptTransferModes(TransferMode.ANY);
	}
	
	@FXML
	private void processDroppedPlot(DragEvent event) {
		File file = event.getDragboard().getFiles().get(0);
		if (!file.getAbsolutePath().endsWith(".txt")) {
			return;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = "";
			String text = "";
			while ((line = br.readLine()) != null) {
				text += (line + "\n");
			}
			plotArea.setText(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void allowImageLocationDrop(DragEvent event) {
		if (event.getDragboard().hasFiles())
			event.acceptTransferModes(TransferMode.ANY);
	}
	
	@FXML
	private void processDroppedImageLocation(DragEvent event) {
		String path = event.getDragboard().getFiles().get(0).getAbsolutePath();
		if (ImageDownloader.checkImageSuffix(path.trim()))
			imageLocationField.setText(path);
	}
	
	@FXML
	private void createAndShowBook(ActionEvent event) {
		if (!checkImportantFields()) {
			Alerts.warningAlert("Author, title and genre must be specified!");
			return;
		}
		myBook = new PrintedBook(authorField.getText(), 
				titleField.getText(), genreField.getText(), publisherField.getText(), 
				languageBox.getValue(), bindingBox.getValue(), formatBox.getValue());
		
		myBook.setPages(parseInt(pagesField.getText().trim()));
		myBook.setChapters(parseInt(chaptersField.getText().trim()));
		myBook.setPublished(parseInt(publishedField.getText().trim()));
		myBook.setOrderInSeries(parseInt(orderInSeriesField.getText().trim()));
		myBook.setEdition(parseInt(editionField.getText().trim()));
		myBook.setISBN(ISBNField.getText());
		myBook.setMainHero(mainHeroField.getText());
		myBook.setPrice(parseDouble(priceField.getText().trim()));
		myBook.setWeight(parseInt(weightField.getText().trim()));
		myBook.setReadStatus(readStatusBox.getValue());
		myBook.loadPlot(plotArea.getText());	
							 
		plotArea.setText(myBook.toString());
		
		// kvoli tomu bordelu dole treba prekopat triedu ImageDownloader na vlakno
		String location = imageLocationField.getText().trim(); 					 
		Thread t = new Thread(() -> {	// tuto zvazit nejaky wrapper lambda     
			try {															     
				ImageDownloader downloader = new ImageDownloader(location);
				myBook.loadImage(downloader.download());
			} catch (MalformedURLException urlE) {								 
				myBook.loadImage(new File(location));							 
			} catch (RefusedConnectionException refused) {
				refused.getMessage();
			}
			bookImage.setImage(SwingFXUtils.toFXImage(myBook.getImage(), null)); 
		});																		 
		t.start();
		Alerts.infoAlert("A new book has been created!\nNow you can add it to your library..");
	}																			 
	
	@FXML
	private void resetFields(ActionEvent event) {
		authorField.setText("");
		titleField.setText("");
		genreField.setText("");
		publisherField.setText("");
		languageBox.setValue(Language.Undefined);
		bindingBox.setValue(Binding.Undefined);
		formatBox.setValue(PrintedFormat.Undefined);
		pagesField.setText("");
		chaptersField.setText("");
		publishedField.setText("");
		orderInSeriesField.setText("");
		editionField.setText("");
		ISBNField.setText("");
		priceField.setText("");
		mainHeroField.setText("");
		imageLocationField.setText("");
		readStatusBox.setValue(ReadStatus.Unread);
		weightField.setText("");
		plotArea.setText("");
		bookImage.setImage(null);
	}
	
	@FXML
	private void addToLibrary(ActionEvent event) {
		if (myBook == null) {
			Alerts.warningAlert("You must create a book to use this option.");
			return;
		}
		myBook = null;
		Alerts.infoAlert("This option is not available yet");
	}
	
	private boolean checkImportantFields() {
		if (authorField.getText().trim().isEmpty() 
				|| titleField.getText().trim().isEmpty() 
				|| genreField.getText().trim().isEmpty())
			return false;
		return true;
	}
	
	private int parseInt(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	private double parseDouble(String text) {
		if (text.contains(","))
			text = text.replace(',', '.');
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}
	
}