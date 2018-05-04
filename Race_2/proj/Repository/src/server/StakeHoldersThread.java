package server;


import communication.Message;
import sharingRegions.Repository;

public class StakeHoldersThread extends Thread {
	protected ServerCom com;
	private Repository repo;
	private StakeHoldersProtocol shp;

	public StakeHoldersThread(ServerCom com, StakeHoldersProtocol shp, Repository repo) {
		this.com = com;
		this.repo = repo;
		this.shp = shp;
	}

	public void run() {
		Message returnMessage=null;
		/* prestacao propriamente dita do servico */
		Message messageFromClient;
		while ((messageFromClient = (Message) com.readObject()) != null) // o cliente respondeu?
		{
		 // teste de fim de comunicacao
			if(messageFromClient.getFunctionName()!=null) {
				returnMessage=shp.processInput(messageFromClient,repo); // geracao da mensagem seguinte
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
