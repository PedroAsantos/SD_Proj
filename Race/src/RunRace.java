import java.util.Random;

import src.*;
import stakeholders.*;

public class RunRace {

	public static void main(String[] args) {
				
		int numberOfHorses = 4; //testar com números maiores.
		int numberOfSpectators=4;
		
		
		MonitorBettingCenter mBettingCenter = new MonitorBettingCenter(numberOfSpectators);
		MonitorControlCenter mControlCenter = new MonitorControlCenter();
		MonitorPaddock mPaddock = new MonitorPaddock(numberOfHorses,numberOfSpectators);
		MonitorRacingTrack mRacingTrack = new MonitorRacingTrack(numberOfHorses);
		MonitorStable mStable = new MonitorStable(numberOfHorses);
		
		
	     /* problem initialization */
		
		Horse[] horses = new Horse[numberOfHorses];
		Spectator[] spectators = new Spectator[numberOfSpectators]; 
		
		Broker broker = new Broker(numberOfSpectators,numberOfHorses,(IBroker_Control) mControlCenter,(IBroker_BettingCenter) mBettingCenter,(IBroker_Stable) mStable,(IBroker_Track) mRacingTrack);
		
		for (int i = 0; i < spectators.length; i++) {
			spectators[i] = new Spectator(i,(ISpectator_BettingCenter) mBettingCenter,(ISpectator_Control) mControlCenter, (ISpectator_Paddock) mPaddock);
		}
		
		for (int i = 0; i < horses.length; i++) {
			Random random = new Random();
			int performace= random.nextInt(10)+1;
			horses[i] = new Horse(i,performace,(IHorse_Track) mRacingTrack,(IHorse_Stable) mStable,(IHorse_Paddock) mPaddock);
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
