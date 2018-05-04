import java.util.Random;


import Interfaces.IHorse_Paddock;
import Interfaces.IHorse_Stable;
import Interfaces.IHorse_Track;
import sharingRegions.MonitorPaddock;
import sharingRegions.MonitorRacingTrack;
import sharingRegions.MonitorStable;
import sharingRegions.Repository;
import stakeholders.Horse;

public class RunHorses {

	public static void main(String[] args) {
		int numberOfHorses = 20; //testar com numeros maiores.
		int maxPerformance=10;
		
		Repository repo = new Repository();

		MonitorPaddock mPaddock = new MonitorPaddock();
		MonitorRacingTrack mRacingTrack = new MonitorRacingTrack();
		MonitorStable mStable = new MonitorStable();
		
		Horse[] horses = new Horse[numberOfHorses];
		
		for (int i = 0; i < horses.length; i++) {
			Random random = new Random();
			int performace= random.nextInt(maxPerformance)+1;
			horses[i] = new Horse(i,performace,(IHorse_Track) mRacingTrack,(IHorse_Stable) mStable,(IHorse_Paddock) mPaddock, repo);
		}
		
		 /* start of the simulation */
		
	
		for (int i = 0; i < horses.length; i++) {
			System.out.println("Horse"+horses[i]+"is starting!");
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
