package sharingRegions;


import communication.Stub;
import interfaces.ISpectator_Control;

//import com.sun.corba.se.pept.broker.Broker;

public class MonitorControlCenter implements ISpectator_Control{

	Repository repo;

	public MonitorControlCenter(Repository repo) {
		this.repo=repo;
	}
	
	/**
	*	Function for spectators to be able to watch the race. In the end of the race they are waken up.
	*
	*	@param spectator_id Spectator ID 
	*/
	
	/**
	*	Function for spectators know if they won the bet. 
	*
	*	@param horsePicked horsePicked ID 
	*	@return boolean Returns true if the spectator won the bet.
	*/
	@Override
	public boolean haveIwon(int spectator_id, int horsePicked) {
		if(sendMessage("haveIwon"+";"+spectator_id+";"+horsePicked).equals("true")) {
			return true;
		}
		return false;
		
	}
	/**
	*	Function for spectators know if there are more races.
	*
	*	@return boolean Returns true if there are no more Races.
	*/
	@Override
	public boolean noMoreRaces() {
		if(sendMessage("noMoreRaces").equals("true")) {
			return true;
		}
		return false;
		
	}
	
	/**
	*	Function for spectators relax at the end of the race.
	*
	*	@param spectator_id Spectator ID.
	*/
	@Override
	public void relaxABit(int spectator_id) {
		sendMessage("relaxABit"+";"+spectator_id);
	}

	@Override
	public void goWatchTheRace(int spectator_id) {
		sendMessage("goWatchTheRace"+";"+spectator_id);
		
	}
	
	public String sendMessage(String payload) {

		String hostName; // nome da máquina onde está o servidor
		int portNumb = 9959; // número do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}

}
