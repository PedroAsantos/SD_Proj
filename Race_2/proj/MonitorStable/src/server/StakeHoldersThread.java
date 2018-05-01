package server;


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
				outputLine; // linha de sa�da
		String returnFunction=null;
		/* presta��o propriamente dita do servi�o */

	//	outputLine = shp.processInput(null); // gera��o da primeira mensagem
		//com.writeObject(outputLine); // seu envio ao cliente
		System.out.println("serverteste0");
		
		while ((inputLine = (String) com.readObject()) != null) // o cliente respondeu?
		{
			System.out.println("serverteste1");
		 // teste de fim de comunica��o
			if(!inputLine.equals("Ok!")) {
				returnFunction=shp.processInput(inputLine,mStable); // gera��o da mensagem seguinte
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
