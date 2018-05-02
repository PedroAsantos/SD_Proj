package server;


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
		String inputLine, // linha de entrada
				outputLine; // linha de saída
		String returnFunction=null;
		/* prestacao propriamente dita do serviço */

		
		while ((inputLine = (String) com.readObject()) != null) // o cliente respondeu?
		{
			System.out.println("serverteste1");
		 // teste de fim de comunicacao
			if(!inputLine.equals("Ok!")) {
				returnFunction=shp.processInput(inputLine,mControlCenter); // geracao da mensagem seguinte
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
