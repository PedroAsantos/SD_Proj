package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import sharingRegions.MonitorPaddock;

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

	public String processInput(String payload, MonitorPaddock mPaddock) {
		System.out.println("processInput");
		Method method = null;
		String[] payloadCamps = payload.split(";");
		Object returnFunction=null;
		String returnFunctionPayload=null;
		String[] checkArray;
		int[] oneArrayOfPayload;
		Map<Integer,int[]> allArraysOfPayload = new HashMap<Integer,int[]>();
		
		for(int argumentNumber=0;argumentNumber<payloadCamps.length;argumentNumber++) {
			checkArray=payloadCamps[argumentNumber].split(",");
			if(checkArray.length>1) {
				//tem array, por isso converter para int
				oneArrayOfPayload = new int[checkArray.length];
				for(int c=0;c<checkArray.length;c++) {
					oneArrayOfPayload[c]=Integer.parseInt(checkArray[c]);
				}
				allArraysOfPayload.put(argumentNumber,oneArrayOfPayload);
			}
		}
		
		try {
			/*
			 * if(functionToCall.equals("proceedToStable")) {
			 * System.out.println("method.invoke(mPaddock, null);");
			 * mPaddock.proceedToStable(1); }else {
			 */
			System.out.println("***********************"+payloadCamps.length);
			switch (payloadCamps.length) {
			case 1:
				System.out.println("CASE1");
				method = mPaddock.getClass().getDeclaredMethod(payloadCamps[0]);
				returnFunction = method.invoke(mPaddock);
				break;
			case 2:
				if(allArraysOfPayload.isEmpty()) {
					System.out.println("CASE2A");
					method = mPaddock.getClass().getDeclaredMethod(payloadCamps[0],int.class);
					returnFunction= method.invoke(mPaddock,Integer.parseInt(payloadCamps[1]));	
				}else {
					System.out.println("CASE2B");
					method = mPaddock.getClass().getDeclaredMethod(payloadCamps[0],int.class);
					returnFunction= method.invoke(mPaddock,allArraysOfPayload.get(1));	
				}
				break;
			case 3:
				System.out.println("CASE3B");
				method = mPaddock.getClass().getDeclaredMethod(payloadCamps[0],int.class);
				if(allArraysOfPayload.isEmpty()) {
					returnFunction= method.invoke(mPaddock,Integer.parseInt(payloadCamps[1]),Integer.parseInt(payloadCamps[2]));
				}else {
					if(allArraysOfPayload.containsKey(1)&&	allArraysOfPayload.containsKey(2)) {
						returnFunction= method.invoke(mPaddock,allArraysOfPayload.get(1),allArraysOfPayload.get(2));
					}else {
						if(allArraysOfPayload.containsKey(1)) {
							returnFunction= method.invoke(mPaddock,allArraysOfPayload.get(1),Integer.parseInt(payloadCamps[2]));
						}else {
							returnFunction= method.invoke(mPaddock,Integer.parseInt(payloadCamps[1]),allArraysOfPayload.get(2));
						}
					}
				}
								
				break;
			case 4:

				break;

			default:
				break;
			}

			System.out.println("--------------------------"+returnFunction);
			// }
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
		boolean tempBoolean;
		int tempInt;
		if(method.getReturnType().equals(Integer.TYPE)) {
			tempInt=(int) returnFunction;  
			returnFunctionPayload=  Integer.toString(tempInt);
		}else if(method.getReturnType().equals(Boolean.TYPE)){
			tempBoolean=(boolean) returnFunction;  
			returnFunctionPayload= String.valueOf(tempBoolean);
		}else if(method.getReturnType().equals(Boolean.TYPE)) {
			//fazer para os arrrays
		}
		return returnFunctionPayload;
	}
}
