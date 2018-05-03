package server;


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
		String inputLine, // linha de entrada
				outputLine; // linha de saida
		String returnFunction=null;
		/* prestacao propriamente dita do servico */

		while ((inputLine = (String) com.readObject()) != null) // o cliente respondeu?
		{
			System.out.println("serverteste1");
		 // teste de fim de comunicacao
			if(!inputLine.equals("Ok!")) {
				returnFunction=shp.processInput(inputLine,repo); // geracao da mensagem seguinte
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
