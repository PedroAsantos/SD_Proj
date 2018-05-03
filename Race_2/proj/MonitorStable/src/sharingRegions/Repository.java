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
	*	Function to add the horses that are running or they will run in the next race.
	*   @param horseId Horse ID 
	*   @return boolean return false if the race is already full.
	*/
	public boolean addHorsesToRun(int horseId) {
		if (sendMessage("addHorsesToRun"+";"+horseId).equals("true")) {
			return true;
		}else{
			return false;
		}
	}
	/**
	*	Function to return the number of horses per race.
	*   @return int Horses in each race.
	*/
	public int getHorsesPerRace() {
		int returnFunction = Integer.parseInt(sendMessage("getHorsesPerRace"));
		return returnFunction;
	}
	/**
	*	Function to return the number of total horses in the event.
	*   @return int Horses in the event.
	*/
	public int getTotalHorses() {
		int returnFunction = Integer.parseInt(sendMessage("getTotalHorses"));
		return returnFunction;
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