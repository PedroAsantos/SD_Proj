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
		if(sendMessage("summonHorsesToPaddock").equals("true")) {
			return true;
		}else {
			return false;
		}
	}

	public String sendMessage(String payload) {

		String hostName; // nome da m�quina onde est� o servidor
		int portNumb = 9989; // n�mero do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunica��o

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}
	

}
