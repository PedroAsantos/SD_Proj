import Interfaces.IBroker_BettingCenter;
import Interfaces.IBroker_Control;
import Interfaces.IBroker_Paddock;
import Interfaces.IBroker_Stable;
import Interfaces.IBroker_Track;
import Interfaces.IMonitor_BettingCenter;
import Interfaces.IMonitor_Control;
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
import sharingRegions.MonitorBettingCenter;
import sharingRegions.MonitorControlCenter;
import sharingRegions.MonitorPaddock;
import sharingRegions.MonitorRacingTrack;
import sharingRegions.MonitorStable;
import sharingRegions.Repository;
import stakeholders.Broker;

public class RunBroker {

	public static void main(String[] args) {
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
	
            
            String rmiRegHostName = prop.getProperty("10machine_Registry"); 
            int rmiRegPortNumb = Integer.parseInt(prop.getProperty("rmiRegPortNumb")); 	
            
          
            try {
                Registry registry = LocateRegistry.getRegistry(rmiRegHostName,rmiRegPortNumb);
             
                IMonitor_Stable mStable = (IMonitor_Stable) registry.lookup("stubStable");
                IMonitor_Control mControlCenter = (IMonitor_Control) registry.lookup("stubControl");
                IMonitor_Paddock mPaddock = (IMonitor_Paddock) registry.lookup("stubPaddock");
                IMonitor_BettingCenter mBettingCenter = (IMonitor_BettingCenter) registry.lookup("stubBettingCenter");
                IMonitor_Track mRacingTrack = (IMonitor_Track) registry.lookup("stubRacingTrack");
               
         
                
                repo =(IRepository) registry.lookup("stubRepository");
                
                Broker broker = new Broker(mControlCenter,mBettingCenter,mStable,mRacingTrack,mPaddock,repo);
		
                System.out.println("Broker is starting!");
                broker.start();
               
                broker.join();
                //exit to therad
        
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
