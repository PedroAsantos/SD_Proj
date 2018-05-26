import java.util.Random;

import src.*;
import stakeholders.*;

public class RunRace {

	public static void main(String[] args) {
				
		int numberOfHorses = 4000; //testar com numeros maiores.
		int numberOfSpectators=4;
		int numberOfRaces=2;
		int horsesPerRace=1000;
		int raceLength=50;
		int maxPerformance=10;
		Repository repo = new Repository(numberOfHorses,numberOfSpectators,numberOfRaces,horsesPerRace);
		repo.writeLog();

		MonitorBettingCenter mBettingCenter = new MonitorBettingCenter(numberOfSpectators, repo);
		MonitorControlCenter mControlCenter = new MonitorControlCenter(repo,numberOfSpectators);
		MonitorPaddock mPaddock = new MonitorPaddock(horsesPerRace,numberOfSpectators, repo);
		MonitorRacingTrack mRacingTrack = new MonitorRacingTrack(horsesPerRace, raceLength, repo);
		MonitorStable mStable = new MonitorStable(numberOfHorses,horsesPerRace, repo);
		
		
	     /* problem initialization */
		
		Horse[] horses = new Horse[numberOfHorses];
		Spectator[] spectators = new Spectator[numberOfSpectators]; 
		
		Broker broker = new Broker(numberOfRaces,(IBroker_Control) mControlCenter,(IBroker_BettingCenter) mBettingCenter,(IBroker_Stable) mStable,(IBroker_Track) mRacingTrack, repo);
		int money;
		for (int i = 0; i < spectators.length; i++) {
			money=100;
			spectators[i] = new Spectator(i,money,(ISpectator_BettingCenter) mBettingCenter,(ISpectator_Control) mControlCenter, (ISpectator_Paddock) mPaddock, repo);
		}
		
		for (int i = 0; i < horses.length; i++) {
			Random random = new Random();
			int performace= random.nextInt(maxPerformance)+1;
			horses[i] = new Horse(i,performace,(IHorse_Track) mRacingTrack,(IHorse_Stable) mStable,(IHorse_Paddock) mPaddock, repo);
		}
		
		 /* start of the simulation */
		
		broker.start();
		
		for (int i = 0; i < spectators.length; i++) {
			spectators[i].start();
		}
		
		for (int i = 0; i < horses.length; i++) {
			horses[i].start();
		}
		
		
	     /* wait for the end of the simulation */

		try {
			broker.join();
		} catch (InterruptedException e) {
			System.out.println("Broker thread has ended.\n");
		}
		
		
		for (int i = 0; i < spectators.length; i++) {
			 try
		       { 
				 spectators[i].join ();
		       }
		       catch (InterruptedException e) {
		    	   System.out.println("Spectator thread " + i + " has ended.\n");
		       }
		}
		
		for (int i = 0; i < horses.length; i++) {
			try {
				horses[i].join();
			}catch(InterruptedException e) {
				 System.out.println("Horse thread " + i + " has ended.\n");
			}
		}
		
	}

}
