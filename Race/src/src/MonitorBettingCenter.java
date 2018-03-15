package src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorBettingCenter implements ISpectator_BettingCenter, IBroker_BettingCenter {
	private final ReentrantLock r1;
	private final Condition inc;
	private final Condition div;
	
	public MonitorBettingCenter() {
		r1 = new ReentrantLock(true);
		inc = r1.newCondition();
		div = r1.newCondition();
	}
	@Override
	public void acceptTheBets() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startTheRace() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void honourTheBets() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeABet() {
		// TODO Auto-generated method stub
		
	}

}
