import src.*;

public class Spectator extends Thread {
	private final int id;
	private final ISpectator_BettingCenter mBettingCenter;
	private final ISpectator_Control mControl;
	
	public Spectator(int id,ISpectator_BettingCenter mBettingCenter, ISpectator_Control mControl) {
		this.id=id;
		this.mBettingCenter = mBettingCenter;
		this.mControl = mControl;
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
