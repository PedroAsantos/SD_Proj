package sharingRegions;

import communication.Stub;
import interfaces.ISpectator_Paddock;


public class MonitorPaddock implements  ISpectator_Paddock {

	Repository repo;
	
	public MonitorPaddock(Repository repo) {
		this.repo = repo;
	}
	

	
	/**
	*	Function for spectators to be able to wait for the horses to be in the paddock.
	*
	*	@param spectator_id Spectator ID 
	*/
	@Override
	public void waitForNextRace(int spectator_id) {
		sendMessage("waitForNextRace"+";"+spectator_id);
		
	}
	/**
	*	Function for spectators to be able to see and choose the horse that they will bet. In this function the probabilities of each horse winning are calculated.
	*
	*	@param spectator_id Spectator ID 
	*	@return int the id of the chosen horse.
	*/
	@Override
	public int goCheckHorses(int spectator_id) {

		return Integer.parseInt(sendMessage("goCheckHorses"+";"+spectator_id));
		
	}
	
	public String sendMessage(String payload) {

		String hostName; // nome da maquina onde esta o servidor
		int portNumb = 9969; // numero do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}
	
}