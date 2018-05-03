import interfaces.ISpectator_BettingCenter;
import interfaces.ISpectator_Control;
import interfaces.ISpectator_Paddock;
import sharingRegions.MonitorBettingCenter;
import sharingRegions.MonitorControlCenter;
import sharingRegions.MonitorPaddock;
import sharingRegions.Repository;
import stakeholders.Spectator;

public class RunSpectators {
	public static void main(String[] args) {
		
		int numberOfHorses = 20; //testar com numeros maiores.
		int numberOfSpectators=4;
		int numberOfRaces=5;
		int horsesPerRace=4;
		int raceLength=30;
		Repository repo = new Repository();

		MonitorBettingCenter mBettingCenter = new MonitorBettingCenter(repo);
		MonitorControlCenter mControlCenter = new MonitorControlCenter(repo);
		MonitorPaddock mPaddock = new MonitorPaddock(repo);
		
		Spectator[] spectators = new Spectator[numberOfSpectators]; 
		
		int money;
		for (int i = 0; i < spectators.length; i++) {
			money=1000;
			spectators[i] = new Spectator(i,money,(ISpectator_BettingCenter) mBettingCenter,(ISpectator_Control) mControlCenter, (ISpectator_Paddock) mPaddock, repo);
		}
	
		 /* start of the simulation */
		
		
		for (int i = 0; i < spectators.length; i++) {
			spectators[i].start();
		}
	
	     /* wait for the end of the simulation */

		
		
		for (int i = 0; i < spectators.length; i++) {
			 try
		       { 
				 spectators[i].join ();
		       }
		       catch (InterruptedException e) {
		    	   System.out.println("Spectator thread " + i + " has ended.\n");
		       }
		}
		
	}	
	
}

