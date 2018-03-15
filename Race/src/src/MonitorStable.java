package src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorStable implements IHorse_Stable {
	private final ReentrantLock mutex;
	private final Condition inc;
	private final Condition div;
	
	private int horsesAtStable;
	
	public MonitorStable() {
		mutex = new ReentrantLock(true);
		inc = mutex.newCondition();
		div = mutex.newCondition();
		horsesAtStable=0;
	}
	
	@Override
	public void proceedToStable() {
		mutex.lock();
			
		mutex.unlock();
		
	}

	@Override
	public void proceedToPaddock() {
		mutex.lock();
		
		mutex.unlock();
		
	}
	
}
