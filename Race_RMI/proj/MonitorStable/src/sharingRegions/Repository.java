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
	*	Function to add the horses that are running or they will run in the next race.
	*  @exception IOException IOException
	*   @param horseId Horse ID 
	*   @return boolean return false if the race is already full.
	*/
	public boolean addHorsesToRun(int horseId) throws IOException{
		return (boolean) sendMessage(new Message("addHorsesToRun",new Object[] {horseId})).getReturn();
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
	*	Function to return the number of total horses in the event.
	*  @exception IOException IOException
	*   @return int Horses in the event.
	*/
	public int getTotalHorses() throws IOException{
		
		return (int) sendMessage(new Message("getTotalHorses")).getReturn();
	}
	/**
	*	Function to send the message with the function to execute and all the arguments
	*
	*  @exception IOException IOException
	*	@param message Message object with the message to send to the monitor.
	*	@return message the message from the monitor.
	*/
	public Message sendMessage(Message message) throws IOException{

		/*String hostName; // nome da máquina onde está o servidor
		Properties prop = new Properties();
		String propFileName = "config.properties";
 	
		prop.load(new FileInputStream("src/resources/"+propFileName));
		
		int portNumb = Integer.parseInt(prop.getProperty("portRepository"));
		//int portNumb = 9949; // número do port
		
		hostName = prop.getProperty("machine_repository");
		//hostName = "localhost";

		/* troca de mensagens com o servidor */
/*
		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
*/
            return null;
        }
}