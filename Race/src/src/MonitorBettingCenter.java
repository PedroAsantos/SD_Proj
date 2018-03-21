package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.sun.javafx.collections.MappingChange.Map;

public class MonitorBettingCenter implements ISpectator_BettingCenter, IBroker_BettingCenter {
	private final ReentrantLock mutex;
	private final Condition spectator_condition;
	private final Condition broker_condition;
	private final Condition spectatorWaiting_condition;
	private final int numberOfSpectators;
	private boolean spectatorsQueueBol;
	private boolean spectatorBetAproving;
	private int numberOfBets;
	//hash map <cavalo,[espectator,money] -> assim com o contains value conseguimos ver logo se alguem ganhou e depois é fácil de retirar o valor.
	private HashMap<Integer,List<int[]>> spectatorBets;
	//index 0=horse
	//index 1=bet
	private int[] bet = new int[2];
	private int horseId;
	public MonitorBettingCenter(int numberOfSpectators) {
		mutex = new ReentrantLock(true);
		spectator_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		spectatorWaiting_condition = mutex.newCondition();
		this.numberOfSpectators = numberOfSpectators;
		numberOfBets=0;
		spectatorBetAproving=true;
		spectatorsQueueBol=false;
		spectatorBets = new HashMap<Integer,List<int[]>>(numberOfSpectators);
	}
	@Override
	public void acceptTheBets() {
		
		mutex.lock();
		try {
			System.out.println("ACCEPTING BETS BROKER");
			List <int[]> listTemp;
			while(numberOfBets < numberOfSpectators) {
				try {
					broker_condition.await();
					//place bet
					listTemp = new ArrayList<int[]>();
					if(spectatorBets.containsKey(horseId)) {
						listTemp = spectatorBets.get(horseId);
						listTemp.add(bet);
						spectatorBets.put(horseId,listTemp);
					}else {	
						listTemp.add(bet);
						spectatorBets.put(horseId,listTemp);
					}
					
					System.out.println("numberofbets: " + numberOfBets);
					numberOfBets++;
					System.out.println("Spectator_"+ bet[0] +" is betting on the horse"+horseId+" the money" + bet[1]);
					spectatorBetAproving=false;
					//spectatorsQueueBol=false;
					spectator_condition.signal();

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			numberOfBets=0;
			spectatorsQueueBol=false;
			//só para testes
			/*
			listTemp = spectatorBets.get(horseId);
			for(int i = 0;i<listTemp.size();i++){
				System.out.println(listTemp.get(i)[0]);
			}*/
			System.out.println("BROKER WAKE UP");
			
			
		} finally {
			mutex.unlock();
		}
		
		
	}

	

	@Override
	public void honourTheBets() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeABet(int spectator_id) {
		mutex.lock();
		try {
			//if(queue==0) {
				//spectatorsQueueBol=false;
			//}
			while(spectatorsQueueBol) {
				try {
					spectatorWaiting_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//queue++;
			spectatorsQueueBol=true;
			System.out.println("Spectator_"+spectator_id+" is betting!");
			this.horseId=0;  //vem de onde de fora?
			this.bet[0]=spectator_id; //spectator_id
			this.bet[1]=bet[1]; //money
			broker_condition.signal();
			
		
			while(spectatorBetAproving) {
				try {
					spectator_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			spectatorBetAproving=true;
			spectatorsQueueBol=false;
			spectatorWaiting_condition.signal();
				
				//this can not be like this .. use boolean to wake up broker.
			//	brokerCanNotGo=false;
						
			System.out.println(numberOfBets+ " " + numberOfSpectators);
		} finally {
			mutex.unlock();
			
		}
		
	}

}
