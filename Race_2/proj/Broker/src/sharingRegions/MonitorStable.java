package sharingRegions;


import Interfaces.IBroker_Stable;
import communication.Message;
import communication.Stub;
import java.util.*;

import java.io.*;

public class MonitorStable implements IBroker_Stable {
	/**
	*	Stub of Monitor Stable
	*
	*	 
	*/
	public MonitorStable() {


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
	 * When the event are ending the horses are going to the end of event
	 *
	 * 
	 */
	@Override
	public void summonHorsesToEnd() {
		sendMessage(new Message("summonHorsesToEnd"));
	}

	/**
	 * All horses go to paddock
	 *
	 * 
	 */
	@Override
	public void summonHorsesToPaddock(){
		sendMessage(new Message("summonHorsesToPaddock"));		
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
 		int portNumb;
 		
			try {
				prop.load(new FileInputStream("resources/"+propFileName));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			portNumb = Integer.parseInt(prop.getProperty("portStable"));
			//int portNumb = 9999; // numero do port

			hostName = prop.getProperty("machine_Stable");
			//hostName = "localhost";
		
		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
	
}
