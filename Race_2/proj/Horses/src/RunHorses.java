import java.util.Random;


import Interfaces.IHorse_Paddock;
import Interfaces.IHorse_Stable;
import Interfaces.IHorse_Track;
import sharingRegions.MonitorBettingCenter;
import sharingRegions.MonitorControlCenter;
import sharingRegions.MonitorPaddock;
import sharingRegions.MonitorRacingTrack;
import sharingRegions.MonitorStable;
import sharingRegions.Repository;
import stakeholders.Horse;

public class RunHorses {

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
		
		Horse[] horses = new Horse[numberOfHorses];
		
		for (int i = 0; i < horses.length; i++) {
			Random random = new Random();
			int performace= random.nextInt(maxPerformance)+1;
			horses[i] = new Horse(i,performace,(IHorse_Track) mRacingTrack,(IHorse_Stable) mStable,(IHorse_Paddock) mPaddock, repo);
		}
		
		 /* start of the simulation */
		
	
		for (int i = 0; i < horses.length; i++) {
			horses[i].start();
		}
		
		
	     /* wait for the end of the simulation */

		
		
		for (int i = 0; i < horses.length; i++) {
			try {
				horses[i].join();
			}catch(InterruptedException e) {
				 System.out.println("Horse thread " + i + " has ended.\n");
			}
		}
		
	}	
	
}
