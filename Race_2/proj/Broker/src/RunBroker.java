import Interfaces.IBroker_BettingCenter;
import Interfaces.IBroker_Control;
import Interfaces.IBroker_Stable;
import Interfaces.IBroker_Track;
import sharingRegions.MonitorBettingCenter;
import sharingRegions.MonitorControlCenter;
import sharingRegions.MonitorRacingTrack;
import sharingRegions.MonitorStable;
import sharingRegions.Repository;
import stakeholders.Broker;

public class RunBroker {

	public static void main(String[] args) {
		
		
		
		MonitorBettingCenter mBettingCenter = new MonitorBettingCenter();
		MonitorControlCenter mControlCenter = new MonitorControlCenter();
		MonitorRacingTrack mRacingTrack = new MonitorRacingTrack();
		MonitorStable mStable = new MonitorStable();
		Repository repo = new Repository();
		Broker broker = new Broker((IBroker_Control) mControlCenter,(IBroker_BettingCenter) mBettingCenter,(IBroker_Stable) mStable,(IBroker_Track) mRacingTrack,repo);
		
		System.out.println("Broker is starting!");
		broker.start();
		
		try {
			broker.join();
		} catch (InterruptedException e) {
			System.out.println("Broker thread has ended.\n");
		}
	
		
	}	
	
}
