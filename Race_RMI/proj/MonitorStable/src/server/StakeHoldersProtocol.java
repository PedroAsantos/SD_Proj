package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import communication.Message;
import sharingRegions.MonitorStable;

public class StakeHoldersProtocol {

	private static StakeHoldersProtocol instance = null;
	private volatile boolean serverOn=true;
	private static ServerCom sCon;
	protected StakeHoldersProtocol() {
		// Exists only to defeat instantiation.
	}
	
	public static void setServerCom(ServerCom scon) {
		sCon=scon;
	}
	public static StakeHoldersProtocol getInstance() {
		if (instance == null) {
			instance = new StakeHoldersProtocol();
		}
		return instance;
	}

	public Message processInput(Message message, MonitorStable mStable) {
		
		Method method = null;
		Object objectReturn = null;
		
		Class<? extends Object>[] argsClass =null;
		if(message.getArgs()!=null) {
			argsClass = new Class[message.getArgs().length];
			for(int i = 0;i<argsClass.length;i++) {
				if(message.getArgs()[i].getClass()==Integer.class) {
					argsClass[i]=int.class;
				}else if(message.getArgs()[i].getClass()==Boolean.class) {
					argsClass[i]=boolean.class;
				}else if(message.getArgs()[i].getClass()==Double.class) {
					argsClass[i]=double.class;
				}else {
					argsClass[i]=message.getArgs()[i].getClass();
				}
			}			
		}

		try {
			method = mStable.getClass().getDeclaredMethod(message.getFunctionName(),argsClass);
		} catch (NoSuchMethodException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			objectReturn = method.invoke(mStable,message.getArgs());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
	
		return new Message(objectReturn);
	}
	   public boolean getServerState() {
		   return serverOn;
	   }
	   
	   public void setServerOff() {
		   serverOn=false;
	   }
}
