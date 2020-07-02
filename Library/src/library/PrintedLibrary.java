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
	
	// refaktor try bloku a if bloku -> zvazit odstranenie try-with-resources
	@SuppressWarnings("unchecked")
	private void loadLibrary() {
		if (SAVED_LOCATION.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVED_LOCATION))) {
				createLibrary((ArrayList<PrintedBook>) ois.readObject());
			} catch (IOException | ClassNotFoundException ex) {
				setOrdering((a, b) -> a.compareTo(b));
			}
		} else {
			setOrdering((a, b) -> a.compareTo(b));
		}
	}

	// TOTO JE BORDEEEEEEEL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// refaktor try blokov a if bloku + overit, ci sa musi vytvarat len subor alebo aj cesta
	public void saveLibrary() {
		if (!SAVED_LOCATION.exists()) {
			try {
				SAVED_LOCATION.getParentFile().mkdirs();
				SAVED_LOCATION.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(SAVED_LOCATION))){
			ois.writeObject(getLibrary());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// treba lepsiu implementaciu!!! nech overuje atributy suboru
	@Override
	public boolean dropLibrary() {
		boolean success;
		try {
			getLibrary().clear();
			Files.delete(SAVED_LOCATION.toPath());
			success = true;
		} catch (IOException e) {
			success = false;
		}
		return success;
	}
}
