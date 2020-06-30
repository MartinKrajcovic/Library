package library;

import java.io.File;

import books.PrintedBook;

public class PrintedLibrary extends Library<PrintedBook>{

	private final File SAVED_LOCATION;
	private static PrintedLibrary library;
	
	private PrintedLibrary() {
		SAVED_LOCATION = new File("src/library/save/pbl.dat");
		loadLibrary();
	}
	
	public static PrintedLibrary getInstance() {
		return (library == null) ? new PrintedLibrary() : library;
	}
	
	// deserialization
	private void loadLibrary() {
		
	}
}
