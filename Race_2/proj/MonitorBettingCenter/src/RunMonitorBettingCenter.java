import java.util.Random;

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
import server.ServerCom;
import server.StakeHoldersProtocol;
import server.StakeHoldersThread;
import sharingRegions.*;


public class RunMonitorBettingCenter {

	public static void main(String[] args) {
				
		int numberOfHorses = 20; //testar com numeros maiores.
		int numberOfSpectators=4;
		int numberOfRaces=5;
		int horsesPerRace=4;
		int raceLength=30;
		Repository repo = new Repository(numberOfHorses,numberOfSpectators,numberOfRaces,horsesPerRace,raceLength);
		repo.writeLog();

		MonitorBettingCenter mBettingCenter = new MonitorBettingCenter(repo);

		
		ServerCom scon, sconi; // canais de comunica��o
		int portNumb = 9989; // n�mero do port em que o servi�o �
								// estabelecido
		StakeHoldersProtocol shp; // servi�o a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associa��o
		scon.start(); // com o endere�o p�blico
		shp = StakeHoldersProtocol.getInstance(); // activar oo servi�o
		System.out.println("MonitorBettingCenter was established!");
		System.out.println("MonitorBettingCenter is listenning.");

		/* processamento de pedidos */

		StakeHoldersThread thread; // agente prestador de servi�o

		while (true) {
			sconi = scon.accept(); // entrar em processo de escuta
			thread = new StakeHoldersThread(sconi, shp, mBettingCenter); // lan�ar agente prestador de servi�o
			thread.start();
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
