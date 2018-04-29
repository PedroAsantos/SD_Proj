import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Interfaces.IBroker_BettingCenter;
import Interfaces.IBroker_Control;
import Interfaces.IBroker_Stable;
import Interfaces.IBroker_Track;
import Interfaces.IHorse_Paddock;
import Interfaces.IHorse_Stable;
import Interfaces.IHorse_Track;
import Interfaces.ISpectator_BettingCenter;
import Interfaces.ISpectator_Control;
import Interfaces.ISpectator_Paddock;
import sharingRegions.*;


public class RunMonitorStable {
    static final int PORT = 9996;

	public static void main(String[] args) {
				
		int numberOfHorses = 20; //testar com numeros maiores.
		int numberOfSpectators=4;
		int numberOfRaces=5;
		int horsesPerRace=4;
		int raceLength=30;
		int maxPerformance=10;
		Repository repo = new Repository(numberOfHorses,numberOfSpectators,numberOfRaces,horsesPerRace,raceLength);
		repo.writeLog();

		MonitorStable mStable = new MonitorStable(repo);
		MonitorBettingCenter mBettingCenter = new MonitorBettingCenter(repo);
		MonitorControlCenter mControlCenter = new MonitorControlCenter(repo);
		MonitorPaddock mPaddock = new MonitorPaddock(repo);
		MonitorRacingTrack mRacingTrack = new MonitorRacingTrack(raceLength, repo);
	
		 ServerSocket serverSocket = null;
	        
		 Socket socket = null;

	     try {
	    	 serverSocket = new ServerSocket(PORT);
	     } catch (IOException e) {
	         e.printStackTrace();

	     }
	     while (true) {
	    	 try {
	    		 socket = serverSocket.accept();
	    	 } catch (IOException e) {
	             System.out.println("I/O error: " + e);
	         }
	            // new thread for a client
	    	 new EchoThread(socket,mStable).start();
	     }
	
	}
		
		
}
//proxy vai implementar as interfaces do monitor
//cada mensagem cria um socket
//servidor vamos ter alguem vai estar a escuta de mensagem
//no servidor vamos ter o monitor em cada processo. temos uma entidade aa escuta.
//no servidor chega um thread e ee criado um thread e este ee que chama o monitor
//os threads ficam sempre aa espera de resposta
//o enunciado pede -> os cavalos estao todos no mesmo processo e na mesma maquina.
//
