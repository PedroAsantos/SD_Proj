import server.ServerCom;
import server.StakeHoldersProtocol;
import server.StakeHoldersThread;
import sharingRegions.MonitorControlCenter;
import sharingRegions.Repository;
import java.util.*;
import java.io.*;

public class RunControlCenter {
	static final int PORT = 9998;

	public static void main(String[] args) throws IOException{


		Repository repo = new Repository();
		

		MonitorControlCenter mControlCenter = new MonitorControlCenter(repo);
	
		Properties prop = new Properties();
		String propFileName = "config.properties";
 	
		prop.load(new FileInputStream("resources/"+propFileName));
		
		int portNumb = Integer.parseInt(prop.getProperty("portControlCenter")); // numero do port em que o servico ee

		ServerCom scon, sconi; // canais de comunicacao
		//int portNumb = 9959; // numero do port em que o servico e
								// estabelecido
		StakeHoldersProtocol shp; // servico a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associacao
		scon.start(); // com o endereco publico
		shp = StakeHoldersProtocol.getInstance(); // activar oo servico
		System.out.println("MonitorControlCenter was established!");
		System.out.println("MonitorControlCenter is listenning.");

		/* processamento de pedidos */

		StakeHoldersThread thread; // agente prestador de servico

		while (shp.getServerState()) {
			sconi = scon.accept(); // entrar em processo de escuta
			if(sconi!=null) {
				thread = new StakeHoldersThread(sconi, shp, mControlCenter); // lancar agente prestador de servico
				thread.start();	
			}
		}
	}
}
