package server;


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
		String inputLine, // linha de entrada
				outputLine; // linha de saída
		String returnFunction=null;
		/* prestacao propriamente dita do serviço */

		
		while ((inputLine = (String) com.readObject()) != null) // o cliente respondeu?
		{
			
		 // teste de fim de comunicacao
			if(!inputLine.equals("Ok!")) {
				returnFunction=shp.processInput(inputLine,mPaddock); // geracao da mensagem seguinte
			}
		
			
			if(returnFunction!=null) {
				outputLine=returnFunction;
				returnFunction=null;
				com.writeObject(outputLine);
			}else {
				outputLine="Ok!";	
				com.writeObject(outputLine);// seu envio ao cliente
			} 
			if (outputLine.equals("Ok!"))
				break;	
		}
	}
}
