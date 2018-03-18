package src;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;



public class MonitorRacingTrack implements IHorse_Track, IBroker_Track{
	private final ReentrantLock mutex;
	private final Condition horse_condition;
	private final Condition broker_condition;
	private final int totalHorses;
	
	private int horses_at_start_line;
	private boolean horsesCanNotRace;
	public MonitorRacingTrack(int totalHorses) {
		mutex = new ReentrantLock(true);
		horse_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		this.totalHorses=totalHorses;
		horses_at_start_line=0;
		horsesCanNotRace = true;
		
	}
	
	@Override
	public void startTheRace() {
			mutex.lock();
			try {
				
				System.out.println("STARTING THE RACE");
				
				while(horses_at_start_line<totalHorses) {
					try {
						broker_condition.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				horsesCanNotRace=false;
				horse_condition.signalAll();
								
				
				
			}finally {
				mutex.unlock();
			}
	}
	
	@Override
	public void proceedToStartLine(int horse_id) {
		// TODO Auto-generated method stub
	
		mutex.lock();
		try {
			horses_at_start_line++;
			System.out.println("Horse_"+horse_id+" is going to startLine");
			if(horses_at_start_line == totalHorses) {
				broker_condition.signal();
			}
			
			try {
				 while(horsesCanNotRace) {
					 horse_condition.await();
				 }
			}catch(Exception e) {
				System.out.println("Horse ProceedToStarLine "+e);
			}
			horses_at_start_line--;
			if(horses_at_start_line==0) {
				horsesCanNotRace=true;
			}
		}finally {
			mutex.unlock();
		}
	
		
		
	
		
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
