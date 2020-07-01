package library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import books.PrintedBook;

@SuppressWarnings("serial")
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
		if (SAVED_LOCATION.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVED_LOCATION))) {
				library = (PrintedLibrary) ois.readObject();
			} catch (IOException | ClassNotFoundException ex) {
				// ak sa nepodari nacitat serializovany objekt
			}
		} else {
			setOrder((a, b) -> a.compareTo(b));
		}
	}
}
