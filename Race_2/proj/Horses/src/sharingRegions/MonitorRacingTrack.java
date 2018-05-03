package sharingRegions;

import Interfaces.IHorse_Track;
import communication.Message;
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
	
		sendMessage(new Message("proceedToStartLine",new Object[] {horseId,performance}));

	}
	/**
	*	Horse make a move with the value random up to his max performance
	*
	*	@param horseId Horse ID
	*/
	@Override
	public void makeAMove(int horseId) {
		sendMessage(new Message("makeAMove",new Object[] {horseId}));
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
		return (boolean) sendMessage(new Message("hasFinishLineBeenCrossed",new Object[] {horseId})).getReturn();
		
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
