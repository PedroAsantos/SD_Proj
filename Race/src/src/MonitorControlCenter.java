package src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorControlCenter implements ISpectator_Control, IBroker_Control{
	private final ReentrantLock mutex;
	private final Condition spectator_condition;
	private final Condition div;
	
	private boolean spectatorHasToWait;
	public MonitorControlCenter() {
		mutex = new ReentrantLock(true);
		spectator_condition = mutex.newCondition();
		div = mutex.newCondition();
		spectatorHasToWait=true;
	}
	@Override
	public void waitForNextRace() {
		//não basta a função goCheckHorses e eles ficam lá a dormir? 
	/*	mutex.lock();
		try {
			
			while(spectatorHasToWait) {
				try {
					spectator_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} finally {
			mutex.unlock();
		}
		*/
	}


	@Override
	public void goWatchTheRace() {
		
		
	}

	@Override
	public void haveIwon() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goCollectTheGains() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void relaxABit() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void summonHorsesToPaddock() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void reportResults() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void entertainTheGuests() {
		// TODO Auto-generated method stub
		
	}

}
