package sharingRegions;

import Interfaces.IBroker_Track;
import communication.Message;
import communication.Stub;
import java.util.*;

import java.io.*;


public class MonitorRacingTrack implements IBroker_Track{
	
	/**
	*	Stub of Monitor Racing track.
	*
	*	 
	*/
	public MonitorRacingTrack() {
		
	}
	/**
	*	Waits until all horses are in starting line to start the race.
	*
	*/
	@Override
	public void startTheRace(){
		sendMessage(new Message("startTheRace"));
	}
	/**
	*	Function to broker turn off the server.
	*
	*	 
	*/
	@Override
	public void turnOffServer(){
		sendMessage(new Message(".EndServer"));
	}
	
	/**
	*	Report the results to spectators returning an array with rankings
	*
	*	@return int[] horseAWinners 
	*/
	@Override
	public int[] reportResults(){
		return (int[]) sendMessage(new Message("reportResults")).getReturn();
        
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
		
		int portNumb = Integer.parseInt(prop.getProperty("portRacingTrack"));
		//int portNumb = 9979; // número do port

		//hostName = "localhost";
		hostName = prop.getProperty("machine_RacingTrack");

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
	

	
}
