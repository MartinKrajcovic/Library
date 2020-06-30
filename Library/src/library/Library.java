package library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import books.Book;

public abstract class Library<T extends Book & Serializable> {
	
	private List<T> library = new ArrayList<>();
	
	public boolean addBook(T book) {
		return library.add(book);
	}

	
}
