package src;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;



public class MonitorRacingTrack implements IHorse_Track{
	private final ReentrantLock r1;
	private final Condition inc;
	private final Condition div;
	
	public MonitorRacingTrack() {
		r1 = new ReentrantLock(true);
		inc = r1.newCondition();
		div = r1.newCondition();
	}
	
	@Override
	public void makeAMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hasFinishLineBeenCrossed() {
		// TODO Auto-generated method stub
		
	}
	
}
