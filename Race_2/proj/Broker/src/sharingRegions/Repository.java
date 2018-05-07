package sharingRegions;

import communication.Message;
import communication.Stub;
import Enum.*;
import java.util.*;

import java.io.*;

public class Repository {

	/**
	*	Stub of Repository
	*
	*	 
	*/
	public Repository(){
		
	}

	/**
	*	Function to write in the log files the update values.
	*
	*/
	public void toLog(){
		sendMessage(new Message("toLog"));
	}


	/**
	*	Function to remove all the horses that were running.
	*
	*/
	public void clearhorsesRunning(){
		sendMessage(new Message("clearhorsesRunning"));
	}

	/**
	*	Function to return the number of races that left.
	*
	* 	@return int the number of races missing.
	*/
	public int getNumberOfRacesMissing() {
		return (int) sendMessage(new Message("getNumberOfRacesMissing")).getReturn();
	}

	/**
	*	Function to update the number of races that were made.
	*	
	*/
	public void raceDone(){
		sendMessage(new Message("raceDone"));
	}
	

	/**
	*	Function to update the broker state
	*
	* 	@param brokerstate state.
	* 	
	*/
	public void setbrokerstate(BrokerState brokerstate){
		sendMessage(new Message("setbrokerstate", new Object[] {brokerstate}));
	}
	/**
	*	Function to broker turn off the server.
	*
	*	 
	*/
	public void turnOffServer(){
		sendMessage(new Message(".EndServer"));
	}
	/**
	*	Function to send the message with the function to execute and all the arguments
	*
	*	@param message Message object with the message to send to the monitor.
	*	@return message the message from the monitor.
	*/
	public Message sendMessage(Message message){

		String hostName; // nome da máquina onde está o servidor
		Properties prop = new Properties();
		String propFileName = "config.properties";
 	
		try {
			prop.load(new FileInputStream("resources/"+propFileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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