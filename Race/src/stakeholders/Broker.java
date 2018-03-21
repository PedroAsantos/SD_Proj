package stakeholders;

import src.IBroker_BettingCenter;
import src.IBroker_Control;
import src.IBroker_Stable;
import src.IBroker_Track;
import src.MonitorRacingTrack;

import java.util.List;

import Enum.BrokerState;
public class Broker extends Thread {
	private final int numberOfSpectators;
	private final int numberOfHorses;
	private BrokerState state;
	private final IBroker_Control monitorControl;
	private final IBroker_BettingCenter monitorBettingCenter;
	private final IBroker_Stable monitorStable;
	private final IBroker_Track monitorTrack;
	
	public Broker(int numberOfSpectators, int numberOfHorses,IBroker_Control mControl, IBroker_BettingCenter mBettingCenter, IBroker_Stable monitorStable, IBroker_Track monitorTrack) {
		this.numberOfSpectators=numberOfSpectators;
		this.numberOfHorses=numberOfHorses;
		this.monitorControl=mControl;
		this.monitorBettingCenter=mBettingCenter;
		this.monitorStable = monitorStable;
		this.monitorTrack = monitorTrack;
		this.state = BrokerState.OPENING_THE_EVENT;
		//define state?
	}
	@Override
	public void run() {
			

		while(true) {
			//monitor.divide();	
			switch (state) {
				case OPENING_THE_EVENT:
					System.out.print("Opening the event \n");
					monitorStable.summonHorsesToPaddock();
					state=BrokerState.ANNOUNCING_NEXT_RACE;
					break;
				case ANNOUNCING_NEXT_RACE:
					monitorBettingCenter.acceptTheBets();
					state=BrokerState.WAITING_FOR_THE_BETS;
					break;
				case WAITING_FOR_THE_BETS:
					monitorTrack.startTheRace();
					state=BrokerState.SUPERVISING_THE_RACE;
					break;
				case SUPERVISING_THE_RACE:
					//passar do track para o control que cavalos ganharam!
					List<Integer> horseWinners;
					horseWinners = monitorTrack.reportResults();
					System.out.println("WINNER: "+ horseWinners.get(0));
					break;
				case SETTING_ACCOUNTS:
					
					break;
				case PLAYING_HOST_AT_THE_BAR:
					
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
}
