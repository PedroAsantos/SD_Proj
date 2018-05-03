package sharingRegions;


import Enum.*;

public class Repository {

	//winnerHoreses
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
	*	Function to remove all the horses that were running.
	*
	*/
	public void clearhorsesRunning() {
		sendMessage("clearhorsesRunning");
	}

	/**
	*	Function to return the number of races that left.
	*
	* 	@return int the number of races missing.
	*/
	public int getNumberOfRacesMissing() {
		int returnFunction = sendMessage("getNumberOfRacesMissing");
		return returnFunction;
	}

	/**
	*	Function to update the number of races that were made.
	*	
	*/
	public void raceDone() {
		sendMessage("raceDone");
	}
	

	/**
	*	Function to update the broker state
	*
	* 	@param brokerstate state.
	* 	
	*/
	public void setbrokerstate(BrokerState brokerstate){
		sendMessage("setbrokerstate"+";"+brokerstate);
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