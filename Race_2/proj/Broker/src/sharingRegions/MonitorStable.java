package sharingRegions;


import Interfaces.IBroker_Stable;
import communication.Stub;

public class MonitorStable implements IBroker_Stable {

	public MonitorStable() {


	}

	

	/**
	 * When the event are ending the horses are going to the end of event
	 *
	 * 
	 */
	@Override
	public void summonHorsesToEnd() {
		sendMessage("summonHorsesToEnd");
	}

	/**
	 * All horses go to paddock
	 *
	 * 
	 */
	@Override
	public void summonHorsesToPaddock() {
		sendMessage("summonHorsesToPaddock");
	}

	public String sendMessage(String payload) {

		String hostName; // nome da maquina onde esta o servidor
		int portNumb = 9999; // numero do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}
	
}
