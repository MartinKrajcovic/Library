package connections;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageDownloader implements Downloader<BufferedImage> {

	@Override
	public BufferedImage download(URL address) {
		String fileName = address.getFile();
		File file;
		InputStream iStream = null;
		OutputStream oStream = null;
		try {
			HttpURLConnection con = (HttpURLConnection) address.openConnection();
			con.connect();
			file = new File(fileName.substring(fileName.lastIndexOf('/') + 1));
			iStream = con.getInputStream();
			oStream = new FileOutputStream(file);
			int i = 0;
			while ((i = iStream.read()) != -1) {
				oStream.write(i);
			}
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (iStream != null)
					iStream.close();
				if (oStream != null)
					oStream.close();
			} catch (IOException ioe) {
				// no need
			}
		}
	}
	
	
}
