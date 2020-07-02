package library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import books.Book;

public abstract class Library<T extends Book & Serializable & Comparable<T>> {
	
	private List<T> library;
	private Comparator<T> ordering;
	
	// upravovaci mechanizmus
	public boolean addBook(T book) {
		boolean success;
		if ((success = library.add(book))) {
			library.sort(ordering);
		}
		return success;
	}

	// upravovaci mechanizmus
	public boolean dropBook(T book) {
		return library.remove(book);
	}
	
	public int countBooks() {
		return library.size();
	}
	
	public void createLibrary(List<T> library) {
		this.library = new ArrayList<>(library);
	}
	
	public List<T> getLibrary() {
		return (this.library != null) ? this.library : new ArrayList<>();
	}
	
	public abstract boolean dropLibrary();
	
	// zoradovaci mechanizmus
	public void setOrdering(Comparator<T> ordering) {
		this.ordering = ordering;
	}
	
}
