package sharingRegions;

import Interfaces.IHorse_Stable;
import communication.Message;
import communication.Stub;


public class MonitorStable implements IHorse_Stable {

	
	public MonitorStable() {
	
		
	}
	
	/**
	*	Wait that all the horses arrives to stable to wake up the broker
	* and then a returns an boolean that indicates if a horse is going to paddock
	*
	*	@param horseId Horse ID
	*	@return boolean goingToPaddock 
	*/
	@Override
	public boolean proceedToStable(int horseId) {
		return (boolean) sendMessage(new Message("proceedToStable",new Object[] {horseId})).getReturn();
				
	}
	
	public Message sendMessage(Message message) {

		String hostName; // nome da maquina onde esta o servidor
		int portNumb = 9999; // numero do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}

}
