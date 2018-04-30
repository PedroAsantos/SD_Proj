package sharingRegions;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.IBroker_Stable;
import Interfaces.IHorse_Stable;
import communication.Stub;


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
		
	}
	
	/**
	*	Wait that all the horses arrives to stable to wake up the broker
	* and then a returns an boolean that indicates if a horse is going to paddock
	*
	*	@param horseId Horse ID
	*	@return boolean goingToPaddock 
	*/
	@Override
	public boolean proceedToStable(int horseId) {
		String returnFunction;
		returnFunction=sendMessage("proceedToStable"+";"+horseId);
		
		if(returnFunction.equals("true")) {
			return true;
		}else {
			return false;
		}
		
				
	}
	/**
	*	When the event are ending the horses are going to the end of event
	*
	*	 
	*/
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
	/**
	*	All horses go to paddock
	*
	*	 
	*/
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
	
	public String sendMessage(String payload) {

		String hostName; // nome da máquina onde está o servidor
		int portNumb = 9991; // número do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}

}
