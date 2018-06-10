package sharingRegions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.IMonitor_Control;
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

//import com.sun.corba.se.pept.broker.Broker;

public class MonitorControlCenter implements IMonitor_Control {
	private final ReentrantLock mutex;
	private final Condition spectator_condition;
	private final Condition broker_condidition;

	private int spectatorsWatchingRace;
	private boolean raceIsOn;
	private boolean eventNotEnd;
	private int spectatorsRelaxing;
	private int totalSpectators;
	private List<Integer> winners;
	IRepository repo;

        /**
        * RMI Register host name
        */
        private String rmiRegHostName;

        /**
        * RMI Register host name
        */
        private int rmiRegPortNumb;

	public MonitorControlCenter(IRepository repo) {
		mutex = new ReentrantLock(true);
		spectator_condition = mutex.newCondition();
		broker_condidition = mutex.newCondition();
		raceIsOn=true;
		eventNotEnd=true;
		this.repo=repo;
		spectatorsWatchingRace=0;
		try {
			this.totalSpectators=repo.getNumberOfSpectators();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		spectatorsRelaxing=0;
	}

	/**
	*	Function for spectators to be able to watch the race. In the end of the race they are waken up.
	*
	*	@param spectator_id Spectator ID
	*/
	@Override
	public void goWatchTheRace(int spectator_id) {

		mutex.lock();
		try {

			System.out.println("Spectator_"+spectator_id+" is watching the race!");
			while(raceIsOn) {
				try {
					spectator_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			spectatorsWatchingRace++;
			System.out.println("Spectator_"+spectator_id+" exiting of the race");
		//	spectatorsWatchingRace--;
			if(spectatorsWatchingRace==totalSpectators) {
				raceIsOn=true;
				spectatorsWatchingRace=0;
			}

		}finally {
			mutex.unlock();
		}

	}
	/**
	*	Function for spectators know if they won the bet.
	*
	*	@param horsePicked horsePicked ID
	*	@return boolean Returns true if the spectator won the bet.
	*/
	@Override
	public boolean haveIwon(int spectator_id, int horsePicked) {
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



			for(int i = 0;i<winners.size();i++) {
				if(winners.get(i)==horsePicked) {
					iWon=true;
				}

			}

			System.out.println("Spectator_"+spectator_id+" won: "+iWon);
		} finally {
			mutex.unlock();
		}

		return iWon;
	}
	/**
	*	Function for spectators know if there are more races.
	*
	*	@return boolean Returns true if there are no more Races.
	*/
	@Override
	public boolean noMoreRaces() {
		int numberOfRaces=0;
		mutex.lock();
		try {
			numberOfRaces = repo.getNumberOfRacesMissing();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			mutex.unlock();
		}
		if(numberOfRaces<=0) {
			return true;
		}else {
			return false;
		}
	}
	/**
	*	Function for broker report the horses that won the last race.
	*
	*	@param horseAWinners Array with the horses that won the last race.
	*/
	@Override
	public void reportResults(int[] horseAWinners) {
		mutex.lock();
		try {
			winners = new ArrayList<Integer>();
			for(int i=0;i<horseAWinners.length;i++) {
				winners.add(horseAWinners[i]);
			}
			raceIsOn=false;
			System.out.println("Reporting Result to Spectators");
			spectator_condition.signalAll();
		} finally {
			mutex.unlock();
		}



	}
	/**
	*	Function for spectators relax at the end of the race.
	*
	*	@param spectator_id Spectator ID.
	*/
	@Override
	public void relaxABit(int spectator_id) {
		mutex.lock();
		try {
			spectatorsRelaxing++;
			if(spectatorsRelaxing==totalSpectators) {
				broker_condidition.signal();
			}
			System.out.println("Spectator_"+spectator_id+" is relaxing!");
			while(eventNotEnd){
				try {
					spectator_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			spectatorsRelaxing--;
			if(spectatorsRelaxing==0) {
				eventNotEnd=true;
			}

		}finally {
			mutex.unlock();
		}
	}
	/**
	*	Function for broker wait for spectators.
	*
	*
	*/
	@Override
	public void entertainTheGuests() {
		mutex.lock();
		try {
			System.out.println("BROKER ENTERTAIN THE GUESTS");
			while(spectatorsRelaxing<totalSpectators){
				try {
					broker_condidition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			eventNotEnd=false;
			spectator_condition.signalAll();

		}finally {
			mutex.unlock();
		}
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
            String nameEntryObject = "stubControl";


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
