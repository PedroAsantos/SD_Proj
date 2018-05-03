package sharingRegions;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;

import communication.Stub;
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
	*	Function to update the state of the spectator_id.
	*
	* 	@param spectator_id Spectator ID.
	* 	@param state Spectator State
	*/
	public void setSpecStat(int spectator_id, SpectatorState state){
		sendMessage("setSpecStat"+";"+spectator_id+";"+state);
	}

	/**
	*	Function to update the money that a spectator has.
	*
	* 	@param spectator_id Spectator ID.
	* 	@param money Money of the specator.
	*/
	public void setspecMoney(int spectator_id, double money){
		sendMessage("setspecMoney"+";"+spectator_id+";"+money);
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