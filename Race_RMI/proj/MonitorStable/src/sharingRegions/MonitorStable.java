package sharingRegions;


import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


import Interfaces.IMonitor_Stable;
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
        
        /**
        * RMI Register host name
        */
        private String rmiRegHostName;

        /**
        * RMI Register host name
        */
        private int rmiRegPortNumb;
	
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
       
    @Override
    public void signalShutdown() throws RemoteException, IOException {
        Register reg = null;
        Registry registry = null;

        String rmiRegHostName;
        int rmiRegPortNumb;

        Properties prop = new Properties();
        String propFileName = "config.properties";

        prop.load(new FileInputStream("resources/"+propFileName));

        rmiRegHostName = this.rmiRegHostName;
        rmiRegPortNumb = this.rmiRegPortNumb;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            System.out.println("Erro ao localizar o registo");
            ex.printStackTrace();
            System.exit(1);
        }

        String nameEntryBase = prop.getProperty("nameEntry");
        String nameEntryObject = prop.getProperty("machine_BettingCenter");


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
            System.out.println("Stable registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Stable not bound exception: " + e.getMessage());
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

        System.out.println("Stable closed.");
    }


}
