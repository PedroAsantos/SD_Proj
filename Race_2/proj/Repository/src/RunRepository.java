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
import sharingRegions.*;
import stakeholders.*;

public class RunRepository {

	public static void main(String[] args) {
				
		int numberOfHorses = 2000; //testar com numeros maiores.
		int numberOfSpectators=4;
		int numberOfRaces=500;
		int horsesPerRace=4;
		int raceLength=30;
		int maxPerformance=10;
		Repository repo = new Repository(numberOfHorses,numberOfSpectators,numberOfRaces,horsesPerRace,raceLength);
		repo.writeLog();
	    
		ServerCom scon, sconi; // canais de comunicação
		int portNumb = 9989; // número do port em que o serviço é
								// estabelecido
		StakeHoldersProtocol shp; // serviço a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associação
		scon.start(); // com o endereço público
		shp = StakeHoldersProtocol.getInstance(); // activar oo serviço
		System.out.println("Repository was established!");
		System.out.println("Repository is listenning.");

		/* processamento de pedidos */

		StakeHoldersThread thread; // agente prestador de serviço

		while (true) {
			sconi = scon.accept(); // entrar em processo de escuta
			thread = new StakeHoldersThread(sconi, shp, repo); // lançar agente prestador de serviço
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
