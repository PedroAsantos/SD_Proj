package sharingRegions;

import Interfaces.IBroker_Paddock;
import communication.Message;
import communication.Stub;
import java.io.*;
import java.util.*;

public class MonitorPaddock implements IBroker_Paddock{
	
	/**
	*	Stub of Monitor Paddock
	*
	*	 
	*/
	public MonitorPaddock() {
		
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
	public Message sendMessage(Message message) {

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
		
		int portNumb = Integer.parseInt(prop.getProperty("portPaddock"));
		//int portNumb = 9969; // numero do port

		//hostName = "localhost";
		hostName = prop.getProperty("machine_Paddock");
		
		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
}
