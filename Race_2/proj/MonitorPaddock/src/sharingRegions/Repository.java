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
	*	Function to know the number of spectors in the event.
	*   @return int the number of spectators.
	*/
	public int getNumberOfSpectators() {
		int returnFunction = Integer.parseInt(sendMessage("getNumberOfSpectators"));
		return returnFunction;
	}
	
	/**
	*	Function to update the probabilitie of the given horse winning.
	*
	* 	@param horse_id Horse ID.
	* 	@param probabilitie Horse winning probabilitie.
	*/
	public void setHorseProbabilitie(int horse_id,double probabilitie) {
		sendMessage("setHorseProbabilitie"+";"+horse_id+";"+probabilitie);
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
	public void setHorsePerformance(int horse_id, int performance) {
		sendMessage("setHorsePerformance"+";"+horse_id+";"+performance);
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