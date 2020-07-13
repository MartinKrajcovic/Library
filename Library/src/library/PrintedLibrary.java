package library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import books.PrintedBook;

public class PrintedLibrary extends Library<PrintedBook> {

	private final File SAVED_LOCATION;
	private static PrintedLibrary library;
	
	private PrintedLibrary() {
		SAVED_LOCATION = new File("src/library/save/pbl.dat");
		loadLibrary();
	}
	
	public static PrintedLibrary getInstance() {
		return (library == null) ? new PrintedLibrary() : library;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadLibrary() {
		if (!SAVED_LOCATION.exists()) {
			setOrdering(PrintedBook::compareTo);
			return;
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVED_LOCATION))) {
			createLibrary((ArrayList<PrintedBook>) ois.readObject());
		} catch (IOException | ClassNotFoundException e) {
			setOrdering(PrintedBook::compareTo);
		}
	}
	
	public void saveLibrary() throws IOException {
		SAVED_LOCATION.getParentFile().mkdirs();
		try (ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(SAVED_LOCATION))){
			ois.writeObject((ArrayList<PrintedBook>) getLibrary());
		}
	}
	
	@Override
	public boolean dropLibrary() {
		try {
			getLibrary().clear();
			return Files.deleteIfExists(SAVED_LOCATION.toPath());
		} catch (IOException e) {
			return false;
		}	
	}
}
