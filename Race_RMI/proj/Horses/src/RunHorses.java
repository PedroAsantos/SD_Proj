import java.util.Random;


import Interfaces.IHorse_Paddock;
import Interfaces.IHorse_Stable;
import Interfaces.IHorse_Track;
import Interfaces.IMonitor_Paddock;
import Interfaces.IMonitor_Stable;
import Interfaces.IMonitor_Track;
import Interfaces.IRepository;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import stakeholders.Horse;

public class RunHorses {

	public static void main(String[] args) throws RemoteException{
		int numberOfHorses = 20; //testar com numeros maiores.
		int maxPerformance=10;

                IRepository repo = null;

		Horse[] horses = new Horse[numberOfHorses];

                String hostName; // nome da maquina onde esta o servidor

                Properties prop = new Properties();
                String propFileName = "config.properties";

                try {
                    prop.load(new FileInputStream("resources/"+propFileName));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                String rmiRegHostName = prop.getProperty("10machine_Registry");
                int rmiRegPortNumb = Integer.parseInt(prop.getProperty("rmiRegPortNumb"));


                try {
                    Registry registry = LocateRegistry.getRegistry(rmiRegHostName,rmiRegPortNumb);

                    IMonitor_Stable mStable = (IMonitor_Stable) registry.lookup("stubStable");
                    IMonitor_Paddock mPaddock = (IMonitor_Paddock) registry.lookup("stubPaddock");
                    IMonitor_Track mRacingTrack = (IMonitor_Track) registry.lookup("stubRacingTrack");


                    repo =(IRepository) registry.lookup("stubRepository");

                    for (int i = 0; i < horses.length; i++) {
												Random random = new Random();
												int performace= random.nextInt(maxPerformance)+1;
												horses[i] = new Horse(i,performace,mRacingTrack, mStable, mPaddock, repo);
                    }

                    /* start of the simulation */
                    for (int i = 0; i < horses.length; i++) {
                        System.out.println("Horse_" + i + "is starting!");
                        horses[i].start();
                    }

                    /* wait for the end of the simulation */
                    for (int i = 0; i < horses.length; i++) {
                        try {
                            horses[i].join();
                        } catch (InterruptedException e) {
                            System.out.println("Horse thread " + i + " has ended.\n");
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Client exception: " + e.toString());
                    e.printStackTrace();
                }

		try {
                    repo.finished();
                } catch (RemoteException ex) {
                    System.out.println("Error closing all!");
                    ex.printStackTrace();
                    System.exit(1);
                }


	}

}
