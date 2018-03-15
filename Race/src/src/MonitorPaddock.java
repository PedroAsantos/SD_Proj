package src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorPaddock implements IHorse_Paddock {
	private final ReentrantLock mutex;
	private final Condition inc;
	private final Condition div;
	
	private int horsesInPaddock;
	
	public MonitorPaddock() {
		mutex = new ReentrantLock(true);
		inc = mutex.newCondition();
		div = mutex.newCondition();
		horsesInPaddock=0;
	}
	
	@Override
	public void proceedToStartLine() {
		// TODO Auto-generated method stub
		mutex.lock();
		horsesInPaddock--;
		
			
		
		mutex.unlock();
		
	}
	public void proceedToPaddock() {
		mutex.lock();
		
		horsesInPaddock++;
		
			
		
		mutex.unlock();
	}
	
}
