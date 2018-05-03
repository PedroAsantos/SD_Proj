package sharingRegions;

import Interfaces.IBroker_Track;
import communication.Message;
import communication.Stub;




public class MonitorRacingTrack implements IBroker_Track{
	

	public MonitorRacingTrack() {
		
	}
	/**
	*	Waits until all horses are in starting line to start the race.
	*
	*/
	@Override
	public void startTheRace() {
		sendMessage(new Message("startTheRace"));
	}
	

	/**
	*	Report the results to spectators returning an array with rankings
	*
	*	@return int[] horseAWinners 
	*/
	@Override
	public int[] reportResults() {
        return (int[]) sendMessage(new Message("summonHorsesToEnd")).getReturn();
	}
	public Message sendMessage(Message message) {

		String hostName; // nome da maquina onde esta o servidor
		int portNumb = 9979; // numero do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
	

	
}
