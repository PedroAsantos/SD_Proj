import Interfaces.IMonitor_Paddock;
import Interfaces.IRepository;
import Interfaces.Register;
import sharingRegions.MonitorPaddock;
import java.util.*;
import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class RunPaddock {
	public static void main(String[] args) throws IOException{

	
		Properties prop = new Properties();
		String propFileName = "config.properties";
 	
		prop.load(new FileInputStream("resources/"+propFileName));
		
		int portNumb = Integer.parseInt(prop.getProperty("portPaddock")); // numero do port em que o servico ee
		 String rmiRegHostName = prop.getProperty("10machine_Registry");
                int rmiRegPortNumb = Integer.parseInt(prop.getProperty("rmiRegPortNumb"));
                String nameEntryBase = prop.getProperty("nameEntry");
                String nameEntryObject = "stubPaddock";

                IMonitor_Paddock stubPaddock = null;
                Registry registry = null;
                Register reg = null;
                IRepository repo = null;
                try {
                    registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
                } catch (RemoteException e) {
                    System.out.println("RMI registry creation exception: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }

                System.out.println("RMI registry was created!");

                try {
                    reg = (Register) registry.lookup(nameEntryBase);
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
                   repo = (IRepository) registry.lookup("stubRepository");
                 } catch (RemoteException e) {
                    System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                } catch (NotBoundException e) {
                    System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }
                
                MonitorPaddock mPaddock = new MonitorPaddock(repo);
	

                try {
                    stubPaddock = (IMonitor_Paddock) UnicastRemoteObject.exportObject(mPaddock, portNumb);
                } catch (RemoteException e) {
                    System.out.println(nameEntryObject + " stub generation exception: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }
                System.out.println("Stub was generated!");

                try {
                    reg.bind(nameEntryObject, stubPaddock);
                } catch (RemoteException e) {
                    System.out.println(nameEntryObject + " registration exception: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                } catch (AlreadyBoundException e) {
                    System.out.println(nameEntryObject + " already bound exception: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }
                System.out.println(nameEntryObject + " object was registered!");

	}
}
