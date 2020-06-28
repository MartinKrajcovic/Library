package connections;

import java.io.IOException;

public interface Downloader<T> {

	public T download() throws IOException;
	
}
