package sharingRegions;


import Interfaces.IBroker_BettingCenter;
import communication.Message;
import communication.Stub;
import java.io.*;
import java.util.*;


public class MonitorBettingCenter implements IBroker_BettingCenter {



	public MonitorBettingCenter() {
		
	}
	/**
	*	Function to broker accept the bets of spectators.
	*
	*	 
	*/
	@Override
	public void acceptTheBets() {
		sendMessage(new Message("acceptTheBets") );
	}

	@Override
	public void honourTheBets() {
		sendMessage(new Message("honourTheBets") );
	}
	/**
	*	Function to broker verify if any spectator won the bet. 
	*
	*	@param horseAWinners Array with the horses that won the last race.
	*   @return boolean returns true if at least one spectator won the bet.
	*/
	@Override
	public boolean areThereAnyWinners(int[] horseAWinners) {
		return (boolean) sendMessage(new Message("areThereAnyWinners", new Object[] {horseAWinners})).getReturn();
	}
	
	@Override
	public void turnOffServer() {
			sendMessage(new Message(".EndServer"));
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
		
		int portNumb = Integer.parseInt(prop.getProperty("portBettingCenter"));
		//int portNumb = 9989; // número do port

		//hostName = "localhost";
		hostName = prop.getProperty("machine_BettingCenter");

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}
	

}
