package stakeholders;
import src.*;
import java.util.*;
import Enum.HorseState;
public class Horse extends Thread {
	private final int id;
	private int performance;
	private int position;
	private int runs;
	private HorseState state; 
	private final IHorse_Track monitorTrack;
	private final IHorse_Stable monitorStable;
	private final IHorse_Paddock monitorPaddock;
	Repository repo;

	public Horse(int id,int performance,IHorse_Track monitorTrack,IHorse_Stable monitorStable,IHorse_Paddock monitorPaddock, Repository repo) {
		this.id=id;
		this.performance=performance;
		this.position=0;
		this.monitorTrack = monitorTrack;
		this.monitorStable=monitorStable;
		this.monitorPaddock=monitorPaddock;
		runs=0;
		this.state=HorseState.AT_THE_STABLE;
		this.repo=repo;
		repo.setHorseStat(id,state);
		repo.sethorseruns(id,runs);
		//define state?
	}
	
	public void resetHorse() {
		runs=0;
		position=0;
	}
	
	public void moveofPosition(int movingPos ) {
		position+=movingPos;
		repo.sethorseruns(id,runs);

	}
	public int getRuns() {
		return runs;
	}
	public void incrementRuns() {
		runs++;
	}
	public int getPerformance() {
		return performance;
	}
	public int getPosition() {
		return position;
	}
	public int getID() {
		return id;
	}
	
	@Override
	public void run() {
		
		while(true) {
			//monitor.divide();	
			switch (state) {
				case AT_THE_STABLE:
					monitorStable.proceedToStable(this);
					state=HorseState.AT_THE_PADDOCK;
					repo.setHorseStat(id,state);
					repo.toLog();
					break;
				case AT_THE_PADDOCK:
					monitorPaddock.proceedToPaddock(this);
					state=HorseState.AT_THE_START_LINE;
					repo.setHorseStat(id,state);
					repo.toLog();
					break;
				case AT_THE_START_LINE:
					monitorTrack.proceedToStartLine(this);
					state=HorseState.RUNNING;
					repo.setHorseStat(id,state);
					repo.toLog();
					break;
				case RUNNING:
					monitorTrack.makeAMove(this);
					if(monitorTrack.hasFinishLineBeenCrossed(this)) {
						state=HorseState.AT_THE_FINISH_LINE;
					}
					repo.setHorseStat(id,state);
					repo.toLog();
					break;
				case AT_THE_FINISH_LINE:
					state=HorseState.AT_THE_STABLE;
					repo.setHorseStat(id,state);
					repo.toLog();
					//System.out.println("WINNER"+id);
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
