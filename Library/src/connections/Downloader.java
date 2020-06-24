package connections;

import java.net.URL;

public interface Downloader<T> {

	public T download(URL address);
	
}
