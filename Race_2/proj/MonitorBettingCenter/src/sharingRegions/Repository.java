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
	
	public void writeLog(){
		sendMessage(new Message("writeLog"));
	}
	/**
	*	Function to know the number of spectors in the event.
	*   @return int the number of spectators.
	*/
	public int getNumberOfSpectators() {
		return (int) sendMessage(new Message("getNumberOfSpectators")).getReturn();
	}
	/**
	*	Function to update the money that a spectator put on a bet.
	*
	* 	@param spectator_id Spectator ID.
	* 	@param amount bet amount.
	*/
	public void setspecbetamount(int spectator_id, double amount){
		sendMessage(new Message("setspecbetamount",new Object[] {spectator_id, amount}));
	}

	/**
	*	Function to update the horse that a given spectator bet.
	*
	* 	@param spec_id Spectator ID.
	* 	@param horse_id Horse picked.
	*/
	public void setspecbets(int spec_id,int horse_id){
		sendMessage(new Message("setspecbets",new Object[] {spec_id, horse_id}));
	}

	/**
	*	Function to clear the horse place on the end of the race.
	*
	*/
	public void clearhorserank() {
		sendMessage(new Message("clearhorserank"));
		
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