package stakeholders;
import Enum.HorseState;
import Interfaces.IHorse_Paddock;
import Interfaces.IHorse_Stable;
import Interfaces.IHorse_Track;
import sharingRegions.*;
public class Horse extends Thread {
    private volatile boolean running = true;
	private final int id;
	private int performance;
	private HorseState state; 
	private final IHorse_Track monitorTrack;
	private final IHorse_Stable monitorStable;
	private final IHorse_Paddock monitorPaddock;
	Repository repo;

	public Horse(int id,int performance,IHorse_Track monitorTrack,IHorse_Stable monitorStable,IHorse_Paddock monitorPaddock, Repository repo) {
		this.id=id;
		this.performance=performance;
		this.monitorTrack = monitorTrack;
		this.monitorStable=monitorStable;
		this.monitorPaddock=monitorPaddock;
		this.state=HorseState.AT_THE_STABLE;
		this.repo=repo;
		repo.setHorseStat(id,state);
		//define state?
	}
	

	@Override
	public void run() {
		
		while(running) {
			switch (state) {
				case AT_THE_STABLE:
					System.out.println("AT_THE_STABLE horse_"+id);
					if(monitorStable.proceedToStable(id)){
					//s	repo.sethorseruns(id,runs);
						state=HorseState.AT_THE_PADDOCK;
						repo.setHorseStat(id,state);
						repo.toLog();
					}else {
						stopRunning();
					}
				
					break;
				case AT_THE_PADDOCK:
					System.out.println("AT_THE_PADDOCK horse_"+id);
					monitorPaddock.proceedToPaddock(id,performance);
					//state=HorseState.AT_THE_FINISH_LINE;
					state=HorseState.AT_THE_START_LINE;
					repo.setHorseStat(id,state);
					repo.toLog();
					break;
				case AT_THE_START_LINE:
					System.out.println("AT_THE_START_LINE horse_"+id);
					monitorTrack.proceedToStartLine(id,performance);
					state=HorseState.RUNNING;
					repo.setHorseStat(id,state);
					repo.toLog();
					break;
				case RUNNING:
					System.out.println("RUNNING horse_"+id);
					monitorTrack.makeAMove(id);
					if(monitorTrack.hasFinishLineBeenCrossed(id)) {
						state=HorseState.AT_THE_FINISH_LINE;
					}
					repo.setHorseStat(id,state);
					repo.toLog();
					break;
				case AT_THE_FINISH_LINE:
					System.out.println("AT_THE_FINISH_LINE horse_"+id);
					state=HorseState.AT_THE_STABLE;
					repo.setHorseStat(id,state);
					repo.toLog();
					break;
				default:
					break;
			}
		/*	try {
				Thread.sleep(500);
			} catch(Exception e) {
				
			}*/
		}
	}
	  public void stopRunning(){
	        running = false;
	        System.out.println("Horse"+id+" exit");
	    }
		
}
