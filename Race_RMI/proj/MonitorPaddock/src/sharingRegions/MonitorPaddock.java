package sharingRegions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.IHorse_Paddock;
import Interfaces.ISpectator_Paddock;


public class MonitorPaddock implements IHorse_Paddock, ISpectator_Paddock {
	private final ReentrantLock mutex;
	private final Condition horse_condition;
	private final Condition spectatorWaiting_condition;
	private final Condition spectatorCheckHorses_condition;
	private final int horsesPerRace;
	private final int totalSpectators;
	
	private boolean horsesCanNotGo;
	private boolean spectatorsCheckingHorses;
	private int horsesInPaddock;
	private int spectatorsInPaddock;
	List<Integer> horsesRunning;
	private HashMap<Integer,Integer> horsePerformance;
	private List<Integer> pickHorse;
	private HashMap<Integer,Double> horseProbabilities;
	Repository repo;
	
	public MonitorPaddock(Repository repo) throws IOException {
		mutex = new ReentrantLock(true);
		horse_condition = mutex.newCondition();
		spectatorWaiting_condition = mutex.newCondition();
		spectatorCheckHorses_condition = mutex.newCondition();
		this.horsesPerRace=repo.getHorsesPerRace();
		this.horsesCanNotGo=true;
		this.totalSpectators=repo.getNumberOfSpectators();
		spectatorsCheckingHorses=true;
		spectatorsInPaddock=0;
		horsesInPaddock=0;
		horsesRunning = new ArrayList<Integer>();
		horsePerformance = new HashMap<Integer,Integer>();
		pickHorse= new ArrayList<Integer>();
		horseProbabilities = new HashMap<Integer,Double>();
		this.repo = repo;
	}
	

	/**
	*	Function for horses to stay in the paddock. They are awake at the end of the spectators all have seen the horses.
	*
	*	@param horseId Horse ID 
	*	@param performance Performance of the horse
	*/
	@Override
	public void proceedToPaddock(int horseId,int performance) {
		
		mutex.lock();
		try {
			
			horsesInPaddock++;
			System.out.println("Horse_"+horseId+" is going to paddock!");
			
			horsesRunning.add(horseId);
			horsePerformance.put(horseId, performance);

			repo.setHorsePerformance(horseId, performance);
			repo.sethorseruns(horseId,0);					
			if(horsesInPaddock==horsesPerRace) {
				spectatorWaiting_condition.signalAll();
			}
			
			try {
				 while(horsesCanNotGo) {
					 horse_condition.await();
				 }
			}catch(Exception e) {
				System.out.println("Horse ProceedToPaddock (paddockMonitor) "+e);
			}
			
				 
				 horsesInPaddock--;
				 
				 if(horsesInPaddock==0) {
					 horsesCanNotGo=true;
				 }
				 
				 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			mutex.unlock();
		}
		
	
	}
	/**
	*	Function for spectators to be able to wait for the horses to be in the paddock.
	*
	*	@param spectator_id Spectator ID 
	*/
	@Override
	public void waitForNextRace(int spectator_id) {
		mutex.lock();
		try {
			System.out.println("Spectator_"+spectator_id+" is waiting for checking the horses!");
			while(horsesInPaddock < horsesPerRace) {
				try {
					spectatorWaiting_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} finally {
			mutex.unlock();
		}
		
	}
	/**
	*	Function for spectators to be able to see and choose the horse that they will bet. In this function the probabilities of each horse winning are calculated.
	*
	*	@param spectator_id Spectator ID 
	*	@return int the id of the chosen horse.
	*/
	@Override
	public int goCheckHorses(int spectator_id) {
		int horsePicked = 0; 
		mutex.lock();
		try {
			System.out.println("Spectator_"+spectator_id+" is checking the horses!");
			spectatorsInPaddock++;
			
			if(spectatorsInPaddock==totalSpectators) {
				spectatorsCheckingHorses=false;
				spectatorCheckHorses_condition.signalAll();
				//last spectator wakes up the horses
				horsesCanNotGo=false;
				horse_condition.signalAll();
			}
			
			while(spectatorsCheckingHorses) {
				try {
					spectatorCheckHorses_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(horseProbabilities.size()==0) {
				int totalP=0;
				for(int i = 0 ;i<horsesRunning.size();i++) {
					totalP+=horsePerformance.get(horsesRunning.get(i));
				}
				
				double prob;
				for(int i=0;i<horsesRunning.size();i++) {
					if(horsesRunning.size()<100) {
						prob=(double)horsePerformance.get(horsesRunning.get(i))/totalP*100;
					}else {
						prob=(double)horsePerformance.get(horsesRunning.get(i))/totalP*horsesRunning.size()*2;	
					}
					horseProbabilities.put(horsesRunning.get(i), prob);
					repo.setHorseProbabilitie(horsesRunning.get(i), prob);
				//	repo.sethorseProbabilities(horseProbabilities);
				} 	
			

				int toPut;

				for(int i=0;i<horsesRunning.size();i++) {
					if(horseProbabilities.containsKey(horsesRunning.get(i))) {
						toPut = horseProbabilities.get(horsesRunning.get(i)).intValue();
						for(int c = 0;c<toPut;c++) {
							pickHorse.add(horsesRunning.get(i));
						}
					}
				}
			}
			Random random = new Random();
			int n = random.nextInt(pickHorse.size());
			horsePicked=pickHorse.get(n); 
			
			
			spectatorsInPaddock--;
			if(spectatorsInPaddock==0) {
				spectatorsCheckingHorses=true;
				horsesRunning.clear();
				horsePerformance.clear();
				horseProbabilities.clear();
				pickHorse.clear();
			}
			
				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			mutex.unlock();
		}
		return horsePicked;
	}
	
}
