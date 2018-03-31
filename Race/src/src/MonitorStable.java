package src;

import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import stakeholders.Horse;

public class MonitorStable implements IHorse_Stable, IBroker_Stable {
	private final ReentrantLock mutex;
	private final Condition horse_condition;
	private final Condition broker_condition;
	
	private int horsesAtStable;
	private int horsesPerRace;
	private int totalHorses;
	private int horsesPaddock;
	private boolean horseCanNotGo;
	private HashMap<Integer,Integer> horsePerformance;
	Repository repo;
	
	public MonitorStable(Repository repo) {
		mutex = new ReentrantLock(true);
		horse_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		horsesAtStable=0;
		horseCanNotGo=true;
		this.totalHorses=repo.getTotalHorses();
		this.repo = repo;
		this.horsesPerRace=repo.getHorsesPerRace();
		this.horsesPaddock=0;
		horsePerformance = repo.gethorsePerformance();
		
	}
	
	@Override
	public void proceedToStable(Horse horse) {
		mutex.lock();
		try {
			horsesAtStable++;
			horsePerformance.put(horse.getID(),horse.getPerformance());
			System.out.print("Horse_"+horse.getID()+" now on stable!\n");
			
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
			horsesPaddock++;
			repo.addHorsesToRun(horse.getID());
			if(horsesPaddock==horsesPerRace) {
				horseCanNotGo=true;
				horsesPaddock=0;
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
	


}
