package Interfaces;

import java.util.List;

public interface IBroker_BettingCenter {
	public void acceptTheBets();
	public void honourTheBets();
	public boolean areThereAnyWinners(List<Integer> horseWinners);

}
