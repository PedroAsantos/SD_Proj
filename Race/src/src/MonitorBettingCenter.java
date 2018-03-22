package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.sun.javafx.collections.MappingChange.Map;

import stakeholders.Spectator;

public class MonitorBettingCenter implements ISpectator_BettingCenter, IBroker_BettingCenter {
	private final ReentrantLock mutex;
	private final Condition spectator_condition;
	private final Condition broker_condition;
	private final Condition spectatorWaiting_condition;
	private final int numberOfSpectators;
	private boolean spectatorsQueueBol;
	private boolean spectatorBetAproving;
	private boolean receivingDividends;
	private boolean waitingForTheSpectator;
	private int numberOfBets;
	//hash map <cavalo,[espectator,money] -> assim com o contains value conseguimos ver logo se alguem ganhou e depois e facil de retirar o valor.
	private HashMap<Integer,List<double[]>> spectatorBets;
	private List<Condition> receivMoneyList ;
	private List<Integer> horseWinners;
	private Queue<Spectator> queueToReceivMoney;
	//index 0=horse
	//index 1=bet
	private double[] bet = new double[2];
	private int horseId;
	private HashMap<Integer,Integer> horsePerformance;
	private HashMap<Integer,Double> horseProbabilities;
	Repository repo;


	public MonitorBettingCenter(int numberOfSpectators, Repository repo) {
		mutex = new ReentrantLock(true);
		spectator_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		spectatorWaiting_condition = mutex.newCondition();
		this.numberOfSpectators = numberOfSpectators;
		numberOfBets=0;
		spectatorBetAproving=true;
		spectatorsQueueBol=false;
		receivingDividends=true;
		waitingForTheSpectator = true;
		spectatorBets = new HashMap<Integer,List<double[]>>(numberOfSpectators);
		receivMoneyList = new ArrayList<Condition>();
		queueToReceivMoney = new LinkedList<Spectator>();
		this.repo=repo;
		horsePerformance = repo.gethorsePerformance();
		horseProbabilities = new HashMap<Integer,Double>();
	}
	@Override
	public void acceptTheBets() {
		
		mutex.lock();
		try {
			System.out.println("ACCEPTING BETS BROKER");
			List <double[]> listTemp;
			while(numberOfBets < numberOfSpectators) {
				try {
					broker_condition.await();
					//place bet
					listTemp = new ArrayList<double[]>();
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
			//so para testes
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
	public void goCollectTheGains(Spectator spectator) {
		// TODO Auto-generated method stub
		//jogar com o facto de a ordem de chegada e igual a de  saida FIFO. E dar assim a saida deles.registando a entrada
		mutex.lock();
		try {
			queueToReceivMoney.add(spectator);
			broker_condition.signal();
			while(receivingDividends) {
				try {
					receivMoneyList .get(spectator.getID()).await();
						
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			receivingDividends=true;
		} finally {
			// TODO: handle finally clause
			mutex.lock();
		}
	}
	@Override
	public void honourTheBets() {
		// TODO Auto-generated method stub
		mutex.lock();
		try {
			List<double[]> winners = new ArrayList<double[]>();
			for(int i=0;i<horseWinners.size();i++) {
				winners.addAll(spectatorBets.get(horseWinners.get(i)));
			}
			Spectator spectatorReceivingMoney;
			while(queueToReceivMoney.size()!=0) {
				try {
					broker_condition.await();
					spectatorReceivingMoney=queueToReceivMoney.remove();
					for(int i=0;i<winners.size();i++) {
						if(winners.get(i)[0]==spectatorReceivingMoney.getID()) {
							//how to knoe the horse that spectator bet
							System.out.println(spectatorReceivingMoney.getMoney());
							spectatorReceivingMoney.addMoney(winners.get(i)[1]*1/horseProbabilities.get(horseWinners.get(0)));
							System.out.println(spectatorReceivingMoney.getMoney());
							break;
							
						}
						receivingDividends=false;
						receivMoneyList.get(spectatorReceivingMoney.getID()).signal();
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
				
		} finally {
			mutex.unlock();
		}
		
	}
	@Override
	public boolean areThereAnyWinners(List<Integer> horseWinners) {
		boolean thereAreWinner=false;
		mutex.lock();
		try {
			this.horseWinners = new ArrayList<>(horseWinners);
			for(int i = 0;i < horseWinners.size();i++) {
				if(spectatorBets.containsKey(horseWinners.get(i))) {
					thereAreWinner=true;
				}
			}
		}finally {
			mutex.unlock();
		}
		return thereAreWinner;
		
	}


	@Override
	public void placeABet(Spectator spectator) {
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
			System.out.println("Spectator_"+spectator.getID()+" is betting!");
			horsePerformance=repo.gethorsePerformance();
			if(horseProbabilities.size()==0) {
				int totalP=0;
				for(int i = 0 ;i<horsePerformance.size();i++) {
					totalP+=horsePerformance.get(i);
				}
				double prob;
				for(int i=0;i<horsePerformance.size();i++) {
					prob=(double)horsePerformance.get(i)/totalP*100;
					horseProbabilities.put(i, prob);
				} 	
			}
				//->>>>
				List<Integer> pickHorse= new ArrayList<Integer>();
				int toPut;
				for(int i=0;i<horseProbabilities.size();i++) {
					toPut = horseProbabilities.get(i).intValue();
					System.out.println("toPut:"+toPut);
					for(int c = 0;c<toPut;c++) {
						pickHorse.add(i);
					}
				}
			
			Random random = new Random();
			int n = random.nextInt(pickHorse.size());
			this.horseId=pickHorse.get(n);  //vem de onde de fora?
			
			this.bet[0]=spectator.getID(); //spectator_id
			
			this.bet[1]= random.nextDouble()*spectator.getMoney(); //money
			System.out.println("Spectator_"+spectator.getID()+" choose horse " + horseId+" the money_" + bet[1]);
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
