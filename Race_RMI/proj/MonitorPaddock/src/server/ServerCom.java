package server;

import java.io.*;
import java.net.*;

/**
 *   Este tipo de dados implementa o canal de comunicacao, lado do servidor, para uma comunicacao baseada em passagem de
 *   mensagens sobre sockets usando o protocolo TCP.
 *   A transferencia de dados e baseada em objectos, um objecto de cada vez.
 */

public class ServerCom
{
  /**
   *  Socket de escuta
   *    @serialField listeningSocket
   */

   private ServerSocket listeningSocket = null;

  /**
   *  Socket de comunicacao
   *    @serialField commSocket
   */

   private Socket commSocket = null;

  /**
   *  Numero do port de escuta do servidor
   *    @serialField serverPortNumb
   */

   private int serverPortNumb;

  /**
   *  Stream de entrada do canal de comunicacao
   *    @serialField in
   */

   private ObjectInputStream in = null;

  /**
   *  Stream de saida do canal de comunicacao
   *    @serialField out
   */

   private ObjectOutputStream out = null;

  /**
   *  Instanciacao de um canal de comunicacao (forma 1).
   *
   *    @param portNumb numero do port de escuta do servidor
   */

   public ServerCom (int portNumb)
   {
      serverPortNumb = portNumb;
   }

  /**
   *  Instanciacao de um canal de comunicacao (forma 2).
   *
   *    @param portNumb numero do port de escuta do servidor
   *    @param lSocket socket de escuta
   */

   public ServerCom (int portNumb, ServerSocket lSocket)
   {
      serverPortNumb = portNumb;
      listeningSocket = lSocket;
   }

  /**
   *  Estabelecimento do servico.
   *  Instanciacao de um socket de escuta e sua associacao ao endereco da maquina local
   *  e ao port de escuta publicos.
   */

   public void start ()
   {
      try
      { listeningSocket = new ServerSocket (serverPortNumb);
    	listeningSocket.setSoTimeout(10000);
      }
      catch (BindException e)                         // erro fatal --- port ja em uso
      { System.out.println (Thread.currentThread ().getName () +
                                 " - nao foi possivel a associacao do socket de escuta ao port: " +
                                 serverPortNumb + "!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)                           // erro fatal --- outras causas
      { System.out.println (Thread.currentThread ().getName () +
                                 " - ocorreu um erro indeterminado na associacao do socket de escuta ao port: " +
                                 serverPortNumb + "!");
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Encerramento do servico.
   *  Fecho do socket de escuta.
   */

   public void end ()
   {
      try
      { listeningSocket.close ();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - nao foi possivel fechar o socket de escuta!");
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Processo de escuta.
   *  Criacao de um canal de comunicacao para um pedido pendente.
   *  Instanciacao de um socket de comunicacao e sua associacao ao endereco do cliente.
   *  Abertura dos streams de entrada e de saida do socket.
   *
   *    @return canal de comunicacao
   */

   public ServerCom accept ()
   {
      ServerCom scon;                                      // canal de comunicacao

      scon = new ServerCom(serverPortNumb, listeningSocket);
      boolean success=false;
      try
      { scon.commSocket = listeningSocket.accept();
         success=true;
      }
      catch (SocketTimeoutException e) {
  		// TODO: handle exception
  	  }
      catch (SocketException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - foi fechado o socket de escuta durante o processo de escuta!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - nao foi possivel abrir um canal de comunicacao para um pedido pendente!");
        e.printStackTrace ();
        System.exit (1);
      }
      if(success) {
	      try
	      { scon.in = new ObjectInputStream (scon.commSocket.getInputStream ());
	      }
	      catch (IOException e)
	      { System.out.println (Thread.currentThread ().getName () +
	                                 " - nao foi possivel abrir o canal de entrada do socket!");
	        e.printStackTrace ();
	        System.exit (1);
	      }
	
	      try
	      { scon.out = new ObjectOutputStream (scon.commSocket.getOutputStream ());
	      }
	      catch (IOException e)
	      { System.out.println (Thread.currentThread ().getName () +
	                                 " - nao foi possivel abrir o canal de saida do socket!");
	        e.printStackTrace ();
	        System.exit (1);
	      }
      
    	  return scon;
      }
      return null;
   }

  /**
   *  Fecho do canal de comunicacao.
   *  Fecho dos streams de entrada e de saida do socket.
   *  Fecho do socket de comunicacao.
   */

   public void close ()
   {
      try
      { in.close();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - nao foi possivel fechar o canal de entrada do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { out.close();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - nao foi possivel fechar o canal de saida do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { commSocket.close();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - nao foi possivel fechar o socket de comunicacao!");
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Leitura de um objecto do canal de comunicacao.
   *
   *    @return objecto lido
   */

   public Object readObject ()
   {
      Object fromClient = null;                            // objecto

      try
      { fromClient = in.readObject ();
      }
      catch (InvalidClassException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o objecto lido nao e passivel de desserializacao!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - erro na leitura de um objecto do canal de entrada do socket de comunicacao!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (ClassNotFoundException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o objecto lido corresponde a um tipo de dados desconhecido!");
        e.printStackTrace ();
        System.exit (1);
      }

      return fromClient;
   }

  /**
   *  Escrita de um objecto no canal de comunicacao.
   *
   *    @param toClient objecto a ser escrito
   */

   public void writeObject (Object toClient)
   {
	  
      try
      { out.writeObject (toClient);
      }
      catch (InvalidClassException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o objecto a ser escrito nao e passivel de serializacao!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (NotSerializableException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o objecto a ser escrito pertence a um tipo de dados nao serializavel!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - erro na escrita de um objecto do canal de saida do socket de comunicacao!");
        e.printStackTrace ();
        System.exit (1);
      }
   }
}
