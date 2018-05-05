package server;


import communication.Message;
import sharingRegions.MonitorControlCenter;

public class StakeHoldersThread extends Thread {
	protected ServerCom com;
	private MonitorControlCenter mControlCenter;
	private StakeHoldersProtocol shp;

	public StakeHoldersThread(ServerCom com, StakeHoldersProtocol shp, MonitorControlCenter mControlCenter) {
		this.com = com;
		this.mControlCenter = mControlCenter;
		this.shp = shp;
	}

	public void run() {
		Message returnMessage=null;
		/* prestacao propriamente dita do servico */
		Message messageFromClient;
		while ((messageFromClient = (Message) com.readObject()) != null) // o cliente respondeu?
		{
		   // teste de fim de comunicacao-> verificar se é para matar o servidor ou se é parar executar uma função.
			if(messageFromClient.getFunctionName()!=null) {
				if(messageFromClient.getFunctionName().equals(".EndServer")) {
					com.writeObject(new Message(true));
					System.out.println(".EndServer");
					shp.setServerOff();
					break;
				}
				returnMessage=shp.processInput(messageFromClient,mControlCenter); // geracao da mensagem seguinte
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
