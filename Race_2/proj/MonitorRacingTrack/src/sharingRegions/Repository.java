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
	*	Function to update the number of races that are missing.
	*
	* 	
	*/
	public void raceStarted() {
		sendMessage("raceStarted");
	}
	
	/**
	*	Function to update the number of runs of a horse.
	*
	* 	@param horse_id Horse ID.
	* 	@param runs Runs of the horse.
	*/
	public void sethorseruns(int horse_id, int runs){
		sendMessage("sethorseruns"+";"+horse_id+";"+runs);
	}
	
	/**
	*	Function to update the position of the horse.
	*
	* 	@param horse_id Horse ID.
	* 	@param position Horse position.
	*/
	public void sethorseposition(int horse_id, int position){
		sendMessage("sethorseposition"+";"+horse_id+";"+position);
	}
	
	/**
	*	Function to update the horse place on the end of the race.
	*
	* 	@param horse_id Spectator ID.
	* 	@param rank Horse place.
	*/
	public void sethorserank(int horse_id, int rank){

		sendMessage("sethorserank"+";"+horse_id+";"+rank);
	}
	
	/**
	*	Function to return the number of horses per race.
	*   @return int Horses in each race.
	*/
	public int getHorsesPerRace() {
		int returnFunction = Integer.parseInt(sendMessage("getHorsesPerRace"));
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