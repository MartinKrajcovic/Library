package library;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import books.Book;

public abstract class Library<T extends Book & Comparable<T>> {
	
	private List<T> library = new ArrayList<>();
	private Comparator<T> ordering;
	
	public boolean addBook(T book) {
		boolean success;
		if ((success = library.add(book))) {
			library.sort(ordering);
		}
		return success;
	}

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
		return this.library;
	}
	
	public abstract void loadLibrary() throws Exception;
	
	public abstract boolean dropLibrary();
	
	public void setOrdering(Comparator<T> ordering) {
		this.ordering = ordering;
	}
	
}
