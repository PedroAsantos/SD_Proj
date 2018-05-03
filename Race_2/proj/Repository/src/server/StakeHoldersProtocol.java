package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import sharingRegions.Repository;

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

	public String processInput(String payload, Repository repo) {
		System.out.println("processInput");
		Method method = null;
		String[] payloadCamps = payload.split(";");
		Object returnFunction=null;
		String returnFunctionPayload=null;
		String[] checkArray;
		int[] oneArrayOfPayload;
		Map<Integer,int[]> allArraysOfPayload = new HashMap<Integer,int[]>();
		
		for(int argumentNumber=0;argumentNumber<payloadCamps.length;argumentNumber++) {
			System.out.println("payloadCamps[argumentNumber]="+payloadCamps[argumentNumber]);
			checkArray=payloadCamps[argumentNumber].split(",");

			if(checkArray.length>1) {
				//tem array, por isso converter para int
				System.out.println("true is array");
				oneArrayOfPayload = new int[checkArray.length];
				for(int c=0;c<checkArray.length;c++) {
					oneArrayOfPayload[c]=Integer.parseInt(checkArray[c]);
					System.out.println("checkArray->"+checkArray[c]);
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
			 * System.out.println("method.invoke(repo, null);");
			 * repo.proceedToStable(1); }else {
			 */
			System.out.println("***********************"+payloadCamps.length);
			switch (payloadCamps.length) {
			case 1:
				System.out.println("CASE1");
				method = repo.getClass().getDeclaredMethod(payloadCamps[0]);
				returnFunction = method.invoke(repo);
				break;
			case 2:
				if(allArraysOfPayload.isEmpty()) {
					System.out.println("CASE2A");
					method = repo.getClass().getDeclaredMethod(payloadCamps[0],int.class);
					returnFunction= method.invoke(repo,Integer.parseInt(payloadCamps[1]));	
				}else {
					System.out.println("CASE2B");
					method = repo.getClass().getDeclaredMethod(payloadCamps[0],int.class);
					returnFunction= method.invoke(repo,allArraysOfPayload.get(1));	
				}
				break;
			case 3:
				System.out.println("CASE3B");
				method = repo.getClass().getDeclaredMethod(payloadCamps[0],int.class,int.class);
				if(allArraysOfPayload.isEmpty()) {
					returnFunction= method.invoke(repo,Integer.parseInt(payloadCamps[1]),Integer.parseInt(payloadCamps[2]));
				}else {
					if(allArraysOfPayload.containsKey(1)&&	allArraysOfPayload.containsKey(2)) {
						returnFunction= method.invoke(repo,allArraysOfPayload.get(1),allArraysOfPayload.get(2));
					}else {
						if(allArraysOfPayload.containsKey(1)) {
							returnFunction= method.invoke(repo,allArraysOfPayload.get(1),Integer.parseInt(payloadCamps[2]));
						}else {
							returnFunction= method.invoke(repo,Integer.parseInt(payloadCamps[1]),allArraysOfPayload.get(2));
						}
					}
				}
								
				break;
			case 4:
				System.out.println("CASE4B");
				//accepting only function int,double,int -> it is the unique necessary case
				method = repo.getClass().getDeclaredMethod(payloadCamps[0],int.class,double.class,int.class);
				returnFunction= method.invoke(repo,Integer.parseInt(payloadCamps[1]), Double.parseDouble(payloadCamps[2]),Integer.parseInt(payloadCamps[3]));
			
				/*if(allArraysOfPayload.isEmpty()) {
					returnFunction= method.invoke(repo,Integer.parseInt(payloadCamps[1]),Integer.parseInt(payloadCamps[2]),Integer.parseInt(payloadCamps[3]));
				}else {
					if(allArraysOfPayload.containsKey(1)&&	allArraysOfPayload.containsKey(2) && allArraysOfPayload.containsKey(3)) {
						returnFunction= method.invoke(repo,allArraysOfPayload.get(1),allArraysOfPayload.get(2),allArraysOfPayload.get(2));
					}else {
						if(allArraysOfPayload.containsKey(1)) {
							returnFunction= method.invoke(repo,allArraysOfPayload.get(1),Integer.parseInt(payloadCamps[2]));
						}else {
							returnFunction= method.invoke(repo,Integer.parseInt(payloadCamps[1]),allArraysOfPayload.get(2));
						}
					}
				}*/
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