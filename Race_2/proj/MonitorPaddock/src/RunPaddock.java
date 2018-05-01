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
	
		
		
		ServerCom scon, sconi; // canais de comunica��o
		int portNumb = 9969; // n�mero do port em que o servi�o �
								// estabelecido
		StakeHoldersProtocol shp; // servi�o a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associa��o
		scon.start(); // com o endere�o p�blico
		shp = StakeHoldersProtocol.getInstance(); // activar oo servi�o
		System.out.println("MonitorPaddock was established!");
		System.out.println("MonitorPaddock is listenning.");


		/* processamento de pedidos */

		StakeHoldersThread thread; // agente prestador de servi�o

		while (true) {
			sconi = scon.accept(); // entrar em processo de escuta
			thread = new StakeHoldersThread(sconi, shp, mPaddock); // lan�ar agente prestador de servi�o
			thread.start();
		}
	}
}
