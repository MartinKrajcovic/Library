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
	@FXML private TextField priceField;
	@FXML private TextField mainHeroField;
	@FXML private TextField imageLocationField;
	@FXML private ImageView bookImage;
	private final Tooltip tooltipEmpty = new Tooltip("This field cannot be empty");
	
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
		
		authorField.setTooltip(tooltipEmpty);
		titleField.setTooltip(tooltipEmpty);
		genreField.setTooltip(tooltipEmpty);
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
		if (checkImageSuffix(path))
			imageLocationField.setText(path);
	}
	
	@FXML
	private void createAndShowBook(ActionEvent event) {
		if (authorField.getText().isEmpty() 
				|| titleField.getText().isEmpty() 
				|| genreField.getText().isEmpty()) {
			System.err.println("author, title and genre must be chosen!");
			return;
		}
		PrintedBook myBook = new PrintedBook(authorField.getText(), 
				titleField.getText(), genreField.getText(), publisherField.getText(), 
				languageBox.getValue(), bindingBox.getValue(), formatBox.getValue());
		
		myBook.setPages(parseInt(pagesField.getText().trim()));
		myBook.setChapters(parseInt(chaptersField.getText().trim()));
		myBook.setPublished(parseInt(publishedField.getText().trim()));
		myBook.setPrice(parseDouble(priceField.getText().trim()));
		myBook.setMainHero(mainHeroField.getText());
		myBook.loadPlot(plotArea.getText());	
		
		String location = imageLocationField.getText().trim(); 					 
		Thread t = new Thread(() -> {	// tuto zvazit nejaky wrapper lambda     
			try {															     
				URL url = new URL(location);									 
				myBook.loadImage(url);											 
			} catch (MalformedURLException urlE) {								 
				myBook.loadImage(new File(location));							 
			}																	 
			//konvertovanie Image to javafxImage								 
			bookImage.setImage(SwingFXUtils.toFXImage(myBook.getImage(), null)); 
		});																		 
		t.start();																 
																				 
		// zobrazenie textovej reprezentacie objektu							 
		plotArea.setText(myBook.toString());									 
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
		priceField.setText("");
		mainHeroField.setText("");
		imageLocationField.setText("");
		plotArea.setText("");
		bookImage.setImage(null);
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
	
	private boolean checkImageSuffix(String path) {
		boolean confirmed = false;
		String[] suffixes = {"jpg", "png", "bmp", "jpeg", "gif"};
		for (String suf : suffixes) {
			if (path.endsWith(suf) || path.endsWith(suf.toUpperCase())) {
				confirmed = true;
				break;
			}
		}
		return confirmed;
	}
	
}