package sharingRegions;

import Interfaces.IBroker_Control;
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
		String array="";
		for(int i=0;i<horseAWinners.length;i++) {
			array+=Integer.toString(horseAWinners[i]);
			if(i<horseAWinners.length-1) {
				array+=",";
			}
		}
		sendMessage("reportResults"+";"+array);		
	}

	/**
	*	Function for broker wait for spectators.
	*
	*	
	*/
	@Override
	public void entertainTheGuests() {
		sendMessage("entertainTheGuests");
	}
	
	public String sendMessage(String payload) {

		String hostName; // nome da máquina onde está o servidor
		int portNumb = 9991; // número do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}
}
