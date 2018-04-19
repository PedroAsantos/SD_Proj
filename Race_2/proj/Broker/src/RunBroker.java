import Interfaces.IBroker_BettingCenter;
import Interfaces.IBroker_Control;
import Interfaces.IBroker_Stable;
import Interfaces.IBroker_Track;
import sharingRegions.MonitorBettingCenter;
import sharingRegions.MonitorControlCenter;
import sharingRegions.MonitorPaddock;
import sharingRegions.MonitorRacingTrack;
import sharingRegions.MonitorStable;
import sharingRegions.Repository;
import stakeholders.Broker;

public class RunBroker {

	public static void main(String[] args) {
		
		int numberOfHorses = 20; //testar com numeros maiores.
		int numberOfSpectators=4;
		int numberOfRaces=5;
		int horsesPerRace=4;
		int raceLength=30;
		int maxPerformance=10;
		Repository repo = new Repository(numberOfHorses,numberOfSpectators,numberOfRaces,horsesPerRace,raceLength);

		MonitorBettingCenter mBettingCenter = new MonitorBettingCenter(repo);
		MonitorControlCenter mControlCenter = new MonitorControlCenter(repo);
		MonitorPaddock mPaddock = new MonitorPaddock(repo);
		MonitorRacingTrack mRacingTrack = new MonitorRacingTrack(raceLength, repo);
		MonitorStable mStable = new MonitorStable(repo);
		Broker broker = new Broker((IBroker_Control) mControlCenter,(IBroker_BettingCenter) mBettingCenter,(IBroker_Stable) mStable,(IBroker_Track) mRacingTrack, repo);
		
		broker.start();
		
		try {
			broker.join();
		} catch (InterruptedException e) {
			System.out.println("Broker thread has ended.\n");
		}
		
		
	}	
	
}
