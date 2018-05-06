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
	
	public void writeLog() throws IOException{
		sendMessage(new Message("writeLog"));
	}
	/**
	*	Function to know the number of spectors in the event.
	*   @return int the number of spectators.
	*/
	public int getNumberOfSpectators() throws IOException{
		return (int) sendMessage(new Message("getNumberOfSpectators")).getReturn();
	}
	/**
	*	Function to update the money that a spectator put on a bet.
	*
	* 	@param spectator_id Spectator ID.
	* 	@param amount bet amount.
	*/
	public void setspecbetamount(int spectator_id, double amount)throws IOException{
		sendMessage(new Message("setspecbetamount",new Object[] {spectator_id, amount}));
	}

	/**
	*	Function to update the horse that a given spectator bet.
	*
	* 	@param spec_id Spectator ID.
	* 	@param horse_id Horse picked.
	*/
	public void setspecbets(int spec_id,int horse_id) throws IOException{
		sendMessage(new Message("setspecbets",new Object[] {spec_id, horse_id}));
	}

	/**
	*	Function to clear the horse place on the end of the race.
	*
	*/
	public void clearhorserank() throws IOException{
		sendMessage(new Message("clearhorserank"));
		
	}

	public Message sendMessage(Message message) throws IOException{

		String hostName; // nome da máquina onde está o servidor
		Properties prop = new Properties();
		String propFileName = "config.properties";
 	
		prop.load(new FileInputStream("resources/"+propFileName));
		
		int portNumb = Integer.parseInt(prop.getProperty("portRepository"));
		//int portNumb = 9949; // número do port

		//hostName = "localhost";
		hostName = prop.getProperty("machine_repository");

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
	
}