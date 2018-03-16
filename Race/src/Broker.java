import src.IBroker_BettingCenter;
import src.IBroker_Control;
import src.IBroker_Stable;
import Enum.BrokerState;
public class Broker extends Thread {
	private final int numberOfSpectators;
	private final int numberOfHorses;
	private BrokerState state;
	private final IBroker_Control monitorControl;
	private final IBroker_BettingCenter monitorBettingCenter;
	private final IBroker_Stable monitorStable;
	
	public Broker(int numberOfSpectators, int numberOfHorses,IBroker_Control mControl, IBroker_BettingCenter mBettingCenter, IBroker_Stable monitorStable) {
		this.numberOfSpectators=numberOfSpectators;
		this.numberOfHorses=numberOfHorses;
		this.monitorControl=mControl;
		this.monitorBettingCenter=mBettingCenter;
		this.monitorStable = monitorStable;
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
					
					break;
				case ANNOUNCING_NEXT_RACE:
					
					break;
				case WAITING_FOR_THE_BETS:
					
					break;
				case SUPERVISING_THE_RACE:
					
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
