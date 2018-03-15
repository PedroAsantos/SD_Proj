package src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorControlCenter implements ISpectator_Control, IBroker_Control{
	private final ReentrantLock r1;
	private final Condition inc;
	private final Condition div;
	
	public MonitorControlCenter() {
		r1 = new ReentrantLock(true);
		inc = r1.newCondition();
		div = r1.newCondition();
	}
	@Override
	public void waitForNextRace() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goCheckHorses() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goWatchTheRace() {
		// TODO Auto-generated method stub
		
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
