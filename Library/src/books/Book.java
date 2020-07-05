package books;

import java.io.Serializable;
import java.time.Year;

public abstract class Book implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String author;
	private final String title;
	private final String genre;
	private String publisher;
	private Language language;
	
	private double price;
	private int pages;
	private int chapters;
	private int published;
	private int edition;
	private int orderInSeries;
	
	private String mainHero;
	private String plot;
	private String ReadState;
	
	{
		publisher = mainHero = plot = "Undefined";
		pages = chapters = 1;
		language = Language.Undefined;
		published = orderInSeries = 0;
		ReadState = "Unread";
	}
	
	public Book (String author, String title, String genre) {
		this.author = author;
		this.title = title;
		this.genre = genre;
	}

	public String getAuthor() {
		return this.author;
	}

	public String getTitle() {
		return this.title;
	}

	public String getGenre() {
		return this.genre;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Language getLanguage() {
		return this.language;
	}
	
	public void setPublisher(String publisher) {
		if (!publisher.trim().isEmpty())
			this.publisher = publisher;
	}
	
	public String getPublisher() {
		return this.publisher;
	}

	public void setPrice(double price) {
		if (price > 0)
			this.price = price;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public void setPages(int pages) {
		if (pages > 1)
			this.pages = pages;
	}
	
	public int getPages() {
		return this.pages;
	}
	
	public void setChapters(int chapters) {
		if (chapters > 1)
			this.chapters = chapters;
	}
	
	public int getChapters() {
		return this.chapters;
	}
	
	public void setPublished(int published) {
		this.published = published;
	}
	
	public void setPublished(Year published) {
		this.published = published.getValue();
	}
	
	public int getPublished() {
		return this.published;
	}
	
	public void setEdition(int edition) {
		if (edition > 0)
			this.edition = edition;
	}
	
	public int getEdition() {
		return edition;
	}
	
	public void setOrderInSeries(int orderInSeries) {
		this.orderInSeries = orderInSeries;
	}
	
	public int getOrderInSeries() {
		return this.orderInSeries;
	}
	
	public void setMainHero(String mainHero) {
		if (!mainHero.trim().isEmpty())
			this.mainHero = mainHero;
	}
	
	public String getMainHero() {
		return this.mainHero;
	}
	
	public void setReadState(String ReadState) {
		this.ReadState = ReadState;
	}
	
	public String  getReadState() {
		return this.ReadState;
	}
	
	
	public void loadPlot(String plot) {
		if (!plot.trim().isEmpty())
			this.plot = plot;
	}

	public String getPlot() {
		return this.plot;
	}

	@Override
	public String toString() {
		return "Author: " + this.author 
				+ "\nTitle: " + this.title
				+ "\nGenre: " + this.genre
				+ "\nLanguage: " + this.language
				+ "\nPublisher: " + this.publisher
				+ "\nPrice: " + this.price
				+ "\nPages: " + this.pages
				+ "\nChapters: " + this.chapters
				+ "\nPublished: " + this.published
				+ "\nMain Hero: " + this.mainHero
				+ "\nPlot: " + this.plot;
	}
}

