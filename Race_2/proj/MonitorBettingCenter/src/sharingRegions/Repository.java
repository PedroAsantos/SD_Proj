package sharingRegions;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;

import communication.Stub;
import Enum.*;

public class Repository {
	
	public Repository(){

	}
	
	public void writeLog(){
		sendMessage("writeLog");
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
	*	Function to update the money that a spectator put on a bet.
	*
	* 	@param spectator_id Spectator ID.
	* 	@param amount bet amount.
	*/
	public void setspecbetamount(int spectator_id, double amount){
		sendMessage("setspecbetamount"+";"+spectator_id+";"+amount);
	}

	/**
	*	Function to update the horse that a given spectator bet.
	*
	* 	@param spec_id Spectator ID.
	* 	@param horse_id Horse picked.
	*/
	public void setspecbets(int spec_id,int horse_id){
		sendMessage("setspecbets"+";"+spec_id+";"+horse_id);
	}

	/**
	*	Function to clear the horse place on the end of the race.
	*
	*/
	public void clearhorserank() {
		sendMessage("clearhorserank");
		
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