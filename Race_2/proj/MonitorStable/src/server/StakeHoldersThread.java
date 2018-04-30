package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
				outputLine; // linha de saída
		String returnFunction;
		/* prestação propriamente dita do serviço */

	//	outputLine = shp.processInput(null); // geração da primeira mensagem
		//com.writeObject(outputLine); // seu envio ao cliente
		System.out.println("serverteste0");
		
		while ((inputLine = (String) com.readObject()) != null) // o cliente respondeu?
		{
			System.out.println("serverteste1");
			if (inputLine.equals("Ok!"))
				break; // teste de fim de comunicação
			returnFunction=shp.processInput(inputLine,mStable); // geração da mensagem seguinte
			if(returnFunction!=null) {
				com.writeObject(returnFunction);
			}else {
				outputLine="Ok!";	
				com.writeObject(outputLine);// seu envio ao cliente
			}
			 
			
		}
		
	}
}
