package sharingRegions;
import Interfaces.IMonitor_Track;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


import Interfaces.IRepository;
import Interfaces.Register;
import java.io.FileInputStream;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;




public class MonitorRacingTrack implements IMonitor_Track {
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
	private	HashMap<Integer,Integer> horsePosition;
	private HashMap<Integer,Integer> horsesFinalPos;
	private HashMap<Integer,Integer> horseFinalRuns;
	private HashMap<Integer,Integer> horseRuns;
	private HashMap<Integer,List<Integer>> invertedhorseFinalRuns;
	private HashMap<Integer,Boolean> horseCanRun;
	private HashMap<Integer,Integer> horsePerformance;
	private Queue<Integer> waitList;
	private List<Integer> horsesArrivalOrder;
	private List<Integer> horsesInRace;
	private int wait;
	private volatile int cycle;
	IRepository repo;

        /**
        * RMI Register host name
        */
        private String rmiRegHostName;

        /**
        * RMI Register host name
        */
        private int rmiRegPortNumb;

	public MonitorRacingTrack(int raceLength,IRepository repo) throws IOException {
		mutex = new ReentrantLock(true);
		horse_condition = mutex.newCondition();
		horseWaitingMoving_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		brokerReportResults_condition = mutex.newCondition();
		this.horsesPerRace=repo.getHorsesPerRace();
		horses_at_start_line=0;
		horsesRacing=horsesPerRace;
		this.raceLength=raceLength;
		horsesCanNotRace = true;
		horsesFinalPos = new HashMap<Integer,Integer>();
		horseFinalRuns = new HashMap<Integer,Integer>();
		horseRuns = new HashMap<Integer,Integer>();
		horsesArrivalOrder = new ArrayList<Integer>();
		invertedhorseFinalRuns = new HashMap<Integer,List<Integer>>();
		horseCanRun = new HashMap<Integer,Boolean>();
		waitList = new LinkedList<Integer>();
		wait=0;
		cycle=0;
		this.repo = repo;
		horsePerformance = new HashMap<Integer,Integer>();
		horsePosition = new HashMap<Integer,Integer>();
		horsesInRace=new ArrayList<Integer>();
	}
	/**
	*	Waits until all horses are in starting line to start the race.
	*
	*/
	@Override
	public void startTheRace() {
			mutex.lock();
			try {

				System.out.println("STARTING THE RACE");

				//to be updated the races that left after this.
			//	repo.raceDone();
		        repo.raceStarted();

				while(horses_at_start_line<horsesPerRace) {
					try {
						broker_condition.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				horsesCanNotRace=false;
				horse_condition.signalAll();

				System.out.println("EXIT startTheRace() BROKER");


			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally {
				mutex.unlock();
			}
	}

	/**
	*	Send horses to start line and then when all horses to race are at start line
	*	the broker is woken up.
	*
	*	@param horseId Horse ID
	*	@param performance Horse performance
	*/
	@Override
	public void proceedToStartLine(int horseId,int performance) {

		mutex.lock();
		try {
			horses_at_start_line++;
			System.out.println("Horse_"+horseId+" is going to startLine");
			if(horsesInRace.size()==0) {
				horseCanRun.put(horseId,false);
			}else {
				horseCanRun.put(horseId, true);
			}

			horsePerformance.put(horseId, performance);
			horsesInRace.add(horseId);
			horseRuns.put(horseId, 0);
			horsePosition.put(horseId, 0);

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
	/**
	*	Horse make a move with the value random up to his max performance
	*
	*	@param horseId Horse ID
	*/
	@Override
	public void makeAMove(int horseId) {
		mutex.lock();
		try {

			wait++;
			if(wait==horsesRacing) {
				wait--;
				horseCanRun.put(horseId,true);
				horseCanRun.put(horsesInRace.get(cycle),false);
				horseWaitingMoving_condition.signalAll();
				cycle++;
				if(cycle==horsesInRace.size()) {
					cycle=0;
				}
			}


			if(horseCanRun.get(horseId)) {
				waitList.add(horseId);
			}
			while(horseCanRun.get(horseId)) {
				try {
					horseWaitingMoving_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}


		    Random random = new Random();
		    int newPos = horsePosition.get(horseId)+random.nextInt(horsePerformance.get(horseId))+10;
		    horsePosition.put(horseId, newPos);
			repo.sethorseposition(horseId,newPos);
			horseRuns.put(horseId,horseRuns.get(horseId)+1);
			repo.sethorseruns(horseId,horseRuns.get(horseId));
			System.out.println("Horse_"+ horseId+" is on the position "+ newPos +" at run "+horseRuns.get(horseId));


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			mutex.unlock();
		}

	}
	/**
	*	Check if a horse has crossed the finish line and then
	*	if all horses already finished the broker is woken up
	* 	else continue to run.
	*
	*	@param horseId Horse ID
	*   @return boolean Returns true if the line has been crossed by the horse.
	*/
	@Override
	public boolean hasFinishLineBeenCrossed(int horseId) {
		boolean hasCross=false;
		mutex.lock();
		try {
			if(horsePosition.get(horseId) > raceLength) {
				System.out.println("Horse_"+horseId+" cross the line in position:"+horsePosition.get(horseId)+" at run "+horseRuns.get(horseId));
				horsesArrivalOrder.add(horseId);
				horsesFinalPos.put(horseId,horsePosition.get(horseId));
				int indexOfRemovedHorse=horsesInRace.indexOf(horseId);
				horsesInRace.remove(indexOfRemovedHorse);
				if(horsesInRace.size()!=0) {
					wait--;
					horseCanRun.put(horseId,true);
					if(indexOfRemovedHorse<cycle) {
						cycle=cycle-1;
					}
					horseCanRun.put(horsesInRace.get(cycle%horsesInRace.size()),false);
					horseWaitingMoving_condition.signalAll();
					cycle++;
					if(cycle>=horsesInRace.size()) {
						cycle=0;
					}

				}

		        List<Integer> valuesList = new ArrayList<Integer>(horseFinalRuns.values());
		        //verificar se existe algum cavalo na mesma run.
		        if(valuesList.contains(horseRuns.get(horseId))) {
		        	horseFinalRuns.put(horseId,horseRuns.get(horseId));
		        	List<Integer> horsesInTheSameRun = invertedhorseFinalRuns.get(horseRuns.get(horseId));
		        	horsesInTheSameRun.add(horseId);
		        	invertedhorseFinalRuns.put(horseId,horsesInTheSameRun);
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
		        	horseFinalRuns.put(horseId,horseRuns.get(horseId));
		        	List<Integer> horsesInTheSameRun = new ArrayList<Integer>();
		        	horsesInTheSameRun.add(horseId);
		        	invertedhorseFinalRuns.put(horseRuns.get(horseId),horsesInTheSameRun);
		        }

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

	/**
	*	Report the results to spectators returning an array with rankings
	*
	*	@return int[] horseAWinners
	*/
	@Override
	public int[] reportResults() {
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
			horsesRacing=horsesPerRace;
		} finally {
			mutex.unlock();
		}
		for(int i = 0;i<horsesArrivalOrder.size();i++) {
			System.out.println("horsesarrival:"+horsesArrivalOrder.get(i));
			//System.out.println(horsesArrivalOrder.get(i)+" "+(i+1));
			try {
				repo.sethorserank(horsesArrivalOrder.get(i),i+1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int[] horseAWinners = new int[1];
		horseAWinners[0]=horsesArrivalOrder.get(0);


		int i=1;
		while(i<horsesArrivalOrder.size() &&  horseFinalRuns.get(horsesArrivalOrder.get(0))==horseFinalRuns.get(horsesArrivalOrder.get(i)) && horsesFinalPos.get(horsesArrivalOrder.get(0))==horsesFinalPos.get(horsesArrivalOrder.get(i)) ) {
			Arrays.copyOf(horseAWinners,horseAWinners.length+1);
			horseAWinners[horseAWinners.length-1]=horsesArrivalOrder.get(i);
			i++;
		}


		horsesArrivalOrder.clear();
		invertedhorseFinalRuns.clear();
		horseFinalRuns.clear();
		horsesFinalPos.clear();
        horsePosition.clear();
        horseRuns.clear();
		//repo.clearhorserank();

        return horseAWinners;
	}

	@Override
	public void signalShutdown() throws RemoteException, IOException {
			Register reg = null;
			Registry registry = null;

			Properties prop = new Properties();
			String propFileName = "config.properties";

			prop.load(new FileInputStream("resources/"+propFileName));

			String rmiRegHostName = prop.getProperty("10machine_Registry");
			int rmiRegPortNumb = Integer.parseInt(prop.getProperty("rmiRegPortNumb"));

			try {
					registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
			} catch (RemoteException ex) {
					System.out.println("Erro ao localizar o registo");
					ex.printStackTrace();
					System.exit(1);
			}

			String nameEntryBase = prop.getProperty("nameEntry");
			String nameEntryObject = "stubRacingTrack";


			try {
					reg = (Register) registry.lookup(nameEntryBase);
			} catch (RemoteException e) {
					System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
					e.printStackTrace();
					System.exit(1);
			} catch (NotBoundException e) {
					System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
					e.printStackTrace();
					System.exit(1);
			}
			try {
					// Unregister ourself
					reg.unbind(nameEntryObject);
			} catch (RemoteException e) {
					System.out.println("stubControl Registration exception: " + e.getMessage());
					e.printStackTrace();
					System.exit(1);
			} catch (NotBoundException e) {
					System.out.println("stubControl Not bound exception: " + e.getMessage());
					e.printStackTrace();
					System.exit(1);
			}

			try {
					// Unexport; this will also remove us from the RMI runtime
					UnicastRemoteObject.unexportObject(this, true);
			} catch (NoSuchObjectException ex) {
					ex.printStackTrace();
					System.exit(1);
			}

			System.out.println("Control Center closed.");
	}





}
