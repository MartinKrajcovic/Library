package connections;

import java.io.File;
import java.io.IOException;

/**
 * Tato trieda bude zabezpecovat stiahnutie obrazku z internetu
 * a vrati nam nazov a umiestnenie obrazku v podobe objektu File
 */
public class ImageDownloader implements Downloader<File> {

	@Override
	public File download() throws IOException {
		return null;
	}
	
	
}
