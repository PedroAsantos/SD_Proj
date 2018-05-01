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
				outputLine; // linha de saída
		String returnFunction=null;
		/* prestação propriamente dita do serviço */

	//	outputLine = shp.processInput(null); // geração da primeira mensagem
		//com.writeObject(outputLine); // seu envio ao cliente
		System.out.println("serverteste0");
		
		while ((inputLine = (String) com.readObject()) != null) // o cliente respondeu?
		{
			System.out.println("serverteste1");
		 // teste de fim de comunicação
			if(!inputLine.equals("Ok!")) {
				returnFunction=shp.processInput(inputLine,mBettingCenter); // geração da mensagem seguinte
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
