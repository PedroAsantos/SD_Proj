
import Interfaces.IRepository;
import Interfaces.Register;
import java.net.SocketTimeoutException;


import sharingRegions.*;
import java.util.*;
import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RunRepository {

	public static void main(String[] args) throws IOException{
		
		int portNumb;
		Properties prop = new Properties();
		String propFileName = "config.properties";
 	
		prop.load(new FileInputStream("src/resources/"+propFileName));
		
		portNumb = Integer.parseInt(prop.getProperty("portRepository")); // numero do port em que o servico ee
                String rmiRegHostName = prop.getProperty("rmiRegHostName");
                int rmiRegPortNumb = Integer.parseInt(prop.getProperty("rmiRegPortNumb"));
                String nameEntryBase = prop.getProperty("nameEntry");
                String nameEntryObject = "stubRepository";
                int numberOfHorses = 20; //testar com numeros maiores.
		int numberOfSpectators=4;
		int numberOfRaces=5;
		int horsesPerRace=4;
		int raceLength=30;
		Repository repo = new Repository(numberOfHorses,numberOfSpectators,numberOfRaces,horsesPerRace,raceLength);
		repo.writeLog();
	    
		IRepository stubRepository = null;
                Registry registry = null;
                Register reg = null;
            
                try
                {stubRepository = (IRepository) UnicastRemoteObject.exportObject(repo, portNumb);
                }
                catch (RemoteException e)
                {   System.out.println (nameEntryObject+" stub generation exception: " + e.getMessage ());
                    e.printStackTrace ();
                    System.exit (1);
                }
                
                System.out.println ("Stub was generated!");

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
                try {
                    reg.bind(nameEntryObject, stubRepository);
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

//proxy vai implementar as interfaces do monitor
//cada mensagem cria um socket
//servidor vamos ter alguem vai estar a escuta de mensagem
//no servidor vamos ter o monitor em cada processo. temos uma entidade aa escuta.
//no servidor chega um thread e ee criado um thread e este ee que chama o monitor
//os threads ficam sempre aa espera de resposta
//o enunciado pede -> os cavalos estao todos no mesmo processo e na mesma maquina.
//
