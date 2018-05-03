package server;


import communication.Message;
import sharingRegions.MonitorStable;

public class StakeHoldersThread extends Thread {
	protected ServerCom com;
	private MonitorStable mStable;
	private StakeHoldersProtocol shp;

	public StakeHoldersThread(ServerCom com, StakeHoldersProtocol shp, MonitorStable mStable) {
		this.com = com;
		this.mStable = mStable;
		this.shp = shp;
	}

	public void run() {
		String inputLine, // linha de entrada
				outputLine; // linha de saida
		Message returnMessage=null;
		/* prestacao propriamente dita do servico */
		Message messageFromClient;
	//	Message messageToClient=null;
		while ((messageFromClient = (Message) com.readObject()) != null) // o cliente respondeu?
		{
			System.out.println("serverteste1");
		 // teste de fim de comunicacao
			if(messageFromClient.getFunctionName()!=null) {
				returnMessage=shp.processInput(messageFromClient,mStable); // geracao da mensagem seguinte
			}
		
			
			if(returnMessage!=null) {
				//outputLine="asd";
				com.writeObject(returnMessage);
				returnMessage=null;
			}else {
			//	outputLine="Ok!";	
				com.writeObject(new Message());// seu envio ao cliente
				break;
			} 
		//	if (outputLine.equals("Ok!"))
		//		break;	
		}
	}
}
