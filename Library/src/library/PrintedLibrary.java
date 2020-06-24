package library;

import books.PrintedBook;

public class PrintedLibrary extends Library<PrintedBook>{

	private static PrintedLibrary library;
	
	public static PrintedLibrary getInstance() {
		return (library == null) ? new PrintedLibrary() : library;
	}
	
	
}
