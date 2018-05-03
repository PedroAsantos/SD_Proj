package communication;

public class Message implements java.io.Serializable {
	
	private String functionName;
	private Object[] args;
	//hostNames[0]= MonitorStable
	//hostNames[1]= MonitorControlCenter
	//hostNames[2]= MonitorPaddock
	//hostNames[3]= MonitorBettingCenter
	//hostNames[4]= MonitorRacingTrack
	//private String[] hostNames;
	private Object returnFunction;
	private boolean end;
	
	public Message(boolean end) {
		this.end=end;
	}
	public Message() {
		
	}
	public Message(Object returnFunction) {
		this.returnFunction=returnFunction;
	}
	
	public Message(String functionName) {
		this.functionName=functionName;
	}
	
	public Message(String functionName,Object[] args) {
		this.functionName=functionName;
		this.args=args;
	}
	
	/*public Message(String functionName,Object arg1,Object arg2) {
		this.functionName=functionName;
		this.arg1=arg1;
		this.arg2=arg2;
	}
	
	public Message(String functionName,Object arg1,Object arg2, Object arg3) {
		this.functionName=functionName;
		this.arg1=arg1;
		this.arg2=arg2;
		this.arg3=arg3;
	}*/
	
	/*public Message(String[] hostNames) {
		this.hostNames=hostNames;
	}
	
	public String[] getHostNames(){
		return hostNames;
	}*/
	
	public String getFunctionName() {
		return functionName;
	}
	public Object[] getArgs() {
		return args;
	}
	public Object getReturn() {
		return returnFunction;
	}
	public boolean getEnd() {
		return end;
	}
	
	
}
