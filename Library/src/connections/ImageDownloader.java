package connections;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Tato trieda bude zabezpecovat stiahnutie obrazku z internetu a vrati nam
 * nazov a umiestnenie obrazku v podobe objektu File
 */
public class ImageDownloader implements Downloader<File> {

	private static final String DESTINATION = "src/images/";
	private static final String[] SUFFIXES = { "jpg", "png", "jpeg", "gif", "bmp" };
	private URL imageAddress;

	public ImageDownloader(String imageAddress) throws MalformedURLException {
		this.imageAddress = new URL(imageAddress);
	}

	/**
	 * Tato metoda overuje ci je vlozeny retazec reprezentujuci
	 * umiestnenie suboru platnym obrazkom na zaklade pripony.
	 */
	public static boolean checkImageSuffix(String path) {
		boolean confirmed = false;
		for (String suf : SUFFIXES) {
			if (path.endsWith(suf) || path.endsWith(suf.toUpperCase())) {
				confirmed = true;
				break;
			}
		}
		return confirmed;
	}

	/**
	 * Tato metoda kopiruje subor obrazku do destinacie vybranej pre obrazky..
	 * Ak uz subor v tejto lokacii existuje, tak je vrateny ten existujuci subor.
	 * V opacnom pripade bude vrateny novy subor, ktory bol nakopirovany do 
	 * destinacie vybranej pre obrazky.
	 */
	public static File copyImage(Path source) throws IOException {
		Path target = Paths.get(DESTINATION + source.getFileName());
		try {
			return Files.copy(source, target).toFile();
		} catch (FileAlreadyExistsException faee) {
			return target.toFile();
		}
	}
	
	/**
	 * Tato metoda ma za ulohu stiahnut obrazok z internetu a vratit
	 * objekt stiahnuteho suboru ulozeneho v subsysteme.
	 */
	@Override
	public File download() {
		File imageFile = new File(getFileDestination());
		InputStream iStream = null;
		OutputStream oStream = null;
		try {
			HttpURLConnection con = (HttpURLConnection) imageAddress.openConnection();
			con.connect();
			iStream = con.getInputStream();
			oStream = new FileOutputStream(imageFile);
			int i = 0;
			while ((i = iStream.read()) != -1) {
				oStream.write(i);
			}
			return imageFile;
		} catch (IOException ioe) {
			return null;
		} finally {
			try {
				if (iStream != null)
					iStream.close();
				if (oStream != null)
					oStream.close();
			} catch (IOException e) {
				// no need
			}
		}
	}

	/**
	 * Tato metoda vracia kompletne umiestnenie suboru aj s jeho nazvom,
	 * ktory je ziskany z url adresy..
	 * 
	 * Ak url adresa obsahuje znaky za priponou obrazku, tak sa
	 * tento zvysny udaj odstrani a ostane retazec konciaci platnou priponou.
	 * 
	 * Priklad: https://mrtns.eu/tovar/_1/171/l171816.jpg?v=1592301659
	 */
	private String getFileDestination() {
		String fileName = imageAddress.getFile();
		fileName = DESTINATION + fileName.substring(fileName.lastIndexOf('/') + 1);
		for (String suf : SUFFIXES) {
			if (fileName.contains(suf) && !fileName.endsWith(suf)) {
				fileName = fileName.substring(0, fileName.lastIndexOf(suf) + suf.length());
				break;
			}
		}
		return fileName;
	}

}
