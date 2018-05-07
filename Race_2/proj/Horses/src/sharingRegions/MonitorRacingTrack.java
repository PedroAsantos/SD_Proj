package sharingRegions;

import Interfaces.IHorse_Track;
import communication.Message;
import communication.Stub;
import java.io.*;
import java.util.*;


public class MonitorRacingTrack implements IHorse_Track{

	

	public MonitorRacingTrack() {

	}
	
	
	/**
	*	Send horses to start line and then when all horses to race are at start line 
	*	the broker is woken up.
	*
	*	@param horseId Horse ID
	*	@param performance Horse performance
	*/
	@Override
	public void proceedToStartLine(int horseId,int performance) {
	
		sendMessage(new Message("proceedToStartLine",new Object[] {horseId,performance}));

	}
	/**
	*	Horse make a move with the value random up to his max performance
	*
	*	@param horseId Horse ID
	*/
	@Override
	public void makeAMove(int horseId) {
		sendMessage(new Message("makeAMove",new Object[] {horseId}));
	}
	/**
	*	Check if a horse has crossed the finish line and then 
	*	if all horses already finished the broker is woken up 
	* 	else continue to run.
	*
	*	@param horseId Horse ID
	*   @return boolean Returns true if the line has been crossed by the horse. 
	*/
	@Override
	public boolean hasFinishLineBeenCrossed(int horseId) {
		return (boolean) sendMessage(new Message("hasFinishLineBeenCrossed",new Object[] {horseId})).getReturn();
		
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
		
		int portNumb = Integer.parseInt(prop.getProperty("portRacingTrack"));
		//int portNumb = 9979; // numero do port

		hostName = prop.getProperty("machine_RacingTrack");
		//hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}

	
}
