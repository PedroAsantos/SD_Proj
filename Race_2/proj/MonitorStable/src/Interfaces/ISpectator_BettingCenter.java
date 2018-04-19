package Interfaces;

public interface ISpectator_BettingCenter {
	public void placeABet(int spectatorId,double money,int horsePicked);
	public double goCollectTheGains(int spectatorId);
}
