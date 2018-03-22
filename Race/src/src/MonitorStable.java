package src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorStable implements IHorse_Stable, IBroker_Stable {
	private final ReentrantLock mutex;
	private final Condition horse_condition;
	private final Condition broker_condition;
	
	private int horsesAtStable;
	private int totalHorses;
	private boolean horseCanNotGo;
	Repository repo;
	
	public MonitorStable(int totalHorses, Repository repo) {
		mutex = new ReentrantLock(true);
		horse_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		horsesAtStable=0;
		horseCanNotGo=true;
		this.totalHorses=totalHorses;
		this.repo = repo;
	}
	
	@Override
	public void proceedToStable(int id) {
		mutex.lock();
		try {
			horsesAtStable++;
			System.out.print("Horse_"+id+" now on stable!\n");
			
			if(horsesAtStable == totalHorses) {
				broker_condition.signal();
			}
			while(horseCanNotGo) {
				try {
					horse_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			horsesAtStable--;
			if(horsesAtStable==0) {
				horseCanNotGo=true;
			}
			
		} finally {
			// TODO: handle finally clause
			mutex.unlock();
		}
				
	}
	
	
	@Override
	public void summonHorsesToPaddock() {
		// TODO Auto-generated method stub
		mutex.lock();
		try {
			
			while(horsesAtStable < totalHorses) {
				try {
					broker_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			horseCanNotGo=false;
			
			horse_condition.signalAll();
			
			System.out.println("ALL HORSES ARE GOING TO PADDOCK");
			
		} finally {
			mutex.unlock();
		}
	}
	

	@Override
	public void proceedToPaddock() {
		mutex.lock();
		try {
			
		} finally {
			mutex.unlock();
		}
		
		
		
	}


}
