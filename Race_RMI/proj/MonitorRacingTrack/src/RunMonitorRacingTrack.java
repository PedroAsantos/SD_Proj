import server.ServerCom;
import server.StakeHoldersProtocol;
import server.StakeHoldersThread;
import sharingRegions.MonitorRacingTrack;
import sharingRegions.Repository;
import java.util.*;
import java.io.*;


public class RunMonitorRacingTrack {
	public static void main(String[] args) throws IOException{
		
		int raceLength=30;
		Repository repo = new Repository();

		Properties prop = new Properties();
		String propFileName = "config.properties";
 	
		prop.load(new FileInputStream("resources/"+propFileName));
		
		int portNumb = Integer.parseInt(prop.getProperty("portRacingTrack")); // numero do port em que o servico ee

		MonitorRacingTrack mRacingTrack = new MonitorRacingTrack(raceLength, repo);
	
		ServerCom scon, sconi; // canais de comunicacao
		//int portNumb = 9979; // numero do port em que o servico ee
								// estabelecido
		StakeHoldersProtocol shp; // servico a ser fornecido

		scon = new ServerCom(portNumb); // criar um canal de escuta e sua associacao
		scon.start(); // com o endereco publico
		shp = StakeHoldersProtocol.getInstance(); // activar oo servico
		System.out.println("MonitorRacingTrack was established!");
		System.out.println("MonitorRacingTrack is listenning.");


		/* processamento de pedidos */

		StakeHoldersThread thread; // agente prestador de servico

		while (shp.getServerState()) {
			sconi = scon.accept(); // entrar em processo de escuta
			if(sconi!=null) {
				thread = new StakeHoldersThread(sconi, shp, mRacingTrack); // lancar agente prestador de servico
				thread.start();	
			}
		}
	}
}
