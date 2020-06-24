package library;

import java.util.ArrayList;
import java.util.List;

import books.Book;

public abstract class Library<T extends Book> {
	
	private List<T> library = new ArrayList<>();
	
	public boolean addBook(T book) {
		if (library.add(book))
			return true;
		return false;
	}
	
	
}
