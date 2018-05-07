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
	*	Function to update the state of the horse.
	*
	* 	@param horse_id Horse ID.
	* 	@param state Horse state.
	*/
	public void setHorseStat(int horse_id, HorseState state){
		sendMessage(new Message("setHorseStat",new Object[] {horse_id,state})).getReturn();
	
	}
	/**
	*	Function to send the message with the function to execute and all the arguments
	*
	*	@param message Message object with the message to send to the monitor.
	*	@return message the message from the monitor.
	*/
	public Message sendMessage(Message message) {

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