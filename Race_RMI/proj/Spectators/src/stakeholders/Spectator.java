package stakeholders;

import java.util.Random;

import Enum.SpectatorState;
import Interfaces.IRepository;
import Interfaces.ISpectator_BettingCenter;
import Interfaces.ISpectator_Control;
import Interfaces.ISpectator_Paddock;
import java.rmi.RemoteException;


public class Spectator extends Thread {
	private volatile boolean running = true;

	private final int id;
	private final ISpectator_BettingCenter monitorBettingCenter;
	private final ISpectator_Control monitorControl;
	private final ISpectator_Paddock monitorPaddock;
	private double money;
	private int horsePicked;
	private SpectatorState state;
	IRepository repo;

	public Spectator(int id,int money,ISpectator_BettingCenter monitorBettingCenter, ISpectator_Control monitorControl, ISpectator_Paddock monitorPaddock, IRepository repo) throws RemoteException {
		this.id=id;
		this.money=money;
		this.monitorBettingCenter = monitorBettingCenter;
		this.monitorControl = monitorControl;
		this.monitorPaddock=monitorPaddock;
		this.state= SpectatorState.WAITING_FOR_A_RACE_TO_START;
		this.repo = repo;
		repo.setSpecStat(id,state);
	}
	
	
	public void addMoney(double moneyWon) throws RemoteException {
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
                    try{
			switch (state) {
				case WAITING_FOR_A_RACE_TO_START:
					System.out.println("WAITING_FOR_A_RACE_TO_START Spectator_"+id);
					monitorPaddock.waitForNextRace(id);
					horsePicked=monitorPaddock.goCheckHorses(id);
					repo.setspecMoney(id,money);
					
					state=SpectatorState.APPRAISING_THE_HORSES;
					repo.setSpecStat(id,state);
					repo.toLog();
					break;
				case APPRAISING_THE_HORSES:
					System.out.println("APPRAISING_THE_HORSES Spectator_"+id);
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
					System.out.println("PLACING_A_BET Spectator_"+id);
					monitorControl.goWatchTheRace(id);
					state=SpectatorState.WATCHING_THE_RACE;
					repo.setSpecStat(id,state);
					repo.toLog();
					break;
				case WATCHING_THE_RACE:
					System.out.println("WATCHING_THE_RACE Spectator_"+id);
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
					System.out.println("COLLECTING_THE_GAINS Spectator_"+id);
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
						System.out.println("CELEBRATING Spectator_"+id);
						repo.setSpecStat(id,state);
						repo.toLog();
						stopRunning();
					break;
				default:
					break;
			}
                        } catch (Exception e) {
                            e.printStackTrace();
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
