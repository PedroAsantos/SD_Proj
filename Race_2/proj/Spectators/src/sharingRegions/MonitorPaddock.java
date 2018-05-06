package sharingRegions;

import communication.Message;
import communication.Stub;
import interfaces.ISpectator_Paddock;
import java.util.*;
import java.io.*;


public class MonitorPaddock implements  ISpectator_Paddock {

	Repository repo;
	
	public MonitorPaddock(Repository repo) {
		this.repo = repo;
	}
	

	
	/**
	*	Function for spectators to be able to wait for the horses to be in the paddock.
	*
	*	@param spectator_id Spectator ID 
	*/
	@Override
	public void waitForNextRace(int spectator_id) {
		sendMessage(new Message("waitForNextRace",new Object[] {spectator_id}));
	}
	/**
	*	Function for spectators to be able to see and choose the horse that they will bet. In this function the probabilities of each horse winning are calculated.
	*
	*	@param spectator_id Spectator ID 
	*	@return int the id of the chosen horse.
	*/
	@Override
	public int goCheckHorses(int spectator_id) {

		return (int) sendMessage(new Message("goCheckHorses",new Object[] {spectator_id})).getReturn();
		
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
