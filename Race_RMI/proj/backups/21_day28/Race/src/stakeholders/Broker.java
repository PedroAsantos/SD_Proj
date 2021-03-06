package stakeholders;

import src.IBroker_BettingCenter;
import src.IBroker_Control;
import src.IBroker_Stable;
import src.IBroker_Track;
import src.MonitorRacingTrack;
import src.*;

import java.util.List;

import Enum.BrokerState;
//import jdk.management.resource.internal.TotalResourceContext;
public class Broker extends Thread {
	private volatile boolean running = true;
	    
//	private final int numberOfSpectators;
//	private final int numberOfHorses;
	private int numberOfRaces;
	private BrokerState state;
	private final IBroker_Control monitorControl;
	private final IBroker_BettingCenter monitorBettingCenter;
	private final IBroker_Stable monitorStable;
	private final IBroker_Track monitorTrack;
	Repository repo;
	
	public Broker(int numberOfRaces,IBroker_Control mControl, IBroker_BettingCenter mBettingCenter, IBroker_Stable monitorStable, IBroker_Track monitorTrack, Repository repo) {
//		this.numberOfSpectators=numberOfSpectators;
//		this.numberOfHorses=numberOfHorses;
		this.monitorControl=mControl;
		this.monitorBettingCenter=mBettingCenter;
		this.monitorStable = monitorStable;
		this.monitorTrack = monitorTrack;
		this.state = BrokerState.OPENING_THE_EVENT;
		this.numberOfRaces=numberOfRaces;
		this.repo=repo;
		//define state?
	}
	
	
	
	@Override
	public void run() {
			

		while(running) {
			//monitor.divide();	
			switch (state) {
				case OPENING_THE_EVENT:
					repo.setbrokerstate(state);
					System.out.print("Opening the event \n");
					monitorStable.summonHorsesToPaddock();
					state=BrokerState.ANNOUNCING_NEXT_RACE;
					repo.toLog();
					break;
				case ANNOUNCING_NEXT_RACE:
					repo.setbrokerstate(state);
					monitorBettingCenter.acceptTheBets();
					state=BrokerState.WAITING_FOR_THE_BETS;
					repo.toLog();
					break;
				case WAITING_FOR_THE_BETS:
					repo.setbrokerstate(state);
					monitorTrack.startTheRace();
					state=BrokerState.SUPERVISING_THE_RACE;
					repo.toLog();
					break;
				case SUPERVISING_THE_RACE:
					repo.setbrokerstate(state);
					//passar do track para o control que cavalos ganharam!
					List<Integer> horseWinners;
					horseWinners = monitorTrack.reportResults();	
					numberOfRaces--;
					monitorControl.reportResults(horseWinners);
					if(monitorBettingCenter.areThereAnyWinners(horseWinners)) {
						monitorBettingCenter.honourTheBets();
						state=BrokerState.SETTING_ACCOUNTS;
					}
					if(numberOfRaces==0) {
						monitorControl.entertainTheGuests();
						state=BrokerState.PLAYING_HOST_AT_THE_BAR;
					}else {
						monitorStable.summonHorsesToPaddock();
						state=BrokerState.ANNOUNCING_NEXT_RACE;
					}
					repo.toLog();
					break;
				case SETTING_ACCOUNTS:
					repo.setbrokerstate(state);
					if(numberOfRaces==0) {
						monitorControl.entertainTheGuests();
						state=BrokerState.PLAYING_HOST_AT_THE_BAR;
					}else {
						monitorStable.summonHorsesToPaddock();
						state=BrokerState.ANNOUNCING_NEXT_RACE;
					}
					repo.toLog();
					break;
				case PLAYING_HOST_AT_THE_BAR:
					repo.setbrokerstate(state);
					System.out.println("EVENT END");
					stopRunning();
					repo.toLog();
					break;
				default:
					break;
			}
			try {
				Thread.sleep(500);
			} catch(Exception e) {
				
			}
		}
	}
	
	public void stopRunning(){
        running = false;
	}

}
