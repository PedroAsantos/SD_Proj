package sharingRegions;


import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


import Interfaces.IMonitor_Stable;
import Interfaces.IRepository;
import java.rmi.RemoteException;


public class MonitorStable implements IMonitor_Stable {
	private final ReentrantLock mutex;
	private final Condition horse_condition;
	private final Condition broker_condition;
	
	private boolean goingToPaddock;
	private int horsesAtStable;
	private int horsesPerRace;
	private int totalHorses;
	private int horsesPaddock;
	private boolean horseCanNotGo;
	IRepository repo;
	
	public MonitorStable(IRepository repo) throws IOException {
		mutex = new ReentrantLock(true);
		horse_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		horsesAtStable=0;
		horseCanNotGo=true;
		//this.totalHorses=repo.getTotalHorses(); //tirar este comemnt
		this.totalHorses=4;
                this.repo = repo;
		//this.horsesPerRace=repo.getHorsesPerRace(); //tirar este comemnt
		this.horsesPerRace=4;
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
		mutex.lock();
		try {
			horsesAtStable++;
			System.out.print("Horse_"+horseId+" now on stable!\n");
			
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
			System.out.print("Horse_"+horseId+" exiting stable!\n");
			horsesAtStable--;
			if(goingToPaddock) {
				horsesPaddock++;
				repo.addHorsesToRun(horseId);
				if(horsesPaddock==horsesPerRace) {
					horseCanNotGo=true;
					horsesPaddock=0;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			mutex.unlock();
		}
		return goingToPaddock;
				
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
		System.out.println("summonHorsesToPaddock");
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

    @Override
    public void turnOffServer() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	


}
