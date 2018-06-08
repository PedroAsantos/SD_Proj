package sharingRegions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


import Interfaces.IMonitor_BettingCenter;
import Interfaces.IRepository;
import Interfaces.Register;
import java.io.FileInputStream;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;


public class MonitorBettingCenter implements IMonitor_BettingCenter {
	private final ReentrantLock mutex;
	private final Condition spectator_condition;
	private final Condition broker_condition;
	private final Condition spectatorWaiting_condition;
	private final int numberOfSpectators;
	private boolean spectatorsQueueBol;
	private boolean spectatorBetAproving;
	private boolean receivingDividends;
	private int numberOfBets;
	//hash map <cavalo,[espectator,money] -> assim com o contains value conseguimos ver logo se alguem ganhou e depois e facil de retirar o valor.
	private HashMap<Integer,List<double[]>> spectatorBets;
	private Condition[] receivMoneyList;
	private List<Integer> horseWinners;
	private Queue<Integer> queueToReceivMoney;
	private double[] bet = new double[2];
	private int horseId;
	private double moneyReceived;
	private boolean brokerIsOccupied;
	private int spectReceiving;
	private boolean brokerCanNotAccept;
	IRepository repo;

	public MonitorBettingCenter(IRepository repo) throws IOException {
		mutex = new ReentrantLock(true);
		spectator_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		spectatorWaiting_condition = mutex.newCondition();
		this.numberOfSpectators = repo.getNumberOfSpectators();
		numberOfBets=0;
		spectatorBetAproving=true;
		spectatorsQueueBol=false;
		receivingDividends=true;
		brokerIsOccupied=false;
		spectatorBets = new HashMap<Integer,List<double[]>>(numberOfSpectators);
		receivMoneyList = new Condition[numberOfSpectators];
		for(int i=0;i<numberOfSpectators;i++) {
			receivMoneyList[i]=mutex.newCondition();
		}
		queueToReceivMoney = new LinkedList<Integer>();
		moneyReceived=0;
		spectReceiving=0;
		brokerCanNotAccept=true;
		this.repo=repo;
	}
	/**
	*	Function to broker accept the bets of spectators.
	*
	*
	*/
	@Override
	public void acceptTheBets() {

		mutex.lock();
		try {
			System.out.println("ACCEPTING BETS BROKER");

			while(numberOfBets < numberOfSpectators) {
				try {
					while(brokerCanNotAccept) {
						broker_condition.await();
					}
					//place bet
					List <double[]> listTemp = new ArrayList<double[]>();
					double[] temp = new double[2];
					temp[0]=bet[0];
					temp[1]=bet[1];
					if(spectatorBets.containsKey(horseId)) {
						listTemp = spectatorBets.get(horseId);
						listTemp.add(temp);
						spectatorBets.put(horseId,listTemp);
						repo.setspecbets((int)bet[0],horseId);
					}else {
						listTemp.add(temp);
						spectatorBets.put(horseId,listTemp);
						repo.setspecbets((int)bet[0],horseId);
					}
					//repo.setspectatorBets(spectatorBets);
					numberOfBets++;
					spectatorBetAproving=false;
					brokerCanNotAccept=true;
					spectator_condition.signal();

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			numberOfBets=0;
			spectatorsQueueBol=false;

			//repo.setspectatorBets(spectatorBets);
			System.out.println("BROKER WAKE UP");


		} finally {
			mutex.unlock();
		}


	}
	/**
	*	Function to spectator place a bet.
	*
	*	@param spectatorId Spectator ID
	*	@param money Money of the bet
	*	@param horsePicked Horse chosen by the spectator.
	*/
	@Override
	public void placeABet(int spectatorId, double money,int horsePicked) {
		mutex.lock();
		try {

			while(spectatorsQueueBol) {
				try {
					spectatorWaiting_condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			spectatorsQueueBol=true;
			System.out.println("Spectator_"+spectatorId+" is betting!");

			this.horseId=horsePicked;
			this.bet[0]=spectatorId; //spectator_id
			this.bet[1]=money; //money
			//spectator.payBet(this.bet[1]);
			System.out.println("Spectator_"+spectatorId+" choose horse " + horseId+ " the money " + bet[1]);
			repo.setspecbetamount(spectatorId,bet[1]);
			brokerCanNotAccept=false;
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

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			mutex.unlock();

		}

	}
	/**
	*	Function to spectator collect the gains if he won the bet.
	*
	*	@param spectatorId Spectator ID
	*	@return double the money that the spectator won.
	*/
	@Override
	public double goCollectTheGains(int spectatorId) {
		// TODO Auto-generated method stub
		//jogar com o facto de a ordem de chegada e igual a de  saida FIFO. E dar assim a saida deles.registando a entrada

		mutex.lock();
		try {
			//openHonorStand=false;
			queueToReceivMoney.add(spectatorId);
			System.out.println("Spectator_"+spectatorId+" waiting to collect the gains!");
			while(brokerIsOccupied||queueToReceivMoney.peek()!=spectatorId) {
				try {
					receivMoneyList[spectatorId].await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			spectReceiving++;
			brokerIsOccupied=true;
			broker_condition.signal();
			System.out.println("Spectator_"+spectatorId+" turn to collect the gains!");

			while(receivingDividends) {
				try {
					receivMoneyList[spectatorId].await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//PAY ATTENTO TO THIS

			//System.out.println("queu size"+queueToReceivMoney.size()+"->"+queueToReceivMoney.peek().getID());
			brokerIsOccupied=false;
			receivingDividends=true;
			if(!queueToReceivMoney.isEmpty()) {
				receivMoneyList[queueToReceivMoney.peek()].signal();
			}


			///----------------------
			System.out.println("Spectator_"+spectatorId+" received the money!");
			//repo.getNumberOfRaces();
		} finally {
			mutex.unlock();
		}
		return moneyReceived;
	}
	/**
	*	Function for the broker be able to pay the winning bets.
	*
	*
	*/
	@Override
	public void honourTheBets() {
		mutex.lock();
		try {

			List<double[]> winners = new ArrayList<double[]>();

			for(int i=0;i<horseWinners.size();i++) {
				System.out.println("HORSE WINNERS:"+horseWinners.get(i));
				if(spectatorBets.containsKey(horseWinners.get(i))) {
					winners.addAll(spectatorBets.get(horseWinners.get(i)));
				}
			}
		/*	for (int i = 0; i < winners.size(); i++) {
				System.out.println("Winners:"+winners.get(i)[0]);
			}*/
			int spectatorReceivingMoney;
			int betsPayed=0;

			while(betsPayed<winners.size()/*|| openHonorStand*/) {
				try {
					while(spectReceiving==0) {
						broker_condition.await();
					}
					spectatorReceivingMoney=queueToReceivMoney.remove();
					for(int i=0;i<winners.size();i++) {
						if(winners.get(i)[0]==spectatorReceivingMoney) {
							//how to know the horse that spectator bet
							moneyReceived=winners.get(i)[1]*2;
							System.out.println("Spectator_"+spectatorReceivingMoney+ " is receiving "+moneyReceived);
							//spectatorReceivingMoney.addMoney(winners.get(i)[1]*1/horseProbabilities.get(horseWinners.get(0)));
							//ver isto.
							//repo.setspecMoney(spectatorReceivingMoney,spectatorReceivingMoney);
							betsPayed++;
							break;
						}

					}

					receivingDividends=false;
					spectReceiving--;
					receivMoneyList[spectatorReceivingMoney].signal();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			//openHonorStand=true;
			spectatorBets.clear();
			repo.clearhorserank();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			mutex.unlock();
		}

	}
	/**
	*	Function to broker verify if any spectator won the bet.
	*
	*	@param horseAWinners Array with the horses that won the last race.
	*   @return boolean returns true if at least one spectator won the bet.
	*/
	@Override
	public boolean areThereAnyWinners(int[] horseAWinners) {
		boolean thereAreWinner=false;
		mutex.lock();
		try {
			this.horseWinners = new ArrayList<>();
			for(int i = 0;i<horseAWinners.length;i++) {
				horseWinners.add(horseAWinners[i]);
			}
			for(int i = 0;i < horseWinners.size();i++) {
				if(spectatorBets.containsKey(horseWinners.get(i))) {
					thereAreWinner=true;
				}
			}
			if(!thereAreWinner) {
				spectatorBets.clear();
				repo.clearhorserank();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			mutex.unlock();
		}
		return thereAreWinner;
	}

        @Override
        public void signalShutdown() throws RemoteException, IOException {
            Register reg = null;
            Registry registry = null;

            Properties prop = new Properties();
            String propFileName = "config.properties";

            prop.load(new FileInputStream("resources/"+propFileName));

						String rmiRegHostName = prop.getProperty("rmiRegHostName");
						int rmiRegPortNumb = Integer.parseInt(prop.getProperty("rmiRegPortNumb"));

            try {
                registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            } catch (RemoteException ex) {
                System.out.println("Erro ao localizar o registo");
                ex.printStackTrace();
                System.exit(1);
            }

            String nameEntryBase = prop.getProperty("nameEntry");
            String nameEntryObject = "stubBettingCenter";


            try {
                reg = (Register) registry.lookup(nameEntryBase);
            } catch (RemoteException e) {
                System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            } catch (NotBoundException e) {
                System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
            try {
                // Unregister ourself
                reg.unbind(nameEntryObject);
            } catch (RemoteException e) {
                System.out.println("stubBettingCenter Registration exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            } catch (NotBoundException e) {
                System.out.println("stubBettingCenter Not bound exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }

            try {
                // Unexport; this will also remove us from the RMI runtime
                UnicastRemoteObject.unexportObject(this, true);
            } catch (NoSuchObjectException ex) {
                ex.printStackTrace();
                System.exit(1);
            }

            System.out.println("Betting Center closed.");
        }

}
