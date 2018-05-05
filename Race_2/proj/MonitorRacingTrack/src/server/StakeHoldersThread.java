package server;


import communication.Message;
import sharingRegions.MonitorRacingTrack;

public class StakeHoldersThread extends Thread {
	protected ServerCom com;
	private MonitorRacingTrack mRacingTrack;
	private StakeHoldersProtocol shp;

	public StakeHoldersThread(ServerCom com, StakeHoldersProtocol shp, MonitorRacingTrack mRacingTrack) {
		this.com = com;
		this.mRacingTrack = mRacingTrack;
		this.shp = shp;
	}

	public void run() {
		Message returnMessage=null;
		/* prestacao propriamente dita do servico */
		Message messageFromClient;
		while ((messageFromClient = (Message) com.readObject()) != null) // o cliente respondeu?
		{
			// teste de fim de comunicacao-> verificar se ee para matar o servidor ou se ee parar executar uma funcao.
			if(messageFromClient.getFunctionName()!=null) {
				if(messageFromClient.getFunctionName().equals(".EndServer")) {
					com.writeObject(new Message(true));
					System.out.println(".EndServer");
					shp.setServerOff();
					break;
				}
				returnMessage=shp.processInput(messageFromClient,mRacingTrack); // geracao da mensagem seguinte
			}
		
			
			if(returnMessage!=null) {
				com.writeObject(returnMessage);
				returnMessage=null;
			}else {
				com.writeObject(new Message(true));// seu envio ao cliente
				break;
			} 
		}
	}
}
