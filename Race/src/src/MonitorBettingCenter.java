package src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorBettingCenter implements ISpectator_BettingCenter, IBroker_BettingCenter {
	private final ReentrantLock mutex;
	private final Condition spectator_condition;
	private final Condition broker_condition;
	private final int numberOfSpectators;
	
	private boolean brokerCanNotGo;
	private int numberOfBets;
	
	public MonitorBettingCenter(int numberOfSpectators) {
		mutex = new ReentrantLock(true);
		spectator_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		this.numberOfSpectators = numberOfSpectators;
		numberOfBets=0;
		brokerCanNotGo=true;
	}
	@Override
	public void acceptTheBets() {
		
		mutex.lock();
		try {
			System.out.println("ACCEPTING BETS BROKER");
			while(brokerCanNotGo) {
				try {
					broker_condition.await();
					System.out.println("Broker:" +numberOfBets);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("BROKER WAKE UP");
			brokerCanNotGo=true;
			
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
				//this can not be like this .. use boolean to wake up broker.
				brokerCanNotGo=false;
				numberOfBets=0;
			}
			
			System.out.println(numberOfBets+ " " + numberOfSpectators);
		} finally {
			mutex.unlock();
			
		}
		
	}

}
