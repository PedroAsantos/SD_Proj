package sharingRegions;



import Enum.*;

public class Repository {

	public Repository(){

	}

	/**
	*	Function to write in the log files the update values.
	*
	*/
	public void toLog(){
		sendMessage("toLog");
	}

	/**
	*	Function to update the state of the horse.
	*
	* 	@param horse_id Horse ID.
	* 	@param state Horse state.
	*/
	public void setHorseStat(int horse_id, HorseState state){
		sendMessage("setHorseStat"+";"+horse_id+";"+state);
	
	}

	public String sendMessage(String payload) {

		String hostName; // nome da máquina onde está o servidor
		int portNumb = 9949; // número do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}

	
}