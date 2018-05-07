package communication;

public class Message implements java.io.Serializable {
	
	private String functionName;
	private Object[] args;
	private Object returnFunction;
	private boolean end;
	
	/**
	* Constructor with boolean argument
	*
	* @param end indicates if it's to end
	*/
	public Message(boolean end) {
		this.end=end;
	}

	public Message() {
		
	}
	/**
	* Constructor with one object argument
	*
	* @param returnFunction
	*/
	public Message(Object returnFunction) {
		this.returnFunction=returnFunction;
	}
	
	/**
	* Constructor with one string argument
	*
	* @param functionName
	*/
	public Message(String functionName) {
		this.functionName=functionName;
	}

	/**
	* Constructor with one string argument and an array of objects
	*
	* @param functionName
	* @param args
	*/
	public Message(String functionName,Object[] args) {
		this.functionName=functionName;
		this.args=args;
	}
	

	/**
	* Get function name
	*
	*/
	public String getFunctionName() {
		return functionName;
	}

	/**
	* Get arguments
	*
	*/
	public Object[] getArgs() {
		return args;
	}

	/**
	* Get the return of the function
	*
	*/
	public Object getReturn() {
		return returnFunction;
	}

	/**
	* Get end variable that indicates if it's to end
	*
	*/
	public boolean getEnd() {
		return end;
	}
	
	
}
