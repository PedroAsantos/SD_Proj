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
//	private boolean waitingForTheSpectator;
	private int numberOfBets;
	//hash map <cavalo,[espectator,money] -> assim com o contains value conseguimos ver logo se alguem ganhou e depois e facil de retirar o valor.
	private HashMap<Integer,List<double[]>> spectatorBets;
	private Condition[] receivMoneyList;
	private List<Integer> horseWinners;
	private Queue<Spectator> queueToReceivMoney;
	//index 0=horse
	//index 1=bet
	private double[] bet = new double[2];
	private int horseId;
	private boolean openHonorStand;
	private boolean brokerIsOccupied;
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
	//	waitingForTheSpectator = true;
		openHonorStand=true;
		brokerIsOccupied=false;
		spectatorBets = new HashMap<Integer,List<double[]>>(numberOfSpectators);
		receivMoneyList = new Condition[numberOfSpectators];
		for(int i=0;i<numberOfSpectators;i++) {
			receivMoneyList[i]=mutex.newCondition();
		}
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
			
			while(numberOfBets < numberOfSpectators) {
				try {
					broker_condition.await();
					//place bet
					List <double[]> listTemp = new ArrayList<double[]>();
					double[] temp = new double[2];
					temp[0]=bet[0];
					temp[1]=bet[1];
					if(spectatorBets.containsKey(horseId)) {
						listTemp = spectatorBets.get(horseId);
						listTemp.add(temp);
						spectatorBets.put(horseId,listTemp);
					}else {	
						listTemp.add(temp);
						spectatorBets.put(horseId,listTemp);
					}
					
					//System.out.println("numberofbets: " + numberOfBets);
					numberOfBets++;
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
		
			repo.setspectatorBets(spectatorBets);
			System.out.println("BROKER WAKE UP");
			
			
		} finally {
			mutex.unlock();
		}
		
		
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
			List<Integer> horsesRunning = repo.getHorsesRunning();
		//	if(horseProbabilities.size()==0) {
				int totalP=0;
				for(int i = 0 ;i<horsesRunning.size();i++) {
					totalP+=horsePerformance.get(horsesRunning.get(i));
				}
				double prob;
				for(int i=0;i<horsesRunning.size();i++) {
					prob=(double)horsePerformance.get(horsesRunning.get(i))/totalP*100;
					horseProbabilities.put(horsesRunning.get(i), prob);
				} 	
			//}

				List<Integer> pickHorse= new ArrayList<Integer>();
				int toPut;
//				System.out.println("SIze horses RUnning"+horsesRunning.size());
//   			System.out.println("SIze horses PROBS"+horseProbabilities.size());
		
				for(int i=0;i<horsesRunning.size();i++) {
					if(horseProbabilities.containsKey(horsesRunning.get(i))) {
						toPut = horseProbabilities.get(horsesRunning.get(i)).intValue();
						for(int c = 0;c<toPut;c++) {
							pickHorse.add(horsesRunning.get(i));
						}
					}
				}
			
				
			Random random = new Random();
			int n = random.nextInt(pickHorse.size());
			this.horseId=pickHorse.get(n); 
			this.bet[0]=spectator.getID(); //spectator_id
			
			this.bet[1]= random.nextDouble()*spectator.getMoney(); //money
			System.out.println("Spectator_"+spectator.getID()+" choose horse " + horseId+" the money " + bet[1]);
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
						
			//System.out.println(numberOfBets+ " " + numberOfSpectators);
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
			openHonorStand=false;
			queueToReceivMoney.add(spectator);
			//System.out.println("added to queeu: "+spectator.getID());
			while(brokerIsOccupied) {
				try {
					receivMoneyList[spectator.getID()].await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		/*	Object[] temp = queueToReceivMoney.toArray();
			for (int i = 0; i < temp.length; i++) {
				Spectator tt = (Spectator)temp[i];
				System.out.println("queueu elemtn: "+tt.getID());
			}*/
			brokerIsOccupied=true;
			broker_condition.signal();
			System.out.println("Spectator_"+spectator.getID()+" waiting to collect the gains!");
			while(receivingDividends) {
				try {
					receivMoneyList[spectator.getID()].await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			brokerIsOccupied=false;
			//PAY ATTENTO TO THIS
		
			//System.out.println("queu size"+queueToReceivMoney.size()+"->"+queueToReceivMoney.peek().getID());
			if(!queueToReceivMoney.isEmpty()) {
				receivingDividends=true;
				receivMoneyList[queueToReceivMoney.peek().getID()].signal();
			}else {
				receivingDividends=true;
			}
			///----------------------
			System.out.println("Spectator_"+spectator.getID()+" received the money!");
			repo.getNumberOfRaces();
		} finally {
			// TODO: handle finally clause
			mutex.unlock();
		}
	}
	@Override
	public void honourTheBets() {
		// TODO Auto-generated method stub
		mutex.lock();
		try {

			List<double[]> winners = new ArrayList<double[]>();
		
			for(int i=0;i<horseWinners.size();i++) {
				System.out.println("HORSEWINNERS:"+horseWinners.get(i));
				if(spectatorBets.containsKey(horseWinners.get(i))) {
					winners.addAll(spectatorBets.get(horseWinners.get(i)));
				}
			}
			for (int i = 0; i < winners.size(); i++) {
				System.out.println("Winners:"+winners.get(i)[0]);
			}
			Spectator spectatorReceivingMoney;
			int betsPayed=0;
		//	System.out.println("winnersSize: "+winners.size());
			while(betsPayed<winners.size()|| openHonorStand) {
				try {
					broker_condition.await();
					spectatorReceivingMoney=queueToReceivMoney.remove();
					for(int i=0;i<winners.size();i++) {
						if(winners.get(i)[0]==spectatorReceivingMoney.getID()) {
							//how to knoe the horse that spectator bet
							System.out.println("Spectator_"+spectatorReceivingMoney.getID()+ " is receiving "+spectatorReceivingMoney.getMoney());
							spectatorReceivingMoney.addMoney(winners.get(i)[1]*1/horseProbabilities.get(horseWinners.get(0)));
							betsPayed++;
							break;
						}
						
					}
					
					receivingDividends=false;
					receivMoneyList[spectatorReceivingMoney.getID()].signal();
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
			}
			
			openHonorStand=true;
			spectatorBets.clear();
			horseProbabilities.clear();
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



}
