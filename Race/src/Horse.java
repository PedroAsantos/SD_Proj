import src.*;
import Enum.HorseState;
public class Horse extends Thread {
	private final int id;
	private int performance;
	private int position;
	private HorseState state; 
	private final IHorse_Track monitorTrack;
	private final IHorse_Stable monitorStable;
	private final IHorse_Paddock monitorPaddock;

	public Horse(int id,int performance,IHorse_Track monitorTrack,IHorse_Stable monitorStable,IHorse_Paddock monitorPaddock) {
		this.id=id;
		this.performance=performance;
		this.position=0;
		this.monitorTrack = monitorTrack;
		this.monitorStable=monitorStable;
		this.monitorPaddock=monitorPaddock;
		//define state?
	}
	
	
	@Override
	public void run() {
		
		while(true) {
			//monitor.divide();	
			switch (state) {
			case AT_THE_STABLE:
				
				break;
			case AT_THE_PADDOCK:
				
				break;
			case AT_THE_START_LINE:
				
				break;
			case RUNNING:
				
				break;
			case AT_THE_FINISH_LINE:
				
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
