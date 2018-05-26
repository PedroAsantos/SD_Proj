package sharingRegions;

import Interfaces.IBroker_Control;
import communication.Message;
import communication.Stub;
import java.io.*;
import java.util.*;


//import com.sun.corba.se.pept.broker.Broker;

public class MonitorControlCenter implements IBroker_Control{

	public MonitorControlCenter() {
	
	}
	
	
	/**
	*	Function for broker report the horses that won the last race.
	*
	*	@param horseAWinners Array with the horses that won the last race.
	*/
	@Override
	public void reportResults(int[] horseAWinners){
		sendMessage(new Message("reportResults",new Object[]{horseAWinners}));
	}

	/**
	*	Function for broker wait for spectators.
	*
	*	
	*/
	@Override
	public void entertainTheGuests(){
		sendMessage(new Message("entertainTheGuests"));
	}
	/**
	*	Function to broker turn off the server.
	*
	*	 
	*/
	@Override
	public void turnOffServer() {
		sendMessage(new Message(".EndServer"));
	}
	/**
	*	Function to send the message with the function to execute and all the arguments
	*
	*	@param message Message object with the message to send to the monitor.
	*	@return message the message from the monitor.
	*/
	public Message sendMessage(Message message){

		String hostName; // nome da maquina onde esta o servidor
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
		
		int portNumb = Integer.parseInt(prop.getProperty("portControlCenter"));
		//int portNumb = 9959; // número do port

		//hostName = "localhost";
		hostName = prop.getProperty("machine_ControlCenter");

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
}
