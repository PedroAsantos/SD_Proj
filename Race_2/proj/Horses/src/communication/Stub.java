package communication;

/**
 * Este tipo de dados define o stub de comunicação do problema Riddles Play.
 */

public class Stub {
	/**
	 * Nome do sistema computacional onde está localizado o servidor.
	 */

	private String serverHostName;

	/**
	 * Número do port de escuta do servidor.
	 */

	private int serverPortNumb;

	/**
	 * Instanciação do stub.
	 *
	 * @param hostName
	 *            nome do sistema computacional onde está localizado o servidor
	 * @param port
	 *            número do port de escuta do servidor
	 */

	public Stub(String hostName, int port) {
		serverHostName = hostName;
		serverPortNumb = port;
	}

	/**
	 * Troca de mensagens com os servidor KnockKnock.
	 */

	public String exchange(String payload) {
		ClientCom com = new ClientCom(serverHostName, serverPortNumb);
		String fromServer, // linha de entrada
				fromUser,
				returnFunction=null; // linha de saída
		System.out.println("teste1");
		while (!com.open()) // estabelecimento de ligação
		{
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}
		System.out.println("teste2");
		
		com.writeObject(payload);
		while ((fromServer = (String) com.readObject()) != null) // teste de recepção de mensagem do servidor
		{
			System.out.println("Servidor: " + fromServer); // imprimir mensagem no dispositivo de saída
															// standard
			if (fromServer.equals("Ok!")) {
				break; // teste de continuação	
			}else {
				returnFunction=fromServer;
			}		
			// ler mensagem do utilizador
			do {
				fromUser = "Ok!";
			} while (fromUser == null);
			System.out.println("Cliente: " + fromUser); // imprimir mensagem no dispositivo de saída
			System.out.println("teste3"); // standard
			com.writeObject(fromUser); // enviá-la ao servidor
		}
		System.out.println("teste5");

		com.close(); // fecho da ligação
		return returnFunction;
	}
}
