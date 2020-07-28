package connections;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.RecursiveAction;

/**
 * Tato trieda bude zabezpecovat stiahnutie obrazku z internetu a vrati nam
 * nazov a umiestnenie obrazku v podobe objektu File
 */
@SuppressWarnings("serial")
public class ImageDownload extends RecursiveAction {

	private static final String DESTINATION = "src/images/";
	private static String imageFormats = "jpg png jpeg gif bmp";
	private static final String[] SUFFIXES = (imageFormats + " " + imageFormats.toUpperCase()).split(" ");
	private static File imageFile;

	private final int limit = 1_000_000;	// 1MB
	private int start, middle, end;
	private byte[] data;
	private URL imageAddress;

	public ImageDownload(URL imageAddress, int start) {
		this.imageAddress = imageAddress;
		this.start = start;
		imageFile = new File(getFileDestination());
	}

	public ImageDownload(URL imageAddress, byte[] data, int start, int end) {
		this.imageAddress = imageAddress;
		this.data = data;
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		BufferedInputStream iStream = null;
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) imageAddress.openConnection();
			con.connect();
			if (data == null) {
				try {
					data = new byte[con.getContentLength()];
				} catch (NegativeArraySizeException nae) {
					return;
				}
			}
			end = (end > 0) ? end : data.length;
			if ((end - start) < limit) {
				iStream = new BufferedInputStream(con.getInputStream());
				iStream.skip(start);
				int temp;
				for (int i = start; i < end; i++) {
					if ((temp = iStream.read()) != -1) {
						data[i] = (byte) temp;
					}
				}
			} else {
				middle = (start + end) / 2;
				ImageDownload.invokeAll(
						new ImageDownload(imageAddress, data, start, middle),
						new ImageDownload(imageAddress, data, middle, end));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (iStream != null)
					iStream.close();
				if (con != null)
					con.disconnect();
			} catch (IOException e) {
				// no need
			}
		}
	}

	public File getFile() {
		try {
			Files.write(imageFile.toPath(), data);
			return imageFile;
		} catch (IOException e) {
			return imageFile;
		}
	}
	
	/**
	 * Tato metoda overuje ci je vlozeny retazec reprezentujuci umiestnenie suboru
	 * platnym obrazkom na zaklade pripony.
	 */
	public static boolean checkImageSuffix(String path) {
		boolean confirmed = false;
		for (String suf : SUFFIXES) {
			if (path.endsWith(suf)) {
				confirmed = true;
				break;
			}
		}
		return confirmed;
	}

	/**
	 * Tato metoda kopiruje subor obrazku do destinacie vybranej pre obrazky.. Ak uz
	 * subor v tejto lokacii existuje, tak je vrateny ten existujuci subor. V
	 * opacnom pripade bude vrateny novy subor, ktory bol nakopirovany do destinacie
	 * vybranej pre obrazky.
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
	 * Tato metoda vracia kompletne umiestnenie suboru aj s jeho nazvom, ktory je
	 * ziskany z url adresy..
	 * 
	 * Ak url adresa obsahuje znaky za priponou obrazku, tak sa tento zvysny udaj
	 * odstrani a ostane retazec konciaci platnou priponou.
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
