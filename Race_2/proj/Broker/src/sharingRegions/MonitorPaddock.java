package sharingRegions;

import Interfaces.IBroker_Paddock;
import communication.Message;
import communication.Stub;

public class MonitorPaddock implements IBroker_Paddock{


	public MonitorPaddock() {
		
	}
	
	@Override
	public void turnOffServer() {
		sendMessage(new Message(".EndServer"));
	}
	
	public Message sendMessage(Message message) {

		String hostName; // nome da maquina onde esta o servidor
		int portNumb = 9969; // numero do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
}
