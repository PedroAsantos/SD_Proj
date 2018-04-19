package src;

import stakeholders.Spectator;

public interface ISpectator_BettingCenter {
	public void placeABet(int spectator_id);
	public void goCollectTheGains(Spectator spectator);
}
