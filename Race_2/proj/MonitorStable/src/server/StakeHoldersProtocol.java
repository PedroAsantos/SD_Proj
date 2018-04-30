package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sharingRegions.MonitorStable;

public class StakeHoldersProtocol {

	private static StakeHoldersProtocol instance = null;

	protected StakeHoldersProtocol() {
		// Exists only to defeat instantiation.
	}

	public static StakeHoldersProtocol getInstance() {
		if (instance == null) {
			instance = new StakeHoldersProtocol();
		}
		return instance;
	}

	public String processInput(String payload, MonitorStable mStable) {
		System.out.println("processInput");
		Method method = null;
		String[] payloadCamps = payload.split(";");
		String returnFunction=null;
		try {
			/*
			 * if(functionToCall.equals("proceedToStable")) {
			 * System.out.println("method.invoke(mStable, null);");
			 * mStable.proceedToStable(1); }else {
			 */
			
			switch (payloadCamps.length) {
			case 1:
				method = mStable.getClass().getDeclaredMethod(payloadCamps[0]);
				break;
			case 2:
				method = mStable.getClass().getDeclaredMethod(payloadCamps[0],int.class);
				break;
			case 3:

				break;
			case 4:

				break;

			default:
				break;
			}
			
			// }
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			/*
			 * if(functionToCall.equals("proceedToStable")) {
			 * System.out.println("method.invoke(mStable, null);");
			 * 
			 * }else {
			 */
			switch (payloadCamps.length) {
			case 1:
				returnFunction = (String) method.invoke(mStable);
				break;
			case 2:
				returnFunction= (String) method.invoke(mStable,Integer.parseInt(payloadCamps[1]));
				break;
			case 3:

				break;
			case 4:

				break;

			default:
				break;
			}
			

			// }

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnFunction;
	}
}
