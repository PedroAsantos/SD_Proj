package server;


import communication.Message;
import sharingRegions.MonitorPaddock;

public class StakeHoldersThread extends Thread {
	protected ServerCom com;
	private MonitorPaddock mPaddock;
	private StakeHoldersProtocol shp;

	public StakeHoldersThread(ServerCom com, StakeHoldersProtocol shp, MonitorPaddock mPaddock) {
		this.com = com;
		this.mPaddock = mPaddock;
		this.shp = shp;
	}

	public void run() {
		Message returnMessage=null;
		/* prestacao propriamente dita do servico */
		Message messageFromClient;
	//	Message messageToClient=null;
		while ((messageFromClient = (Message) com.readObject()) != null) // o cliente respondeu?
		{
		 // teste de fim de comunicacao
			if(messageFromClient.getFunctionName()!=null) {
				returnMessage=shp.processInput(messageFromClient,mPaddock); // geracao da mensagem seguinte
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
