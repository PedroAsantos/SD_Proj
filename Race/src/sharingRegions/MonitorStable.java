package sharingRegions;

import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.IBroker_Stable;
import Interfaces.IHorse_Stable;
import stakeholders.Horse;

public class MonitorStable implements IHorse_Stable, IBroker_Stable {
	private final ReentrantLock mutex;
	private final Condition horse_condition;
	private final Condition broker_condition;
	
	private boolean goingToPaddock;
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
		this.goingToPaddock=true;
		horsePerformance = repo.gethorsePerformance();
		
	}
	
	@Override
	public boolean proceedToStable(Horse horse) {
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
					e.printStackTrace();
				}
			}
			
			horsesAtStable--;
			if(goingToPaddock) {
				horsesPaddock++;
				repo.addHorsesToRun(horse.getID());
				if(horsesPaddock==horsesPerRace) {
					horseCanNotGo=true;
					horsesPaddock=0;
				}
			}
		} finally {
			mutex.unlock();
		}
		return goingToPaddock;
				
	}
	
	@Override
	public void summonHorsesToEnd() {
		mutex.lock();
		try {
			goingToPaddock=false;
			while(horsesAtStable < totalHorses) {
				try {
					broker_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			horseCanNotGo=false;
			horse_condition.signalAll();
			
			System.out.println("ALL HORSES ARE GOING TO END OF EVENT");
		} finally {
			mutex.unlock();
		}
	}
	
	@Override
	public void summonHorsesToPaddock() {
		mutex.lock();
		try {
			goingToPaddock=true;
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
