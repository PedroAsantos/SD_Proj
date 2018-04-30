package communication;

/**
 * Este tipo de dados define o stub de comunica��o do problema Riddles Play.
 */

public class Stub {
	/**
	 * Nome do sistema computacional onde est� localizado o servidor.
	 */

	private String serverHostName;

	/**
	 * N�mero do port de escuta do servidor.
	 */

	private int serverPortNumb;

	/**
	 * Instancia��o do stub.
	 *
	 * @param hostName
	 *            nome do sistema computacional onde est� localizado o servidor
	 * @param port
	 *            n�mero do port de escuta do servidor
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
				returnFunction=null; // linha de sa�da
		System.out.println("teste1");
		while (!com.open()) // estabelecimento de liga��o
		{
			try {
				Thread.currentThread().sleep((long) (10));
			} catch (InterruptedException e) {
			}
		}
		System.out.println("teste2");
		
		com.writeObject(payload);
		while ((fromServer = (String) com.readObject()) != null) // teste de recep��o de mensagem do servidor
		{
			System.out.println("Servidor: " + fromServer); // imprimir mensagem no dispositivo de sa�da
															// standard
			if (fromServer.equals("Ok!")) {
				break; // teste de continua��o	
			}else {
				returnFunction=fromServer;
			}		
			// ler mensagem do utilizador
			do {
				fromUser = "Ok!";
			} while (fromUser == null);
			System.out.println("Cliente: " + fromUser); // imprimir mensagem no dispositivo de sa�da
			System.out.println("teste3"); // standard
			com.writeObject(fromUser); // envi�-la ao servidor
		}
		System.out.println("teste5");

		com.close(); // fecho da liga��o
		return returnFunction;
	}
}
