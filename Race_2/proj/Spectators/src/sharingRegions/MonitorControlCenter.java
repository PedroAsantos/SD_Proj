package sharingRegions;


import communication.Message;
import communication.Stub;
import interfaces.ISpectator_Control;
import java.util.*;
import java.io.*;


//import com.sun.corba.se.pept.broker.Broker;

public class MonitorControlCenter implements ISpectator_Control{

	Repository repo;

	public MonitorControlCenter(Repository repo) {
		this.repo=repo;
	}
	
	/**
	*	Function for spectators to be able to watch the race. In the end of the race they are waken up.
	*
	*	@param spectator_id Spectator ID 
	*/
	
	/**
	*	Function for spectators know if they won the bet. 
	*
	*	@param horsePicked horsePicked ID 
	*	@return boolean Returns true if the spectator won the bet.
	*/
	@Override
	public boolean haveIwon(int spectator_id, int horsePicked) {
		return (boolean) sendMessage(new Message("haveIwon",new Object[] {spectator_id,horsePicked})).getReturn();
		
	}
	/**
	*	Function for spectators know if there are more races.
	*
	*	@return boolean Returns true if there are no more Races.
	*/
	@Override
	public boolean noMoreRaces() {
		return (boolean) sendMessage(new Message("noMoreRaces")).getReturn();
		
	}
	
	/**
	*	Function for spectators relax at the end of the race.
	*
	*	@param spectator_id Spectator ID.
	*/
	@Override
	public void relaxABit(int spectator_id) {
		sendMessage(new Message("relaxABit",new Object[] {spectator_id}));
	}

	@Override
	public void goWatchTheRace(int spectator_id) {
		sendMessage(new Message("goWatchTheRace",new Object[] {spectator_id}));
		
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
		
		int portNumb = Integer.parseInt(prop.getProperty("portControlCenter"));

		//int portNumb = 9959; // numero do port

		//hostName = "localhost";
		hostName = prop.getProperty("machine_ControlCenter");

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}

}
