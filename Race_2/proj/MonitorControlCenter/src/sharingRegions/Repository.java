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
	*	Function to return the number of races that left.
	*
	*  @exception IOException IOException
	* 	@return int the number of races missing.
	*/
	public int getNumberOfRacesMissing() throws IOException{
		return (int) sendMessage(new Message("getNumberOfRacesMissing")).getReturn();
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

		//hostName = "localhost";
		hostName = prop.getProperty("machine_repository");

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
}