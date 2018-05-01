package sharingRegions;


import Interfaces.IHorse_Paddock;
import communication.Stub;


public class MonitorPaddock implements IHorse_Paddock {
	
	
	public MonitorPaddock() {

	}
	

	/**
	*	Function for horses to stay in the paddock. They are awake at the end of the spectators all have seen the horses.
	*
	*	@param horseId Horse ID 
	*	@param performance Performance of the horse
	*/
	@Override
	public void proceedToPaddock(int horseId,int performance) {	
		sendMessage("waitForNextRace"+";"+horseId+";"+performance);
	}


	public String sendMessage(String payload) {

		String hostName; // nome da máquina onde está o servidor
		int portNumb = 9988; // número do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}
}
