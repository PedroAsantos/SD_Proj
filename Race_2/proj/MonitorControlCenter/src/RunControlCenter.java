import server.ServerCom;
import server.StakeHoldersProtocol;
import server.StakeHoldersThread;
import sharingRegions.MonitorControlCenter;
import sharingRegions.Repository;

public class RunControlCenter {
	static final int PORT = 9998;

	public static void main(String[] args) {

		int numberOfHorses = 20; // testar com numeros maiores.
		int numberOfSpectators = 4;
		int numberOfRaces = 5;
		int horsesPerRace = 4;
		int raceLength = 30;
		Repository repo = new Repository(numberOfHorses, numberOfSpectators, numberOfRaces, horsesPerRace, raceLength);
		repo.writeLog();

		MonitorControlCenter mControlCenter = new MonitorControlCenter(repo);
		/*
		 * ServerSocket serverSocket = null;
		 * 
		 * Socket socket = null;
		 * 
		 * try { serverSocket = new ServerSocket(PORT); } catch (IOException e) {
		 * e.printStackTrace();
		 * 
		 * } while (true) { try { socket = serverSocket.accept(); } catch (IOException
		 * e) { System.out.println("I/O error: " + e); } // new thread for a client new
		 * EchoThread(socket,mStable).start(); }
		 */
		ServerCom scon, sconi; // canais de comunica��o
		int portNumb = 9959; // n�mero do port em que o servi�o �
								// estabelecido
		StakeHoldersProtocol shp; // servi�o a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associa��o
		scon.start(); // com o endere�o p�blico
		shp = StakeHoldersProtocol.getInstance(); // activar oo servi�o
		System.out.println("MonitorControlCenter was established!");
		System.out.println("MonitorControlCenter is listenning.");

		/* processamento de pedidos */

		StakeHoldersThread thread; // agente prestador de servi�o

		while (true) {
			sconi = scon.accept(); // entrar em processo de escuta
			thread = new StakeHoldersThread(sconi, shp, mControlCenter); // lan�ar agente prestador de servi�o
			thread.start();
		}
	}
}
