package stakeholders;
import Enum.HorseState;
import src.*;
import Enum.SpectatorState;

public class Spectator extends Thread {
	private final int id;
	private final ISpectator_BettingCenter monitorBettingCenter;
	private final ISpectator_Control monitorControl;
	private final ISpectator_Paddock monitorPaddock;
	private double money;
	private SpectatorState state;
	public Spectator(int id,int money,ISpectator_BettingCenter monitorBettingCenter, ISpectator_Control monitorControl, ISpectator_Paddock monitorPaddock) {
		this.id=id;
		this.money=money;
		this.monitorBettingCenter = monitorBettingCenter;
		this.monitorControl = monitorControl;
		this.monitorPaddock=monitorPaddock;
		this.state= SpectatorState.WAITING_FOR_A_RACE_TO_START;
		
	}
	
	
	public void addMoney(double moneyWon) {
		money=money+moneyWon;
	}
	public double getMoney() {
		return money;
	}
	public int getID() {
		return id;
	}
	@Override
	public void run() {
			
		while(true) {
			//monitor.divide();	
			switch (state) {
				case WAITING_FOR_A_RACE_TO_START:
					monitorPaddock.waitForNextRace(id);
					monitorPaddock.goCheckHorses(id);
			
					state=SpectatorState.APPRAISING_THE_HORSES;
					break;
				case APPRAISING_THE_HORSES:
					monitorBettingCenter.placeABet(this);
					state=SpectatorState.PLACING_A_BET;
					break;
				case PLACING_A_BET:
					monitorControl.goWatchTheRace(id);
					state=SpectatorState.WATCHING_THE_RACE;
					break;
				case WATCHING_THE_RACE:
					if(monitorControl.haveIwon(id)) {
						state=SpectatorState.COLLECTING_THE_GAINS;
					}else {
						if(monitorControl.noMoreRaces()) {
							state=SpectatorState.CELEBRATING;
						}else {
							state=SpectatorState.WAITING_FOR_A_RACE_TO_START;
						}	
					}
					break;
				case COLLECTING_THE_GAINS:
					monitorBettingCenter.goCollectTheGains(this);
					if(monitorControl.noMoreRaces()) {
						state=SpectatorState.CELEBRATING;
					}else {
						state=SpectatorState.WAITING_FOR_A_RACE_TO_START;
					}					
					break;
				case CELEBRATING:
						System.out.println("CELEBRATING");
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
