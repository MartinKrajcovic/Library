package library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import books.Book;

public abstract class Library<T extends Book & Serializable> {
	
	private int books = 0;
	private List<T> library = new ArrayList<>();
	
	public boolean addBook(T book) {
		books++;
		return library.add(book);
	}

	public boolean dropBook(T book) {
		books--;
		return library.remove(book);
	}
	
	public int getBookCount() {
		return this.books;
	}
	
	
}
