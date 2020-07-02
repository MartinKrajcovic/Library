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
import connections.ImageDownloader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import library.PrintedLibrary;

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
	@FXML private ImageView bookImage;
	private final Tooltip tooltipDragDrop = new Tooltip("You can use drag and drop here");
	private PrintedLibrary p;
	private PrintedBook myBook;
	
	ObservableList<Language> languageList = FXCollections.observableArrayList(Language.values());
	ObservableList<Binding> bindingList = FXCollections.observableArrayList(Binding.values());
	ObservableList<PrintedFormat> formatList = FXCollections.observableArrayList(PrintedFormat.values());
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		languageBox.setValue(Language.Undefined);
		languageBox.setItems(languageList);
		bindingBox.setValue(Binding.Undefined);
		bindingBox.setItems(bindingList);
		formatBox.setValue(PrintedFormat.Undefined);
		formatBox.setItems(formatList);
		
		imageLocationField.setTooltip(tooltipDragDrop);
		p = PrintedLibrary.getInstance();
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
	
	// Stlacenie tlacidla
	@FXML
	private void createAndShowBook(ActionEvent event) {
		if (authorField.getText().trim().isEmpty() 
				|| titleField.getText().trim().isEmpty() 
				|| genreField.getText().trim().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING, "Author, Title and Genre must be specified !", ButtonType.OK);
			alert.showAndWait();
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
		myBook.loadPlot(plotArea.getText());	
																		 																		 
		// zobrazenie textovej reprezentacie objektu							 
		plotArea.setText(myBook.toString());							
		
		String location = imageLocationField.getText().trim(); 					 
		Thread t = new Thread(() -> {	// tuto zvazit nejaky wrapper lambda     
			try {															     
				ImageDownloader downloader = new ImageDownloader(location);
				myBook.loadImage(downloader.download());
				// ked je neplatna url adresa, tak sa loaduje zo suboru
			} catch (MalformedURLException urlE) {								 
				myBook.loadImage(new File(location));							 
			} 
			bookImage.setImage(SwingFXUtils.toFXImage(myBook.getImage(), null)); 
		});																		 
		t.start();
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
		plotArea.setText("");
		bookImage.setImage(null);
		p.saveLibrary();
	}
	
	@FXML
	private void addToLibrary(ActionEvent event) {
		p.addBook(myBook);
		Alert alert = new Alert(AlertType.INFORMATION, "Book added to Library!\nContains " + p.countBooks() + " books", ButtonType.OK);
		alert.showAndWait();
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