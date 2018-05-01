import server.ServerCom;
import server.StakeHoldersProtocol;
import server.StakeHoldersThread;
import sharingRegions.MonitorPaddock;
import sharingRegions.Repository;

public class RunPaddock {
	public static void main(String[] args) {
		
		int numberOfHorses = 20; //testar com numeros maiores.
		int numberOfSpectators=4;
		int numberOfRaces=5;
		int horsesPerRace=4;
		int raceLength=30;
	
		Repository repo = new Repository(numberOfHorses,numberOfSpectators,numberOfRaces,horsesPerRace,raceLength);
		repo.writeLog();

		MonitorPaddock mPaddock = new MonitorPaddock(repo);
	
		
		
		ServerCom scon, sconi; // canais de comunicação
		int portNumb = 9969; // número do port em que o serviço é
								// estabelecido
		StakeHoldersProtocol shp; // serviço a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associação
		scon.start(); // com o endereço público
		shp = StakeHoldersProtocol.getInstance(); // activar oo serviço
		System.out.println("MonitorPaddock was established!");
		System.out.println("MonitorPaddock is listenning.");


		/* processamento de pedidos */

		StakeHoldersThread thread; // agente prestador de serviço

		while (true) {
			sconi = scon.accept(); // entrar em processo de escuta
			thread = new StakeHoldersThread(sconi, shp, mPaddock); // lançar agente prestador de serviço
			thread.start();
		}
	}
}
