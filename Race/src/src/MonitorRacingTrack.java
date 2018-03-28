package src;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
	private int horsesRacing;
	private int horses_at_start_line;
	private boolean horsesCanNotRace;
	private HashMap<Integer,Integer> horsesFinalPos; 
	private HashMap<Integer,Integer> horseRuns;
	private HashMap<Integer,List<Integer>> invertedHorseRuns;
	private HashMap<Integer,Boolean> horseCanRun;
	private Queue<Integer> waitList;
	private List<Integer> horsesArrivalOrder;
	private List<Integer> horsesInRace;
	private int wait;
	private volatile int cycle;
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
		horsesFinalPos = new HashMap<Integer,Integer>();
		horseRuns = new HashMap<Integer,Integer>();
		horsesArrivalOrder = new ArrayList<Integer>();
		invertedHorseRuns = new HashMap<Integer,List<Integer>>();
		horseCanRun = new HashMap<Integer,Boolean>();
		waitList = new LinkedList<Integer>();
		wait=0;
		cycle=0;
		this.repo = repo;	
	}
	
	@Override
	public void startTheRace() {
			mutex.lock();
			try {
				
				System.out.println("STARTING THE RACE");
				horsesInRace=new ArrayList<Integer>(repo.getHorsesRunning());
				horseCanRun.put(horsesInRace.get(0),false);
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
			horseCanRun.put(horse.getID(), true);
			
		
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

			wait++;
			if(wait==horsesRacing) {
				wait--;
				horseCanRun.put(horse.getID(),true);
				horseCanRun.put(horsesInRace.get(cycle),false);
				horseWaitingMoving_condition.signalAll();
				cycle++;
				if(cycle==horsesInRace.size()) {
					cycle=0;
				}
			//	System.out.println("1cycle: "+cycle+" call: "+horsesInRace.get(cycle));
			}
						
			
			if(horseCanRun.get(horse.getID())) {
				waitList.add(horse.getID());
			}
			while(horseCanRun.get(horse.getID())) {
				try {
					horseWaitingMoving_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			int performance = horse.getPerformance();
		    Random random = new Random();
			horse.moveofPosition(random.nextInt(performance)+10);
			horse.incrementRuns();
			System.out.println("Horse_"+ horse.getID()+" is on the position "+ horse.getPosition()+" at run "+horse.getRuns());
			repo.sethorseposition(horse.getID(),horse.getPosition());
		
			
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
				System.out.println("Horse_"+horse.getID()+" cross the line in position:"+horse.getPosition()+" at run "+horse.getRuns());
				horsesArrivalOrder.add(horse.getID());
				horsesFinalPos.put(horse.getID(),horse.getPosition());
				int indexOfRemovedHorse=horsesInRace.indexOf(horse.getID());
				horsesInRace.remove(indexOfRemovedHorse);
				if(horsesInRace.size()!=0) {
					/*for(int i=0;i<horsesInRace.size();i++) {
						System.out.println("horseInRace0:"+horsesInRace.get(i));
					}*/
					wait--;
					horseCanRun.put(horse.getID(),true);
					if(indexOfRemovedHorse<cycle) {
						cycle=cycle-1;
					}
					horseCanRun.put(horsesInRace.get(cycle%horsesInRace.size()),false);
					horseWaitingMoving_condition.signalAll();
					cycle++;	
					if(cycle>=horsesInRace.size()) {
						cycle=0;
					}
				//	System.out.println("cycle: "+cycle+" call: "+horsesInRace.get(cycle));

				}
								
		        List<Integer> valuesList = new ArrayList<Integer>(horseRuns.values());
		        //verificar se existe algum cavalo na mesma run.
		        if(valuesList.contains(horse.getRuns())) {
		        	horseRuns.put(horse.getID(),horse.getRuns());
		        	List<Integer> horsesInTheSameRun = invertedHorseRuns.get(horse.getRuns());
		        	horsesInTheSameRun.add(horse.getID());
		        	invertedHorseRuns.put(horse.getRuns(),horsesInTheSameRun);	
		        	horsesArrivalOrder.removeAll(horsesInTheSameRun);
		        	//int max = horsesInTheSameRun.get(0);
 		        	for(int i = 0;i<horsesInTheSameRun.size();i++) {
 		        		 int max = horsesFinalPos.get(horsesInTheSameRun.get(i));
 		        		 for(int i2=i+1;i2<horsesInTheSameRun.size();i2++) {
 		        			 if(max<horsesFinalPos.get(horsesInTheSameRun.get(i2))) {
 		        				  int t = horsesInTheSameRun.get(i);
 		        				  horsesInTheSameRun.set(i, horsesInTheSameRun.get(i2));
 		        				  horsesInTheSameRun.set(i2, t);
 		        				  max = horsesFinalPos.get(horsesInTheSameRun.get(i2));
 		        			 }
 		        		 }
 		        		//posOrder.add(max);
		        	}	        	
		        	
		        	horsesArrivalOrder.addAll(horsesInTheSameRun);
		        	
		        }else {
		        	horseRuns.put(horse.getID(),horse.getRuns());
		        	List<Integer> horsesInTheSameRun = new ArrayList<Integer>();
		        	horsesInTheSameRun.add(horse.getID());
		        	invertedHorseRuns.put(horse.getRuns(),horsesInTheSameRun);	
		        }
		       
		        	
				horse.resetHorse();
				horsesRacing--;
				if(horsesRacing==0) {
					cycle=0;
					wait=0;
					horseCanRun.clear();
					brokerReportResults_condition.signal();
					//reset horsesRacing?
				}
				hasCross=true;
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
		for(int i = 0;i<horsesArrivalOrder.size();i++) {
			System.out.println("horsesarrival:"+horsesArrivalOrder.get(i));
			repo.sethorserank(horsesArrivalOrder.get(i),i+1);
		}
		List<Integer> horseWinners = new ArrayList<Integer>();
		horseWinners.add(horsesArrivalOrder.get(0));
		int i=1;
		while(i<horsesArrivalOrder.size() &&  horseRuns.get(horsesArrivalOrder.get(0))==horseRuns.get(horsesArrivalOrder.get(i)) && horsesFinalPos.get(horsesArrivalOrder.get(0))==horsesFinalPos.get(horsesArrivalOrder.get(i)) ) {
			horseWinners.add(horsesArrivalOrder.get(i));
			i++;
		}
		
		
		horsesArrivalOrder.clear();
		invertedHorseRuns.clear();
		horseRuns.clear();
		horsesFinalPos.clear();
		return horseWinners;
		
	}


	
}
