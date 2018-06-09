import Interfaces.IMonitor_Control;
import Interfaces.IRepository;
import Interfaces.Register;
import sharingRegions.MonitorControlCenter;
import java.util.*;
import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RunControlCenter {
	static final int PORT = 9998;

	public static void main(String[] args) throws IOException{
		
		Properties prop = new Properties();
		String propFileName = "config.properties";
 	
		prop.load(new FileInputStream("resources/"+propFileName));
		
		int portNumb = Integer.parseInt(prop.getProperty("portControlCenter")); // numero do port em que o servico ee

		 String rmiRegHostName = prop.getProperty("rmiRegHostName");
                int rmiRegPortNumb = Integer.parseInt(prop.getProperty("rmiRegPortNumb"));
                String nameEntryBase = prop.getProperty("2machine_ControlCenter");
                String nameEntryObject = "stubControl";

                IMonitor_Control stubControl = null;
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
                
             

		MonitorControlCenter mControlCenter = new MonitorControlCenter(repo);
	
	

                try {
                    stubControl = (IMonitor_Control) UnicastRemoteObject.exportObject(mControlCenter, portNumb);
                } catch (RemoteException e) {
                    System.out.println(nameEntryObject + " stub generation exception: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }
                System.out.println("Stub was generated!");

                try {
                    reg.bind(nameEntryObject, stubControl);
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
