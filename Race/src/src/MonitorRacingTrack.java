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
	private final int totalHorses;
	
	private final int raceLength;
	private boolean horsesCanNotMove;
	private int horsesRacing;
	private int horses_at_start_line;
	private boolean horsesCanNotRace;
	private int[] horsesFinalPos; 
	private int[] horseRuns;
	private int iter;
	
	public MonitorRacingTrack(int totalHorses) {
		mutex = new ReentrantLock(true);
		horse_condition = mutex.newCondition();
		horseWaitingMoving_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		brokerReportResults_condition = mutex.newCondition();
		this.totalHorses=totalHorses;
		horses_at_start_line=0;
		horsesRacing=totalHorses;
		raceLength=30;
		horsesCanNotRace = true;
		horsesCanNotMove = true;
		horsesFinalPos = new int[totalHorses];
		horseRuns = new int[totalHorses];
		iter=0;
		
	}
	
	@Override
	public void startTheRace() {
			mutex.lock();
			try {
				
				System.out.println("STARTING THE RACE");
				
				while(horses_at_start_line<totalHorses) {
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
			if(horses_at_start_line == totalHorses) {
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
			horse.moveofPosition(random.nextInt(performance));
			horse.incrementRuns();
			System.out.println("Horse_"+ horse.getID()+" is "+ horse.getPosition());
			iter++; 
			horsesCanNotMove=false;
			horseWaitingMoving_condition.signal();
			
		
		//	movedHorses++;
		/*	if(movedHorses==totalHorses) {
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
			
				horsesFinalPos[horse.getID()]=horse.getPosition();
				horseRuns[horse.getID()]=horse.getRuns();
				horsesRacing--;
				if(horsesRacing==0) {
					System.out.println(horse.getID());
					brokerReportResults_condition.signal();
					//reset horsesRacing?
				}
				hasCross=true;
			}else {
				while(horsesCanNotMove) {
					try {
						System.out.println(iter / totalHorses);
						//assim, não pode acontecer o mesmo cavalo correr sem ser a vez dele?
						horseWaitingMoving_condition.await();
					/*	if(iter / totalHorses == horse.getRuns()) {
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
					System.out.println("Broker sleeping");
					brokerReportResults_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		} finally {
			mutex.unlock();
		}
		int min = horseRuns[0];
		//1.element id horse
		//2.number de runs
		int[] horseArray = new int[2];
		List<int[]> bestHorses = new ArrayList<int[]>(totalHorses);
		List<Integer> bestofTheBests= new ArrayList<Integer>(totalHorses);
		for(int i=1; i < horseRuns.length;i++) {
			if(horseRuns[i]<min){
				bestHorses.clear();
				horseArray[0]=i;
				horseArray[1] = horseRuns[i];
				min = horseRuns[i];
				bestHorses.add(horseArray);
				
			}else if(horseRuns[i]==min) {
				horseArray[0]=i;
				horseArray[1] = horseRuns[i];
				min = horseRuns[i];
				bestHorses.add(horseArray);
			}
		}
		int biggestPos=0;
		if(bestHorses.size()==1) {
			bestofTheBests.add(bestHorses.get(0)[0]);
			return bestofTheBests;
		}else {
			biggestPos = horsesFinalPos[bestHorses.get(0)[0]];
		
			for(int i=1;i<bestHorses.size();i++) {
				if(biggestPos < horsesFinalPos[bestHorses.get(i)[0]]) {
					bestofTheBests.clear();
					bestofTheBests.add(bestHorses.get(i)[0]);
					
					biggestPos = horsesFinalPos[bestHorses.get(i)[0]];
					
				}else if(biggestPos ==  horsesFinalPos[bestHorses.get(i)[0]] ) {
					bestofTheBests.add(bestHorses.get(i)[0]);
				}
			}
			
		}
		return bestofTheBests; 
		
	}


	
}
