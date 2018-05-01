package sharingRegions;

import Interfaces.IHorse_Stable;
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
		String returnFunction;
		returnFunction=sendMessage("proceedToStable"+";"+horseId);
		
		if(returnFunction.equals("true")) {
			return true;
		}else {
			return false;
		}
		
				
	}
	
	public String sendMessage(String payload) {

		String hostName; // nome da máquina onde está o servidor
		int portNumb = 9998; // número do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}

}
