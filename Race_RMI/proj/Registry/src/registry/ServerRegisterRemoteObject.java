/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registry;

import Interfaces.Register;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

/**
 *
 * @author rute
 */
public class ServerRegisterRemoteObject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        
        Properties prop = new Properties();
        String propFileName = "config.properties";
        
        prop.load(new FileInputStream("resources/"+propFileName));
                 
        String rmiRegHostName = prop.getProperty("10machine_Registry"); 
        int rmiRegPortNumb = Integer.parseInt(prop.getProperty("rmiRegPortNumb")); 
        
        RegisterRemoteObject regEngine = new RegisterRemoteObject (rmiRegHostName, rmiRegPortNumb);
        Register regEngineStub = null;
        
        int listeningPort = 22399;                            /* it should be set accordingly in each case */

        try
        { regEngineStub = (Register) UnicastRemoteObject.exportObject (regEngine, listeningPort);
        }
        catch (RemoteException e)
        { System.out.println ("RegisterRemoteObject stub generation exception: " + e.getMessage ());
            System.exit (1);
        }
        System.out.println ("Stub was generated!");
        String nameEntry = "RegisterHandler";
        Registry registry = null;

        try
        { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { System.out.println ("RMI registry creation exception: " + e.getMessage ());
          System.exit (1);
        }
        System.out.println ("RMI registry was created!");

        try
        { registry.rebind (nameEntry, regEngineStub);
        }
        catch (RemoteException e)
        { System.out.println ("RegisterRemoteObject remote exception on registration: " + e.getMessage ());
          System.exit (1);
        }
        System.out.println ("RegisterRemoteObject object was registered!");
    }
    
}
