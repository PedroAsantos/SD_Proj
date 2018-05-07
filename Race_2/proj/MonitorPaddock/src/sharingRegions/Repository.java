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
	*  @exception IOException IOException
	*   @return int the number of spectators.
	*/
	public int getNumberOfSpectators() throws IOException{
		return (int) sendMessage(new Message("getNumberOfSpectators")).getReturn();
	}
	
	/**
	*	Function to update the probabilitie of the given horse winning.
	*
	*  @exception IOException IOException
	* 	@param horse_id Horse ID.
	* 	@param probabilitie Horse winning probabilitie.
	*/
	public void setHorseProbabilitie(int horse_id,double probabilitie) throws IOException{
		sendMessage(new Message("setHorseProbabilitie",new Object[] {horse_id,probabilitie}));
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
	*	Function to update perfomance horse.
	*
	*  @exception IOException IOException
	* 	@param horse_id int id of horse.
	* 	@param performance int perfomance horse.
	*/
	public void setHorsePerformance(int horse_id, int performance) throws IOException{
		sendMessage(new Message("setHorsePerformance",new Object[] {horse_id,performance}));
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