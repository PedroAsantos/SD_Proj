package Interfaces;
import stakeholders.Horse;

public interface IHorse_Track {
	public void proceedToStartLine(Horse horse);
	public void makeAMove(Horse horse);
	public boolean hasFinishLineBeenCrossed(Horse horse);

}
