import server.ServerCom;
import server.StakeHoldersProtocol;
import server.StakeHoldersThread;
import sharingRegions.MonitorPaddock;
import sharingRegions.Repository;
import java.util.*;
import java.io.*;


public class RunPaddock {
	public static void main(String[] args) throws IOException{

		Repository repo = new Repository();

		MonitorPaddock mPaddock = new MonitorPaddock(repo);
	
		Properties prop = new Properties();
		String propFileName = "config.properties";
 	
		prop.load(new FileInputStream("resources/"+propFileName));
		
		int portNumb = Integer.parseInt(prop.getProperty("portPaddock")); // numero do port em que o servico ee
		
		ServerCom scon, sconi; // canais de comunicacao
		//int portNumb = 9969; // numero do port em que o servico e
								// estabelecido
		StakeHoldersProtocol shp; // servico a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associacao
		scon.start(); // com o endereco publico
		shp = StakeHoldersProtocol.getInstance(); // activar oo servico
		System.out.println("MonitorPaddock was established!");
		System.out.println("MonitorPaddock is listenning.");


		/* processamento de pedidos */

		StakeHoldersThread thread; // agente prestador de servico

		while (shp.getServerState()) {
			sconi = scon.accept(); // entrar em processo de escuta
			if(sconi!=null) {
				thread = new StakeHoldersThread(sconi, shp, mPaddock); // lancar agente prestador de servico
				thread.start();	
			}
		}
	}
}
