package books;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import connections.ImageDownloader;

public class PrintedBook extends Book {
	
	private Binding binding;
	private PrintedFormat format;
	private BufferedImage image;
	private String ISBN;
	
	// initialization block
	{
		binding = Binding.Undefined;
		format = PrintedFormat.Undefined;
		ISBN = "Undefined";
		try {
			image = ImageIO.read(new File("src/images/no_book.jpg"));
		} catch (IOException e) {
			System.err.println("no_book.jpg image problem!");
		}
	}
	
	public PrintedBook (
			String author, String title, String genre, String publisher,
			Language language, Binding binding, PrintedFormat format) {
		super (author, title, genre);
		setLanguage(language);
		setPublisher(publisher);
		this.binding = binding;
		this.format = format;
	}
	
	public void setBinding(Binding binding) {
		this.binding = binding;
	}
	
	public Binding getBinding() {
		return this.binding;
	}
	
	public void setFormat(PrintedFormat format) {
		this.format = format;
	}
	
	public PrintedFormat getFormat() {
		return this.format;
	}
	
	// nacitanie obrazka zo suboru
	public void loadImage(File imagePath) {
		try {
			image = ImageIO.read(imagePath);
		} catch (IOException e) {
			System.err.println("no_book.jpg image problem!");
		}
	}
	
	// nacitanie obrazku z internetovej adresy
	public void loadImage(URL imagePath) {
		BufferedImage image = new ImageDownloader().download(imagePath);
		if (image != null)
			this.image = image;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	public void setISBN(String ISBN) {
		if (!ISBN.isEmpty())
			this.ISBN = ISBN;
	}
	
	public String getISBN() {
		return ISBN;
	}
	
	@Override
	public String toString() {
		return "Author: " + getAuthor() 
				+ "\nTitle: " + getTitle()
				+ "\nGenre: " + getGenre()
				+ "\nPublisher: " + getPublisher()
				+ "\nLanguage: " + getLanguage()
				+ "\nBinding: " + getBinding()
				+ "\nFormat: " + getFormat()
				+ "\nPublished: " + getPublished()
				+ "\nPrice: " + getPrice()
				+ "\nPages: " + getPages()
				+ "\nChapters: " + getChapters()
				+ "\nMain Hero: " + getMainHero()
				+ "\n======= Plot =======\n " + getPlot();
	}

}
