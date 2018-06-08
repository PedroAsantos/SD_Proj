import Interfaces.IMonitor_BettingCenter;
import Interfaces.IMonitor_Control;
import Interfaces.IMonitor_Paddock;
import Interfaces.IRepository;
import Interfaces.ISpectator_BettingCenter;
import Interfaces.ISpectator_Control;
import Interfaces.ISpectator_Paddock;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.Random;
import sharingRegions.MonitorBettingCenter;
import sharingRegions.MonitorControlCenter;
import sharingRegions.MonitorPaddock;
import sharingRegions.Repository;
import stakeholders.Spectator;

public class RunSpectators {
	public static void main(String[] args) {
		
		int numberOfSpectators=4;

		Spectator[] spectators = new Spectator[numberOfSpectators]; 
		
                 String hostName; // nome da maquina onde esta o servidor

                Properties prop = new Properties();
                String propFileName = "config.properties";

                IRepository repo = null;
                
                try {
                    prop.load(new FileInputStream("resources/"+propFileName));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                String rmiRegHostName = prop.getProperty("rmiRegHostName"); 
                int rmiRegPortNumb = Integer.parseInt(prop.getProperty("rmiRegPortNumb")); 	


                try {
                    Registry registry = LocateRegistry.getRegistry(rmiRegHostName,rmiRegPortNumb);

                    IMonitor_Control mControlCenter = (IMonitor_Control) registry.lookup("stubControl");
                    IMonitor_Paddock mPaddock = (IMonitor_Paddock) registry.lookup("stubPaddock");
                    IMonitor_BettingCenter mBettingCenter = (IMonitor_BettingCenter) registry.lookup("stubBettingCenter"); 

                    repo =(IRepository) registry.lookup("stubRepository");

                    int money;
                    for (int i = 0; i < spectators.length; i++) {
                        money = 1000;
                        spectators[i] = new Spectator(i, money, mBettingCenter, mControlCenter, mPaddock, repo);
                    }

                    /* start of the simulation */
                    for (int i = 0; i < spectators.length; i++) {
                        System.out.println("Spectator_" + i + " is starting!");
                        spectators[i].start();
                    }

                    /* wait for the end of the simulation */
                    for (int i = 0; i < spectators.length; i++) {
                        try {
                            spectators[i].join();
                        } catch (InterruptedException e) {
                            System.out.println("Spectator thread " + i + " has ended.\n");
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

