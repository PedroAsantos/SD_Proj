package src;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import stakeholders.Horse;



public class MonitorRacingTrack implements IHorse_Track, IBroker_Track{
	private final ReentrantLock mutex;
	private final Condition horse_condition;
	private final Condition broker_condition;
	private final Condition horseWaitingMoving_condition;
	private final Condition brokerReportResults_condition;
	private final int horsesPerRace;
	
	private final int raceLength;
	private boolean horsesCanNotMove;
	private int horsesRacing;
	private int horses_at_start_line;
	private boolean horsesCanNotRace;
	private HashMap<Integer,Integer> horsesFinalPos; 
	private HashMap<Integer,Integer> horseRuns;
	//private int iter;
	Repository repo;
	

	public MonitorRacingTrack(int horsesPerRace, int raceLength,Repository repo) {
		mutex = new ReentrantLock(true);
		horse_condition = mutex.newCondition();
		horseWaitingMoving_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		brokerReportResults_condition = mutex.newCondition();
		this.horsesPerRace=horsesPerRace;
		horses_at_start_line=0;
		horsesRacing=horsesPerRace;
		this.raceLength=raceLength;
		horsesCanNotRace = true;
		horsesCanNotMove = true;
		horsesFinalPos = new HashMap<Integer,Integer>();
		horseRuns = new HashMap<Integer,Integer>();
	//	iter=0;
		this.repo = repo;	
	}
	
	@Override
	public void startTheRace() {
			mutex.lock();
			try {
				
				System.out.println("STARTING THE RACE");
				
				while(horses_at_start_line<horsesPerRace) {
					try {
						broker_condition.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				horsesCanNotRace=false;
				horse_condition.signalAll();
								
				
				
			}finally {
				mutex.unlock();
			}
	}
	
	@Override
	public void proceedToStartLine(Horse horse) {
		//use only id???
	
		mutex.lock();
		try {
			horses_at_start_line++;
			System.out.println("Horse_"+horse.getID()+" is going to startLine");
			if(horses_at_start_line == horsesPerRace) {
				broker_condition.signal();
			}
			
			try {
				 while(horsesCanNotRace) {
					 horse_condition.await();
				 }
			}catch(Exception e) {
				System.out.println("Horse ProceedToStarLine "+e);
			}
			horses_at_start_line--;
			if(horses_at_start_line==0) {
				horsesCanNotRace=true;
			}
		}finally {
			mutex.unlock();
		}
	
		
		
	
		
	}
	
	@Override
	public void makeAMove(Horse horse) {
		// TODO Auto-generated method stub
		mutex.lock();
		try {
			
			int performance = horse.getPerformance();
		    Random random = new Random();
			horse.moveofPosition(random.nextInt(performance)+10);
			horse.incrementRuns();
			System.out.println("Horse_"+ horse.getID()+" is on the position "+ horse.getPosition());
			repo.sethorseposition(horse.getID(),horse.getPosition());
		//	iter++; 
			horsesCanNotMove=false;
			horseWaitingMoving_condition.signal();
			
		
		//	movedHorses++;
		/*	if(movedHorses==horsesPerRace) {
				horsesAreMoving=false;
				horseWaitingMoving_condition.signalAll();
			}
			while(horsesAreMoving) {
				try {
					horseWaitingMoving_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//movedHorses--;
			if(movedHorses==0) {
				horsesAreMoving=true;
			}*/
			
		} finally {
			mutex.unlock();
		}
		
	}

	@Override
	public boolean hasFinishLineBeenCrossed(Horse horse) {
		boolean hasCross=false;
		mutex.lock();
		try {
			
			if(horse.getPosition() > raceLength) {
				System.out.println("Horse_"+horse.getID()+" cross the line in position:"+horse.getPosition());
			
				horsesFinalPos.put(horse.getID(),horse.getPosition());
				horseRuns.put(horse.getID(),horse.getRuns());
		
				horse.resetHorse();
				horsesRacing--;
				if(horsesRacing==0) {
					brokerReportResults_condition.signal();
					//reset horsesRacing?
				}
				hasCross=true;
			}else {
				while(horsesCanNotMove) {
					try {
						//System.out.println(iter / horsesPerRace);
						//assim, nao pode acontecer o mesmo cavalo correr sem ser a vez dele?
						horseWaitingMoving_condition.await();
					/*	if(iter / horsesPerRace == horse.getRuns()) {
							horseWaitingMoving_condition.await();
						}*/
						//-> 
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				horsesCanNotMove=true;
				//System.out.println("Horse_"+horse.getID()+" is started to move! ");
					
					
			}
		
			
		
				
		} finally {
			mutex.unlock();
		}
		
		return hasCross;
	}

	@Override
	public List<Integer> reportResults() {
		// TODO Auto-generated method stub
	
		mutex.lock();
		try {
			while(horsesRacing>0) {
				try {
					System.out.println("Broker Waiting for the end of the race!");
					brokerReportResults_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			repo.raceDone();
			//repor o valor para a proxima corrida;
			horsesRacing=horsesPerRace;
			
		} finally {
			mutex.unlock();
		}
		List<Integer> horsesRunning = repo.getHorsesRunning();
		int min = horseRuns.get(horsesRunning.get(0));
		//1.element id horse
		//2.number de runs
		List<int[]> bestHorses = new ArrayList<int[]>(horsesPerRace);
		List<Integer> bestofTheBests= new ArrayList<Integer>(horsesPerRace);
		bestofTheBests.clear();
		for(int i=0; i < horsesRunning.size();i++) {
			int[] horseArray = new int[2];
			if(horseRuns.get(horsesRunning.get(i))<min){
				bestHorses.clear();
				horseArray[0]=horsesRunning.get(i);
				horseArray[1] = horseRuns.get(horsesRunning.get(i));
				min = horseRuns.get(horsesRunning.get(i));
				bestHorses.add(horseArray);
				
			}else if(horseRuns.get(horsesRunning.get(i))==min) {
				horseArray[0]=horsesRunning.get(i);
				horseArray[1] = horseRuns.get(horsesRunning.get(i));
				min = horseRuns.get(horsesRunning.get(i));
				bestHorses.add(horseArray);
			}
		}
		int biggestPos=0;
		if(bestHorses.size()==1) {
			horseRuns.clear();
			horsesFinalPos.clear();
			//System.out.println(bestHorses.get(0)[0]);
			bestofTheBests.add(bestHorses.get(0)[0]);
			return bestofTheBests;
		}else {
			biggestPos = horsesFinalPos.get(bestHorses.get(0)[0]);
		
			for(int i=0;i<bestHorses.size();i++) {
				if(biggestPos < horsesFinalPos.get(bestHorses.get(i)[0])) {
					bestofTheBests.clear();
					bestofTheBests.add(bestHorses.get(i)[0]);
					
					biggestPos = horsesFinalPos.get(bestHorses.get(i)[0]);
					
				}else if(biggestPos ==  horsesFinalPos.get(bestHorses.get(i)[0])) {
					bestofTheBests.add(bestHorses.get(i)[0]);
				}
			}
			
		}
		horseRuns.clear();
		horsesFinalPos.clear();
		return bestofTheBests; 
		
	}


	
}
