package sharingRegions;


import Interfaces.IBroker_BettingCenter;
import communication.Message;
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
		 sendMessage(new Message("acceptTheBets") );
	}

	@Override
	public void honourTheBets() {
		 sendMessage(new Message("honourTheBets") );
		
	}
	/**
	*	Function to broker verify if any spectator won the bet. 
	*
	*	@param horseAWinners Array with the horses that won the last race.
	*   @return boolean returns true if at least one spectator won the bet.
	*/
	@Override
	public boolean areThereAnyWinners(int[] horseAWinners) {
		return (boolean) sendMessage(new Message("areThereAnyWinners", new Object[] {horseAWinners})).getReturn();
	}
	
	@Override
	public void turnOffServer() {
		sendMessage(new Message(".EndServer"));
	}

	public Message sendMessage(Message message) {

		String hostName; // nome da maquina onde esta o servidor
		int portNumb = 9989; // numero do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
	

}
