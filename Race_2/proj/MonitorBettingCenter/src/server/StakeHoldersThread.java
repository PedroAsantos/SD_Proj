package server;


import sharingRegions.MonitorBettingCenter;

public class StakeHoldersThread extends Thread {
	protected ServerCom com;
	private MonitorBettingCenter mBettingCenter;
	private StakeHoldersProtocol shp;

	public StakeHoldersThread(ServerCom com, StakeHoldersProtocol shp, MonitorBettingCenter mBettingCenter) {
		this.com = com;
		this.mBettingCenter = mBettingCenter;
		this.shp = shp;
	}

	public void run() {
		String inputLine, // linha de entrada
				outputLine; // linha de saida
		String returnFunction=null;
		/* prestacao propriamente dita do servico */

		while ((inputLine = (String) com.readObject()) != null) // o cliente respondeu?
		{
			System.out.println("serverteste1");
		 // teste de fim de comunicacao
			if(!inputLine.equals("Ok!")) {
				returnFunction=shp.processInput(inputLine,mBettingCenter); // geracao da mensagem seguinte
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
