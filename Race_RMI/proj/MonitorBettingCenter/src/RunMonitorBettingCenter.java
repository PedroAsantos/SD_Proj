import server.ServerCom;
import server.StakeHoldersProtocol;
import server.StakeHoldersThread;
import sharingRegions.*;
import java.util.*;
import java.io.*;

public class RunMonitorBettingCenter {

	public static void main(String[] args) throws IOException{
				

		Repository repo = new Repository();
		repo.writeLog();

		MonitorBettingCenter mBettingCenter = new MonitorBettingCenter(repo);

		Properties prop = new Properties();
		String propFileName = "config.properties";
 	
		prop.load(new FileInputStream("resources/"+propFileName));
		
		int portNumb = Integer.parseInt(prop.getProperty("portBettingCenter")); // numero do port em que o servico ee
		ServerCom scon, sconi; // canais de comunicacao
		//int portNumb = 9989; // numero do port em que o servico ee
								// estabelecido
		StakeHoldersProtocol shp; // servico a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associacao
		scon.start(); // com o endereco publico
		shp = StakeHoldersProtocol.getInstance(); // activar oo servico
		System.out.println("MonitorBettingCenter was established!");
		System.out.println("MonitorBettingCenter is listenning.");

		/* processamento de pedidos */

		StakeHoldersThread thread; // agente prestador de servico

		while (shp.getServerState()) {
			sconi = scon.accept(); // entrar em processo de escuta
			if(sconi!=null) {
				thread = new StakeHoldersThread(sconi, shp, mBettingCenter); // lancar agente prestador de servico
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
