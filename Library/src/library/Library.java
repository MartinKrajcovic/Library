package library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import books.Book;

public abstract class Library<T extends Book & Serializable & Comparable<T>> {
	
	private int books = 0;
	private List<T> library = new ArrayList<>();
	private Comparator<T> order;
	
	// upravovaci mechanizmus
	public boolean addBook(T book) {
		boolean success;
		if ((success = library.add(book))) {
			books++;
			library.sort(order);
		}
		return success;
	}

	// upravovaci mechanizmus
	public boolean dropBook(T book) {
		boolean success;
		if ((success = library.remove(book))) {
			books--;
		}
		return success;
	}
	
	public int getBookCount() {
		return this.books;
	}
	
	public List<T> getLibraryContent() {
		return new ArrayList<>(library);
	}
	
	// zoradovaci mechanizmus
	public void setOrder(Comparator<T> order) {
		this.order = order;
	}
	
}
