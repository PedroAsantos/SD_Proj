package Interfaces;

public interface ISpectator_Control {
	public void goWatchTheRace(int spectator_id);
	public void relaxABit(int spectator_id);
	public boolean haveIwon(int spectator_id,int horsePicked);
	public boolean noMoreRaces();
}
