package connections;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import books.Language;

/**
 * Tato trieda bude zabezpecovat stiahnutie obrazku z internetu
 * a vrati nam nazov a umiestnenie obrazku v podobe objektu File
 */
public class ImageDownloader {

	private URL imageAddress;
	private final String destination;
	
	public ImageDownloader(String imageAddress) throws MalformedURLException {
		this.imageAddress = new URL(imageAddress);
		this.destination = "destination";
	}
	
	public File download() throws IOException {
		//vracia subor obrazku
		return null;
	}
	
}
