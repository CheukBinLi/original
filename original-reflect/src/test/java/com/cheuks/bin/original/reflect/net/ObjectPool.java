package java.com.cheuks.bin.original.reflect.net;

public interface ObjectPool<T> {

	public T getResource();

	public void returnResource(T t);

	public void resetPool();

	public int maxResource();

}
