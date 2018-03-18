package src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorBettingCenter implements ISpectator_BettingCenter, IBroker_BettingCenter {
	private final ReentrantLock mutex;
	private final Condition spectator_condition;
	private final Condition broker_condition;
	private final int numberOfSpectators;
	
	private int numberOfBets;
	
	public MonitorBettingCenter(int numberOfSpectators) {
		mutex = new ReentrantLock(true);
		spectator_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		this.numberOfSpectators = numberOfSpectators;
		numberOfBets=0;
	}
	@Override
	public void acceptTheBets() {
		
		mutex.lock();
		try {
			System.out.println("ACCEPTING BETS BROKER");
			while(numberOfBets<numberOfSpectators) {
				try {
					broker_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("BROKER WAKE UP");

			
		} finally {
			mutex.unlock();
		}
		
		
	}

	

	@Override
	public void honourTheBets() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeABet(int spectator_id) {
		mutex.lock();
		try {
			numberOfBets++;
			System.out.println("Spectator_"+spectator_id+" is betting!");
			if(numberOfBets == numberOfSpectators) {
				broker_condition.signal();
				numberOfBets=0;
			}
			
	
		} finally {
			mutex.unlock();
			
		}
		
	}

}
