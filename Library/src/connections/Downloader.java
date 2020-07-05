package connections;

public interface Downloader<T> {

	public T download() throws Exception;
	
}
