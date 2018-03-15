import src.*;

public class RunRace {

	public static void main(String[] args) {
				
		int numberOfHorses = 4; //testar com n�meros maiores.
		int numberOfSpectators=4;
		
		
		MonitorBettingCenter mBettingCenter = new MonitorBettingCenter();
		MonitorControlCenter mControlCenter = new MonitorControlCenter();
		MonitorPaddock mPaddock = new MonitorPaddock();
		MonitorRacingTrack mRacingTrack = new MonitorRacingTrack();
		MonitorStable mStable = new MonitorStable();
		
		
	     /* problem initialization */
		
		Horse[] horses = new Horse[numberOfHorses];
		Spectator[] spectators = new Spectator[numberOfSpectators]; 
		
		Broker broker = new Broker(numberOfSpectators,numberOfHorses,(IBroker_Control) mControlCenter,(IBroker_BettingCenter) mBettingCenter);
		
		for (int i = 0; i < spectators.length; i++) {
			spectators[i] = new Spectator(i,(ISpectator_BettingCenter) mBettingCenter,(ISpectator_Control) mControlCenter);
		}
		
		for (int i = 0; i < horses.length; i++) {
			int performace=123;
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
