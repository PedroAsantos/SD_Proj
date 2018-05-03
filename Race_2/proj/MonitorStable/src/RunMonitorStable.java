import server.ServerCom;
import server.StakeHoldersProtocol;
import server.StakeHoldersThread;
import sharingRegions.*;

public class RunMonitorStable {
	static final int PORT = 9999;

	public static void main(String[] args) {

		int numberOfHorses = 20; // testar com numeros maiores.
		int numberOfSpectators = 4;
		int numberOfRaces = 5;
		int horsesPerRace = 4;
		int raceLength = 30;
		Repository repo = new Repository();

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
		ServerCom scon, sconi; // canais de comunicacao
		int portNumb = 9999; // numero do port em que o servico ee
								// estabelecido
		StakeHoldersProtocol shp; // servico a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associacao
		scon.start(); // com o endereco publico
		shp = StakeHoldersProtocol.getInstance(); // activar oo servico
		System.out.println("MonitorStable was established!");
		System.out.println("MonitorStable is listenning.");

		/* processamento de pedidos */

		StakeHoldersThread thread; // agente prestador de servico

		while (true) {
			sconi = scon.accept(); // entrar em processo de escuta
			thread = new StakeHoldersThread(sconi, shp, mStable); // lancar agente prestador de servico
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
