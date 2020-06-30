package library;

import books.PrintedBook;

public class PrintedLibrary extends Library<PrintedBook>{

	private static PrintedLibrary library;
	
	private PrintedLibrary() {
		loadLibrary();
	}
	
	public static PrintedLibrary getInstance() {
		return (library == null) ? new PrintedLibrary() : library;
	}
	
	// deserialization
	private void loadLibrary() {
		
	}
}
