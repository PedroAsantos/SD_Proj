package sharingRegions;


import Interfaces.IHorse_Paddock;
import communication.Message;
import communication.Stub;
import java.io.*;
import java.util.*;

public class MonitorPaddock implements IHorse_Paddock {
	
	
	public MonitorPaddock() {

	}
	

	/**
	*	Function for horses to stay in the paddock. They are awake at the end of the spectators all have seen the horses.
	*
	*	@param horseId Horse ID 
	*	@param performance Performance of the horse
	*/
	@Override
	public void proceedToPaddock(int horseId,int performance) {	
		sendMessage(new Message("proceedToPaddock",new Object[] {horseId,performance}));
	}


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

		hostName = prop.getProperty("machine_Paddock");
		//hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
}
