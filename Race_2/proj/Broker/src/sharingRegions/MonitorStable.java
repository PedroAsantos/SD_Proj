package sharingRegions;


import Interfaces.IBroker_Stable;
import communication.Message;
import communication.Stub;

public class MonitorStable implements IBroker_Stable {

	public MonitorStable() {


	}

	@Override
	public void turnOffServer() {
		sendMessage(new Message(".EndServer"));
	}
	/**
	 * When the event are ending the horses are going to the end of event
	 *
	 * 
	 */
	@Override
	public void summonHorsesToEnd() {
		sendMessage(new Message("summonHorsesToEnd"));
	}

	/**
	 * All horses go to paddock
	 *
	 * 
	 */
	@Override
	public void summonHorsesToPaddock() {
		sendMessage(new Message("summonHorsesToPaddock"));
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
