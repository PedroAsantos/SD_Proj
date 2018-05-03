package sharingRegions;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;

import communication.Message;
import communication.Stub;
import Enum.*;

public class Repository {
	public Repository(){

	}
	
	/**
	*	Function to know the number of spectors in the event.
	*   @return int the number of spectators.
	*/
	public int getNumberOfSpectators() {
		return (int) sendMessage(new Message("getNumberOfSpectators")).getReturn();
	}
	/**
	*	Function to return the number of races that left.
	*
	* 	@return int the number of races missing.
	*/
	public int getNumberOfRacesMissing() {
		return (int) sendMessage(new Message("getNumberOfRacesMissing")).getReturn();
	}

	public Message sendMessage(Message message) {

		String hostName; // nome da máquina onde está o servidor
		int portNumb = 9949; // número do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
}