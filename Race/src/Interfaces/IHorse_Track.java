package Interfaces;

public interface IHorse_Track {
	public void proceedToStartLine(int horseId,int performance);
	public void makeAMove(int horseId);
	public boolean hasFinishLineBeenCrossed(int horseId);

}
