package sharingRegions;

import communication.Message;
import communication.Stub;
import Enum.*;
import java.util.*;
import java.io.*;

public class Repository {

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
	*	Function to update the state of the spectator_id.
	*
	* 	@param spectator_id Spectator ID.
	* 	@param state Spectator State
	*/
	public void setSpecStat(int spectator_id, SpectatorState state){
		sendMessage(new Message("setSpecStat",new Object[] {spectator_id,state}));
	}

	/**
	*	Function to update the money that a spectator has.
	*
	* 	@param spectator_id Spectator ID.
	* 	@param money Money of the specator.
	*/
	public void setspecMoney(int spectator_id, double money){
		sendMessage(new Message("setspecMoney",new Object[] {spectator_id,money}));
	}

	/**
	*	Function to send the message with the function to execute and all the arguments
	*
	*	@param message Message object with the message to send to the monitor.
	*	@return message the message from the monitor.
	*/
	public Message sendMessage(Message message) {

		String hostName; // nome da máquina onde está o servidor
		//int portNumb = 9949; // número do port

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
		hostName = prop.getProperty("machine_repository");
		//hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
	
}