package sharingRegions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.IHorse_Paddock;
import Interfaces.IMonitor_Paddock;
import Interfaces.IRepository;
import Interfaces.ISpectator_Paddock;
import Interfaces.Register;
import java.io.FileInputStream;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;


public class MonitorPaddock implements IMonitor_Paddock {
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
	IRepository repo;

        /**
        * RMI Register host name
        */
        private String rmiRegHostName;

        /**
        * RMI Register host name
        */
        private int rmiRegPortNumb;


	public MonitorPaddock(IRepository repo) throws IOException {
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

            @Override
        public void signalShutdown() throws RemoteException, IOException {
            Register reg = null;
            Registry registry = null;

            Properties prop = new Properties();
            String propFileName = "config.properties";

            prop.load(new FileInputStream("resources/"+propFileName));

						String rmiRegHostName = prop.getProperty("rmiRegHostName");
						int rmiRegPortNumb = Integer.parseInt(prop.getProperty("rmiRegPortNumb"));

            try {
                registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            } catch (RemoteException ex) {
                System.out.println("Erro ao localizar o registo");
                ex.printStackTrace();
                System.exit(1);
            }

            String nameEntryBase = prop.getProperty("nameEntry");
            String nameEntryObject = "stubPaddock";


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

            System.out.println("Paddock closed.");
        }

}
