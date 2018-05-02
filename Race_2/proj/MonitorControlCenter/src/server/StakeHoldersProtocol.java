package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sharingRegions.MonitorControlCenter;

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

	public String processInput(String payload, MonitorControlCenter mRacingTrack) {
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
			}else if(payloadCamps[argumentNumber].length() - payloadCamps[argumentNumber].replace(",", "").length()==1) {
				oneArrayOfPayload = new int[1];
				oneArrayOfPayload[0]=Integer.parseInt(checkArray[0]);
				allArraysOfPayload.put(argumentNumber,oneArrayOfPayload);
			}
		}
		
		try {
			/*
			 * if(functionToCall.equals("proceedToStable")) {
			 * System.out.println("method.invoke(mRacingTrack, null);");
			 * mRacingTrack.proceedToStable(1); }else {
			 */
			switch (payloadCamps.length) {
			case 1:
				method = mRacingTrack.getClass().getDeclaredMethod(payloadCamps[0]);
				returnFunction = method.invoke(mRacingTrack);
				break;
			case 2:
				if(allArraysOfPayload.isEmpty()) {
					method = mRacingTrack.getClass().getDeclaredMethod(payloadCamps[0],int.class);
					returnFunction= method.invoke(mRacingTrack,Integer.parseInt(payloadCamps[1]));	
				}else {
					method = mRacingTrack.getClass().getDeclaredMethod(payloadCamps[0],int[].class);
					int[] temp = Arrays.copyOf(allArraysOfPayload.get(1), allArraysOfPayload.get(1).length);
					returnFunction= method.invoke(mRacingTrack,temp);	
				}
				break;
			case 3:
				if(allArraysOfPayload.isEmpty()) {
					method = mRacingTrack.getClass().getDeclaredMethod(payloadCamps[0],int.class,int.class);
					returnFunction= method.invoke(mRacingTrack,Integer.parseInt(payloadCamps[1]),Integer.parseInt(payloadCamps[2]));
				}else {
					if(allArraysOfPayload.containsKey(1) &&	allArraysOfPayload.containsKey(2)) {
						method = mRacingTrack.getClass().getDeclaredMethod(payloadCamps[0],int[].class,int[].class);
						returnFunction= method.invoke(mRacingTrack,allArraysOfPayload.get(1),allArraysOfPayload.get(2));
					}else {
						if(allArraysOfPayload.containsKey(1)) {
							method = mRacingTrack.getClass().getDeclaredMethod(payloadCamps[0],int[].class,int.class);
							returnFunction= method.invoke(mRacingTrack,allArraysOfPayload.get(1),Integer.parseInt(payloadCamps[2]));
						}else {
							method = mRacingTrack.getClass().getDeclaredMethod(payloadCamps[0],int.class,int[].class);
							returnFunction= method.invoke(mRacingTrack,Integer.parseInt(payloadCamps[1]),allArraysOfPayload.get(2));
						}
					}
				}
								
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
		int[] tempIntArray;
		if (method.getReturnType().isArray()) {
		        if(method.getReturnType().getComponentType().equals(Integer.TYPE)) {
		        	tempIntArray=(int[]) returnFunction;
		        	returnFunctionPayload="";
		        	for(int i=0;i<tempIntArray.length;i++) {
		        		returnFunctionPayload+=Integer.toString(tempIntArray[i]);
		        		if(i<tempIntArray.length-1) {
		        			returnFunctionPayload+=",";
		    			}
		        	}
		        }
		}else if(method.getReturnType().equals(Integer.TYPE)) {
			tempInt=(int) returnFunction;  
			returnFunctionPayload=  Integer.toString(tempInt);
		}else if(method.getReturnType().equals(Boolean.TYPE)){
			tempBoolean=(boolean) returnFunction;  
			returnFunctionPayload= String.valueOf(tempBoolean);
		}
		return returnFunctionPayload;
	}
}
