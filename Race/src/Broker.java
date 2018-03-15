import src.IBroker_BettingCenter;
import src.IBroker_Control;

public class Broker extends Thread {
	private final int numberOfSpectators;
	private final int numberOfHorses;
	private String state;
	private final IBroker_Control mControl;
	private final IBroker_BettingCenter mBettingCenter;
	
	public Broker(int numberOfSpectators, int numberOfHorses,IBroker_Control mControl, IBroker_BettingCenter mBettingCenter) {
		this.numberOfSpectators=numberOfSpectators;
		this.numberOfHorses=numberOfHorses;
		this.mControl=mControl;
		this.mBettingCenter=mBettingCenter;
		//define state?
	}
	@Override
	public void run() {
			
		while(true) {
		//	monitor.divide();
			try {
				Thread.sleep(500);
			} catch(Exception e) {
				
			}
		}
	}
}
