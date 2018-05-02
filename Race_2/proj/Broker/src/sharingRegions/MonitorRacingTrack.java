package sharingRegions;

import Interfaces.IBroker_Track;
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
		sendMessage("startTheRace");
	}
	

	/**
	*	Report the results to spectators returning an array with rankings
	*
	*	@return int[] horseAWinners 
	*/
	@Override
	public int[] reportResults() {
		String[] returnFunction;
		returnFunction=sendMessage("reportResults").split(",");
	
		int[] result = new int[returnFunction.length];
		for(int i=0;i<returnFunction.length;i++) {
			result[i]=Integer.parseInt(returnFunction[i]);
		}
		
        return result;
	}
	public String sendMessage(String payload) {

		String hostName; // nome da máquina onde está o servidor
		int portNumb = 9979; // número do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}
	

	
}
