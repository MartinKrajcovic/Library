package books;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import connections.ImageDownloader;

public class PrintedBook extends Book implements Comparable<PrintedBook> {
	
	// POZOR ! -> BufferedImage nie je mozne serializovat
	private static final long serialVersionUID = 128L;
	private Binding binding;
	private PrintedFormat format;
	private transient BufferedImage image;
	private String ISBN;
	private int weight;
	
	// initialization block
	{
		binding = Binding.Undefined;
		format = PrintedFormat.Undefined;
		ISBN = "Undefined";
		weight = 0;
		try {
			image = ImageIO.read(new File("src/images/default/no_book.jpg"));
		} catch (IOException e) {
			System.err.println("could not load a default image");
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
	
	/**
	 * Nacitanie obrazka zo suboroveho systemu
	 */
	public void loadImage(File imagePath) {
		if (imagePath != null) {
			try {
				image = ImageIO.read(ImageDownloader.copyImage(imagePath.toPath()));
			} catch (IOException e) {
				// invalid path
			}
		}
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	public void setISBN(String ISBN) {
		if (!ISBN.trim().isEmpty())
			this.ISBN = ISBN;
	}
	
	public String getISBN() {
		return ISBN;
	}
	
	public void setWeight(int weight) {
		if (weight > 0)
			this.weight = weight;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	// pozriet knihu na strane 554 pre citatelnejsi zapis
	@Override
	public int compareTo(PrintedBook obj) {
		if (this.getAuthor().substring(this.getAuthor().lastIndexOf(' ') + 1).compareTo(obj.getAuthor().substring(obj.getAuthor().lastIndexOf(' ') + 1)) > 0) {
			return 1;
		} else if (this.getAuthor().substring(this.getAuthor().lastIndexOf(' ') + 1).compareTo(obj.getAuthor().substring(obj.getAuthor().lastIndexOf(' ') + 1)) < 0) {
			return -1;
		} else {
			if (this.getNumberInSeries() > obj.getNumberInSeries()) {
				return 1;
			} else if (this.getNumberInSeries() < obj.getNumberInSeries()) {
				return -1;
			} else {
				if (this.getPublished() > obj.getPublished()) {
					return 1;
				} else if (this.getPublished() < obj.getPublished()) {
					return -1;
				}
			}
		}
		return 0;	
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
				+ "\nPages: " + getPages()
				+ "\nChapters: " + getChapters()
				+ "\nPublished: " + getPublished()
				+ "\nVersion: " + getVersion()
				+ "\nISBN: " + getISBN()
				+ "\nMain Hero: " + getMainHero()
				+ "\nPrice: " + getPrice()
				+ "\n======= Plot =======\n " + getPlot();
	}	
	
}
