
import java.net.SocketTimeoutException;

import server.*;
import sharingRegions.*;

public class RunRepository {

	public static void main(String[] args) {
				
		int numberOfHorses = 20; //testar com numeros maiores.
		int numberOfSpectators=4;
		int numberOfRaces=5;
		int horsesPerRace=4;
		int raceLength=30;
		Repository repo = new Repository(numberOfHorses,numberOfSpectators,numberOfRaces,horsesPerRace,raceLength);
		repo.writeLog();
	    
		ServerCom scon, sconi; // canais de comunicacao
		int portNumb = 9949; // numero do port em que o servico ee
								// estabelecido
		StakeHoldersProtocol shp; // servico a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associacao
		scon.start(); // com o endereco publico
		shp = StakeHoldersProtocol.getInstance(); // activar oo servico
		System.out.println("Repository was established!");
		System.out.println("Repository is listenning.");
		
		/* processamento de pedidos */
		
		StakeHoldersThread thread; // agente prestador de servico
		while (shp.getServerState()) {
			
			sconi = scon.accept(); // entrar em processo de escuta	
			if(sconi!=null) {
				thread = new StakeHoldersThread(sconi, shp, repo); // lancar agente prestador de servico
				thread.start();	
			}
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
