package sharingRegions;

import Interfaces.IHorse_Track;
import communication.Stub;




public class MonitorRacingTrack implements IHorse_Track{

	

	public MonitorRacingTrack() {

	}
	
	
	/**
	*	Send horses to start line and then when all horses to race are at start line 
	*	the broker is woken up.
	*
	*	@param horseId Horse ID
	*	@param performance Horse performance
	*/
	@Override
	public void proceedToStartLine(int horseId,int performance) {
	
		sendMessage("proceedToStartLine"+";"+horseId+";"+performance);

	}
	/**
	*	Horse make a move with the value random up to his max performance
	*
	*	@param horseId Horse ID
	*/
	@Override
	public void makeAMove(int horseId) {
		sendMessage("makeAMove"+";"+horseId);
	}
	/**
	*	Check if a horse has crossed the finish line and then 
	*	if all horses already finished the broker is woken up 
	* 	else continue to run.
	*
	*	@param horseId Horse ID
	*   @return boolean Returns true if the line has been crossed by the horse. 
	*/
	@Override
	public boolean hasFinishLineBeenCrossed(int horseId) {
		if(sendMessage("hasFinishLineBeenCrossed"+";"+horseId).equals("true")) {
			return true;
		}
		return false;
		
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
