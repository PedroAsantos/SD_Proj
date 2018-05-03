package sharingRegions;


import Interfaces.IBroker_BettingCenter;
import communication.Stub;



public class MonitorBettingCenter implements IBroker_BettingCenter {



	public MonitorBettingCenter() {
		
	}
	/**
	*	Function to broker accept the bets of spectators.
	*
	*	 
	*/
	@Override
	public void acceptTheBets() {
		sendMessage("acceptTheBets");
	}

	@Override
	public void honourTheBets() {
		sendMessage("honourTheBets");
	}
	/**
	*	Function to broker verify if any spectator won the bet. 
	*
	*	@param horseAWinners Array with the horses that won the last race.
	*   @return boolean returns true if at least one spectator won the bet.
	*/
	@Override
	public boolean areThereAnyWinners(int[] horseAWinners) {
		String array="";
		for(int i=0;i<horseAWinners.length;i++) {
			array+=Integer.toString(horseAWinners[i]);
			if(i<horseAWinners.length-1) {
				array+=",";
			}
		}
		if(horseAWinners.length==1) {
			array+=",";
		}
		if(sendMessage("areThereAnyWinners"+";"+array).equals("true")) {
			return true;
		}else {
			return false;
		}
	}

	public String sendMessage(String payload) {

		String hostName; // nome da maquina onde esta o servidor
		int portNumb = 9989; // numero do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}
	

}
