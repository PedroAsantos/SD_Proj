package src;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorControlCenter implements ISpectator_Control, IBroker_Control{
	private final ReentrantLock mutex;
	private final Condition spectator_condition;
	private final Condition div;
	
	private boolean havaIWon;
	private boolean raceIsOn;
	private boolean spectatorHasToWait;
	public MonitorControlCenter() {
		mutex = new ReentrantLock(true);
		spectator_condition = mutex.newCondition();
		div = mutex.newCondition();
		spectatorHasToWait=true;
		raceIsOn=true;
		havaIWon=true;
	}
	
	

	@Override
	public void goWatchTheRace(int spectator_id) {
		
		mutex.lock();
		try {
			
			System.out.println("Spectator_"+spectator_id+" is watching the race!");
			while(raceIsOn) {
				try {
					spectator_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
						
			
	
		}finally {
			mutex.unlock();
		}
		
	}

	@Override
	public boolean haveIwon() {
		// TODO Auto-generated method stub
		mutex.lock();
		try {
			
			while(havaIWon) {
				try {
					//usar outra condição?
					spectator_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} finally {
			mutex.unlock();
		}
		
		
		
		return false;
		
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
	public void reportResults(List<Integer> result) {
		mutex.lock();
		try {
			
		} finally {
			// TODO: handle finally clause
			mutex.unlock();
		}
		
		
		
	}
	@Override
	public void entertainTheGuests() {
		// TODO Auto-generated method stub
		
	}

}
