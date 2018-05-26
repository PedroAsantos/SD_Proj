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
	*	Function to update the number of races that are missing.
	*
	*  @exception IOException IOException
	* 	
	*/
	public void raceStarted() throws IOException{
		sendMessage(new Message("raceStarted"));
	}
	
	/**
	*	Function to update the number of runs of a horse.
	*
	*  @exception IOException IOException
	* 	@param horse_id Horse ID.
	* 	@param runs Runs of the horse.
	*/
	public void sethorseruns(int horse_id, int runs) throws IOException{
		sendMessage(new Message("sethorseruns",new Object[] {horse_id,runs}));
	}
	
	/**
	*	Function to update the position of the horse.
	*
	*  @exception IOException IOException
	* 	@param horse_id Horse ID.
	* 	@param position Horse position.
	*/
	public void sethorseposition(int horse_id, int position) throws IOException{
		sendMessage(new Message("sethorseposition",new Object[] {horse_id,position}));
	}
	
	/**
	*	Function to update the horse place on the end of the race.
	*
	*  @exception IOException IOException
	* 	@param horse_id Spectator ID.
	* 	@param rank Horse place.
	*/
	public void sethorserank(int horse_id, int rank) throws IOException{

		sendMessage(new Message("sethorserank",new Object[] {horse_id,rank}));
	}
	
	/**
	*	Function to return the number of horses per race.
	*  @exception IOException IOException
	*   @return int Horses in each race.
	*/
	public int getHorsesPerRace() throws IOException{
		return (int) sendMessage(new Message("getHorsesPerRace")).getReturn();
	}

	/**
	*	Function to send the message with the function to execute and all the arguments
	*
	*  @exception IOException IOException
	*	@param message Message object with the message to send to the monitor.
	*	@return message the message from the monitor.
	*/
	public Message sendMessage(Message message) throws IOException{

		String hostName; // nome da máquina onde está o servidor
		Properties prop = new Properties();
		String propFileName = "config.properties";
 	
		prop.load(new FileInputStream("resources/"+propFileName));
		
		int portNumb = Integer.parseInt(prop.getProperty("portRepository"));
		//int portNumb = 9949; // número do port

		hostName = prop.getProperty("machine_repository");
		//hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
	
}