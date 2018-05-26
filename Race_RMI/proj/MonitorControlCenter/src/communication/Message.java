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
	* @param returnFunction name of the function to execute on monitor
	*/
	public Message(Object returnFunction) {
		this.returnFunction=returnFunction;
	}
	
	/**
	* Constructor with one string argument
	*
	* @param functionName name of the function to execute on monitor
	*/
	public Message(String functionName) {
		this.functionName=functionName;
	}

	/**
	* Constructor with one string argument and an array of objects
	*
	* @param functionName name of the function.
	* @param args arguments needed to function.
	*/
	public Message(String functionName,Object[] args) {
		this.functionName=functionName;
		this.args=args;
	}
	

	/**
	* Get function name
	*@return name of the function.
	*/
	public String getFunctionName() {
		return functionName;
	}

	/**
	* Get arguments
	*	@return Object[] get arguments of the function
	*/
	public Object[] getArgs() {
		return args;
	}

	/**
	* Get the return of the function
	*	@return Object get return of the function
	*/
	public Object getReturn() {
		return returnFunction;
	}

	/**
	* Get end variable that indicates if it's to end
	* @return if the message is to finish the conversation between client and server
	*/
	public boolean getEnd() {
		return end;
	}
	
	
}
