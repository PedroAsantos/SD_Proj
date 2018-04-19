package stakeholders;

import java.util.Random;

import Enum.SpectatorState;
import interfaces.ISpectator_BettingCenter;
import interfaces.ISpectator_Control;
import interfaces.ISpectator_Paddock;
import sharingRegions.*;

public class Spectator extends Thread {
	private volatile boolean running = true;

	private final int id;
	private final ISpectator_BettingCenter monitorBettingCenter;
	private final ISpectator_Control monitorControl;
	private final ISpectator_Paddock monitorPaddock;
	private double money;
	private int horsePicked;
	private SpectatorState state;
	Repository repo;

	public Spectator(int id,int money,ISpectator_BettingCenter monitorBettingCenter, ISpectator_Control monitorControl, ISpectator_Paddock monitorPaddock, Repository repo) {
		this.id=id;
		this.money=money;
		this.monitorBettingCenter = monitorBettingCenter;
		this.monitorControl = monitorControl;
		this.monitorPaddock=monitorPaddock;
		this.state= SpectatorState.WAITING_FOR_A_RACE_TO_START;
		this.repo = repo;
		repo.setSpecStat(id,state);
	}
	
	
	public void addMoney(double moneyWon) {
		money=money+moneyWon;
		repo.setspecMoney(id,money);
	}
	public double getMoney() {
		return money;
	}
	public void payBet(double moneyBet) {
		money=money-moneyBet;
	}
	public int getID() {
		return id;
	}
	@Override
	public void run() {
			
		while(running) {
			switch (state) {
				case WAITING_FOR_A_RACE_TO_START:
					monitorPaddock.waitForNextRace(id);
					horsePicked=monitorPaddock.goCheckHorses(id);
					repo.setspecMoney(id,money);
					
					state=SpectatorState.APPRAISING_THE_HORSES;
					repo.setSpecStat(id,state);
					repo.toLog();
					break;
				case APPRAISING_THE_HORSES:
					double bet;
					Random random = new Random();
					bet = random.nextDouble()*money;
					money=money-bet;
					//novo
					monitorBettingCenter.placeABet(id, bet,horsePicked);
					repo.setspecMoney(id,money);
					state=SpectatorState.PLACING_A_BET;
					repo.setSpecStat(id,state);
					repo.toLog();
					break;
				case PLACING_A_BET:
					monitorControl.goWatchTheRace(id);
					state=SpectatorState.WATCHING_THE_RACE;
					repo.setSpecStat(id,state);
					repo.toLog();
					break;
				case WATCHING_THE_RACE:
					if(monitorControl.haveIwon(id,horsePicked)) {
						state=SpectatorState.COLLECTING_THE_GAINS;
					}else {
						if(monitorControl.noMoreRaces()) {
							monitorControl.relaxABit(id);
							state=SpectatorState.CELEBRATING;
						}else {
							state=SpectatorState.WAITING_FOR_A_RACE_TO_START;
						}	
					}
					repo.setSpecStat(id,state);
					repo.toLog();
					break;
				case COLLECTING_THE_GAINS:
					double moneyReceived=0;
					moneyReceived=monitorBettingCenter.goCollectTheGains(id);
					money=money+moneyReceived;
					repo.setspecMoney(id,money);
					if(monitorControl.noMoreRaces()) {
						monitorControl.relaxABit(id);
						state=SpectatorState.CELEBRATING;
					}else {
						state=SpectatorState.WAITING_FOR_A_RACE_TO_START;
					}			
					repo.setSpecStat(id,state);
					repo.toLog();
					break;
				case CELEBRATING:
						System.out.println("CELEBRATING");
						repo.setSpecStat(id,state);
						repo.toLog();
						stopRunning();
					break;
				default:
					break;
			}
		/*	try {
				Thread.sleep(500);
			} catch(Exception e) {
				
			}*/
		}
	}
	public void stopRunning(){
        running = false;
        System.out.println("Spectator_"+id+" exit");
	}

}
