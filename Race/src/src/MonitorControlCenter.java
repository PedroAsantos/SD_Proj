package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorControlCenter implements ISpectator_Control, IBroker_Control{
	private final ReentrantLock mutex;
	private final Condition spectator_condition;
	private final Condition div;
	
	private int spectatorsWatchingRace;
	private boolean waitinghaveIwon;
	private boolean raceIsOn;
	private boolean spectatorHasToWait;
	private List<Integer> winners;
	private HashMap<Integer,List<double[]>> bets;
	Repository repo;

	public MonitorControlCenter(Repository repo) {
		mutex = new ReentrantLock(true);
		spectator_condition = mutex.newCondition();
		div = mutex.newCondition();
		spectatorHasToWait=true;
		raceIsOn=true;
		waitinghaveIwon=true;
		this.repo=repo;
		this.spectatorsWatchingRace=0;
	}
	
	

	@Override
	public void goWatchTheRace(int spectator_id) {
		
		mutex.lock();
		try {
			spectatorsWatchingRace++;
			System.out.println("Spectator_"+spectator_id+" is watching the race!");
			while(raceIsOn) {
				try {
					spectator_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Spectator_"+spectator_id+" exiting of the race");
			spectatorsWatchingRace--;
			if(spectatorsWatchingRace==0) {
				raceIsOn=true;
			}
	
		}finally {
			mutex.unlock();
		}
		
	}

	@Override
	public boolean haveIwon(int spectator_id) {
		// TODO Auto-generated method stub
		boolean iWon=false;
		mutex.lock();
		try {
			
		/*	while(waitinghaveIwon) {
				try {
					//usar outra condicao?
					spectator_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			bets = repo.getspectatorBets();
			
			
		
			List<double[]> betsOnHorse;
			double[] bet;
			
			for(int i = 0;i<winners.size();i++) {
				if(bets.containsKey(winners.get(i))) {
					betsOnHorse=bets.get(winners.get(i));
					for(int c=0;c<betsOnHorse.size();c++) {
						bet=betsOnHorse.get(c);
						if((int)bet[0]==spectator_id) {
							iWon=true;
							break;
						}
					}
				}
				
			}
			
			System.out.println("Spectator_"+spectator_id+" won: "+iWon);
		} finally {
			mutex.unlock();
		}
		
		return iWon;
	}
	@Override
	public boolean noMoreRaces() {
		int numberOfRaces=0;
		mutex.lock();
		try {
			numberOfRaces = repo.getNumberOfRaces();
		} finally {
			mutex.unlock();
		}
		if(numberOfRaces<=0) {
			return true;
		}else {
			return false;
		}
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
			winners = new ArrayList<Integer>(result);
			raceIsOn=false;
			repo.clearhorsesRunning();
			System.out.println("Reporting Result to Spectators");
			spectator_condition.signalAll();
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
