package server;


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
		String inputLine, // linha de entrada
				outputLine; // linha de saida
		String returnFunction=null;
		/* prestacao propriamente dita do servico */

	
		
		while ((inputLine = (String) com.readObject()) != null) // o cliente respondeu?
		{
			System.out.println("serverteste1");
		 // teste de fim de comunicacao
			if(!inputLine.equals("Ok!")) {
				returnFunction=shp.processInput(inputLine,mRacingTrack); // geracao da mensagem seguinte
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
