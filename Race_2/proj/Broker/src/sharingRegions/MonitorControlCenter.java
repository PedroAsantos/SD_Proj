package sharingRegions;

import Interfaces.IBroker_Control;
import communication.Message;
import communication.Stub;


//import com.sun.corba.se.pept.broker.Broker;

public class MonitorControlCenter implements IBroker_Control{

	public MonitorControlCenter() {
	
	}
	
	
	/**
	*	Function for broker report the horses that won the last race.
	*
	*	@param horseAWinners Array with the horses that won the last race.
	*/
	@Override
	public void reportResults(int[] horseAWinners) {
		sendMessage(new Message("reportResults",new Object[]{horseAWinners}));
	}

	/**
	*	Function for broker wait for spectators.
	*
	*	
	*/
	@Override
	public void entertainTheGuests() {
		sendMessage(new Message("entertainTheGuests"));
		
	}
	
	public Message sendMessage(Message message) {

		String hostName; // nome da maquina onde esta o servidor
		int portNumb = 9959; // numero do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
}
