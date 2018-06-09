import sharingRegions.*;
import java.util.*;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import Interfaces.*;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
public class RunMonitorStable {

	public static void main(String[] args) throws IOException, NotBoundException, AlreadyBoundException{
           
            Properties prop = new Properties();
            String propFileName = "config.properties";
          /*  System.out.println("Working Directory = " +
              System.getProperty("user.dir"));*/
            prop.load(new FileInputStream("resources/"+propFileName));
               
            int portNumb = Integer.parseInt(prop.getProperty("portStable")); // numero do port em que o servico ee
            String rmiRegHostName = prop.getProperty("rmiRegHostName"); 
            int rmiRegPortNumb = Integer.parseInt(prop.getProperty("rmiRegPortNumb")); 	
            String nameEntryBase = prop.getProperty("6machine_Stable"); 	
            String nameEntryObject = "stubStable";
          
            IMonitor_Stable stubStable = null;
            Registry registry = null;
            Register reg = null;
            
            try {
                registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            } catch (RemoteException e) {
                System.out.println("RMI registry creation exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
             
            System.out.println("RMI registry was created!");

            try {
               reg = (Register) registry.lookup (nameEntryBase);
            } catch (RemoteException e) {
                System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            } catch (NotBoundException e) {
                System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
            
            IRepository repo =(IRepository) registry.lookup("stubRepository");
            MonitorStable mStable = new MonitorStable(repo);
            
            try
            { stubStable= (IMonitor_Stable) UnicastRemoteObject.exportObject(mStable, portNumb);
            }
            catch (RemoteException e)
            {   System.out.println (nameEntryObject+" stub generation exception: " + e.getMessage ());
                e.printStackTrace ();
                System.exit (1);
            }
            System.out.println ("Stub was generated!");
            
            
            try {
                reg.bind(nameEntryObject, stubStable);
            } catch (RemoteException e) {
                System.out.println(nameEntryObject+" registration exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            } catch (AlreadyBoundException e) {
                System.out.println(nameEntryObject+" already bound exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
            System.out.println(nameEntryObject+" object was registered!");
             
            
	}

}
// proxy vai implementar as interfaces do monitor
// cada mensagem cria um socket
// servidor vamos ter alguem vai estar a escuta de mensagem
// no servidor vamos ter o monitor em cada processo. temos uma entidade aa
// escuta.
// no servidor chega um thread e ee criado um thread e este ee que chama o
// monitor
// os threads ficam sempre aa espera de resposta
// o enunciado pede -> os cavalos estao todos no mesmo processo e na mesma
// maquina.
//
