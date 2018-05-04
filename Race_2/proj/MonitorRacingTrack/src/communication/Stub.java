package communication;

/**
 * Este tipo de dados define o stub de comunicacao do problema Riddles Play.
 */

public class Stub {
	/**
	 * Nome do sistema computacional onde esta localizado o servidor.
	 */

	private String serverHostName;

	/**
	 * Numero do port de escuta do servidor.
	 */

	private int serverPortNumb;

	/**
	 * Instanciacao do stub.
	 *
	 * @param hostName
	 *            nome do sistema computacional onde esta localizado o servidor
	 * @param port
	 *            numero do port de escuta do servidor
	 */

	public Stub(String hostName, int port) {
		serverHostName = hostName;
		serverPortNumb = port;
	}

	/**
	 * Troca de mensagens com os servidor KnockKnock.
	 */

	public Message exchange(Message message) {
		ClientCom com = new ClientCom(serverHostName, serverPortNumb);
		Message fromServer, // linha de entrada
				returnFunction=null; // linha de saida
	
		while (!com.open()) // estabelecimento de ligacao
		{
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}
		
		
		com.writeObject(message);
		while ((fromServer = (Message) com.readObject()) != null) // teste de recepcao de mensagem do servidor
		{
		//	System.out.println("Servidor: " + fromServer); // imprimir mensagem no dispositivo de saida
															// standard
			if (fromServer.getEnd()) 
				break; // teste de continuacao	
			
			returnFunction=fromServer;
					
			// ler mensagem do utilizador
	//		System.out.println("Cliente: " + fromUser); // imprimir mensagem no dispositivo de saida
			
			com.writeObject(new Message(true)); // envia-la ao servidor
		}
		

		com.close(); // fecho da ligacao
		return returnFunction;
	}
}

