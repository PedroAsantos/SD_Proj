package src;
import stakeholders.Horse;

public interface IHorse_Track {
	public void proceedToStartLine(int horse_id);
	public void makeAMove(Horse horse);
	public boolean hasFinishLineBeenCrossed(Horse horse);

}
