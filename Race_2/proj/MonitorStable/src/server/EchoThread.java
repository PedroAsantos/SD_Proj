package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import sharingRegions.MonitorStable;

public class EchoThread extends Thread {
    protected Socket socket;
    private MonitorStable mStable;
    public EchoThread(Socket clientSocket, MonitorStable mStable) {
        this.socket = clientSocket;
        this.mStable=mStable;
    }

    public void run() {
        PrintWriter out= null;
    	BufferedReader in = null;
    	try {
    		 out = new PrintWriter(socket.getOutputStream(), true);
        	 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	}catch (Exception e) {
			// TODO: handle exception
		}
        	
        		    
        	 String inputLine, outputLine;
             
        	    // Initiate conversation with client
        	
        //	    outputLine = "Started_Conversation";
        //	    out.println(outputLine);
            
        
	        String line;
	        try {
	        	inputLine  = in.readLine(); 
			/*	while ((inputLine  = in.readLine())!= null) {
					System.out.println("line"+inputLine);
					// outputLine = inputLine;
				    //   out.println(outputLine);
				        if (outputLine.equals("Bye."))
				            break;
				}*/
				
				switch (inputLine) {
				case "summonHorsesToPaddock":
					System.out.println("worked");
					mStable.summonHorsesToPaddock();
					out.println("OK!");
					break;

				default:
					break;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     
    }
}

