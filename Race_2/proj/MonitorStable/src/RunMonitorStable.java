import server.ServerCom;
import server.StakeHoldersProtocol;
import server.StakeHoldersThread;
import sharingRegions.*;

public class RunMonitorStable {
	static final int PORT = 9998;

	public static void main(String[] args) {

		int numberOfHorses = 20; // testar com numeros maiores.
		int numberOfSpectators = 4;
		int numberOfRaces = 5;
		int horsesPerRace = 4;
		int raceLength = 30;
		Repository repo = new Repository(numberOfHorses, numberOfSpectators, numberOfRaces, horsesPerRace, raceLength);
		repo.writeLog();

		MonitorStable mStable = new MonitorStable(repo);
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
		int portNumb = 9999; // n�mero do port em que o servi�o �
								// estabelecido
		StakeHoldersProtocol shp; // servi�o a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associa��o
		scon.start(); // com o endere�o p�blico
		shp = StakeHoldersProtocol.getInstance(); // activar oo servi�o
		System.out.println("MonitorStable was established!");
		System.out.println("MonitorStable is listenning.");

		/* processamento de pedidos */

		StakeHoldersThread thread; // agente prestador de servi�o

		while (true) {
			sconi = scon.accept(); // entrar em processo de escuta
			thread = new StakeHoldersThread(sconi, shp, mStable); // lan�ar agente prestador de servi�o
			thread.start();
		}
	}

}
// proxy vai implementar as interfaces do monitor
// cada mensagem cria um socket
// servidor vamos ter alguem vai estar a escuta de mensagem
// no servidor vamos ter o monitor em cada processo. temos uma entidade aa
// escuta.
// no servidor chega um thread e ee criado um thread e este ee que chama o
// monitor
// os threads ficam sempre aa espera de resposta
// o enunciado pede -> os cavalos estao todos no mesmo processo e na mesma
// maquina.
//
