package sharingRegions;



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
		sendMessage("placeABet"+";"+ spectatorId+";"+ money+ ";"+ horsePicked);
		
	}

	@Override
	public double goCollectTheGains(int spectatorId) {
		sendMessage("goCollectTheGains"+";"+spectatorId);
		return 0;
	}
	
	
	public String sendMessage(String payload) {

		String hostName; // nome da m�quina onde est� o servidor
		int portNumb = 9989; // n�mero do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunica��o

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}

}
