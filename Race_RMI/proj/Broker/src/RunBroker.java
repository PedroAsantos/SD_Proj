import Interfaces.IBroker_BettingCenter;
import Interfaces.IBroker_Control;
import Interfaces.IBroker_Paddock;
import Interfaces.IBroker_Stable;
import Interfaces.IBroker_Track;
import Interfaces.IRepository;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
            
            try {
		prop.load(new FileInputStream("src/resources/"+propFileName));
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
                Registry registryStable = LocateRegistry.getRegistry(rmiRegHostName,rmiRegPortNumb);
             
                IBroker_Stable mStable = (IBroker_Stable) registryStable.lookup("stubStable");
                /*IBroker_Control mControlCenter = (IBroker_Control) registryControl.lookup("stubControlBroker");
                IBroker_Paddock mPaddock = (IBroker_Paddock) registryPaddock.lookup("stubPaddockBroker");
                IBroker_BettingCenter mBettingCenter = (IBroker_BettingCenter) registryRacingTrack.lookup("stubBettingCenterBroker");
                IBroker_Track mRacingTrack = (IBroker_Track) registryBettingCenter.lookup("stubRacingTrackBroker");
               */
                IBroker_Control mControlCenter = null;
                IBroker_Paddock mPaddock = null;
                IBroker_BettingCenter mBettingCenter = null;
                IBroker_Track mRacingTrack = null;
                
                IRepository repo =(IRepository) registryStable.lookup("stubRepository");
                
                Broker broker = new Broker(mControlCenter,mBettingCenter,mStable,mRacingTrack,mPaddock ,repo);
		
                System.out.println("Broker is starting!");
                broker.start();
               
                broker.join();
                //exit to therad
        
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
   	
		
		
		
	
	
		
	}	
	
}
