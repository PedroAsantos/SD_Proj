package sharingRegions;



import communication.Message;
import communication.Stub;
import interfaces.ISpectator_BettingCenter;


public class MonitorBettingCenter implements ISpectator_BettingCenter {
	Repository repo;


	public MonitorBettingCenter(Repository repo) {
		
		this.repo=repo;
	}
	
	/**
	*	Function to spectator place a bet.
	*
	*	@param spectatorId Spectator ID 
	*	@param money Money of the bet
	*	@param horsePicked Horse chosen by the spectator.
	*/
	@Override
	public void placeABet(int spectatorId, double money,int horsePicked) {
		sendMessage(new Message("placeABet",new Object[] {spectatorId,money,horsePicked}));
		
	}

	@Override
	public double goCollectTheGains(int spectatorId) {
		return (double) sendMessage(new Message("goCollectTheGains",new Object[] {spectatorId})).getReturn();
	}
	
	
	public Message sendMessage(Message message) {

		String hostName; // nome da maquina onde esta o servidor
		int portNumb = 9989; // numero do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicacao

		stub = new Stub(hostName, portNumb);
		return stub.exchange(message);	
	}

}
