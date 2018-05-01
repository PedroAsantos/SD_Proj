package server;

import java.io.*;
import java.net.*;

/**
 *   Este tipo de dados implementa o canal de comunica��o, lado do servidor, para uma comunica��o baseada em passagem de
 *   mensagens sobre sockets usando o protocolo TCP.
 *   A transfer�ncia de dados � baseada em objectos, um objecto de cada vez.
 */

public class ServerCom
{
  /**
   *  Socket de escuta
   *    @serialField listeningSocket
   */

   private ServerSocket listeningSocket = null;

  /**
   *  Socket de comunica��o
   *    @serialField commSocket
   */

   private Socket commSocket = null;

  /**
   *  N�mero do port de escuta do servidor
   *    @serialField serverPortNumb
   */

   private int serverPortNumb;

  /**
   *  Stream de entrada do canal de comunica��o
   *    @serialField in
   */

   private ObjectInputStream in = null;

  /**
   *  Stream de sa�da do canal de comunica��o
   *    @serialField out
   */

   private ObjectOutputStream out = null;

  /**
   *  Instancia��o de um canal de comunica��o (forma 1).
   *
   *    @param portNumb n�mero do port de escuta do servidor
   */

   public ServerCom (int portNumb)
   {
      serverPortNumb = portNumb;
   }

  /**
   *  Instancia��o de um canal de comunica��o (forma 2).
   *
   *    @param portNumb n�mero do port de escuta do servidor
   *    @param lSocket socket de escuta
   */

   public ServerCom (int portNumb, ServerSocket lSocket)
   {
      serverPortNumb = portNumb;
      listeningSocket = lSocket;
   }

  /**
   *  Estabelecimento do servi�o.
   *  Instancia��o de um socket de escuta e sua associa��o ao endere�o da m�quina local
   *  e ao port de escuta p�blicos.
   */

   public void start ()
   {
      try
      { listeningSocket = new ServerSocket (serverPortNumb);
      }
      catch (BindException e)                         // erro fatal --- port j� em uso
      { System.out.println (Thread.currentThread ().getName () +
                                 " - n�o foi poss�vel a associa��o do socket de escuta ao port: " +
                                 serverPortNumb + "!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)                           // erro fatal --- outras causas
      { System.out.println (Thread.currentThread ().getName () +
                                 " - ocorreu um erro indeterminado na associa��o do socket de escuta ao port: " +
                                 serverPortNumb + "!");
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Encerramento do servi�o.
   *  Fecho do socket de escuta.
   */

   public void end ()
   {
      try
      { listeningSocket.close ();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - n�o foi poss�vel fechar o socket de escuta!");
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Processo de escuta.
   *  Cria��o de um canal de comunica��o para um pedido pendente.
   *  Instancia��o de um socket de comunica��o e sua associa��o ao endere�o do cliente.
   *  Abertura dos streams de entrada e de sa�da do socket.
   *
   *    @return canal de comunica��o
   */

   public ServerCom accept ()
   {
      ServerCom scon;                                      // canal de comunica��o

      scon = new ServerCom(serverPortNumb, listeningSocket);
      try
      { scon.commSocket = listeningSocket.accept();
      }
      catch (SocketException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - foi fechado o socket de escuta durante o processo de escuta!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - n�o foi poss�vel abrir um canal de comunica��o para um pedido pendente!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { scon.in = new ObjectInputStream (scon.commSocket.getInputStream ());
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - n�o foi poss�vel abrir o canal de entrada do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { scon.out = new ObjectOutputStream (scon.commSocket.getOutputStream ());
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - n�o foi poss�vel abrir o canal de sa�da do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      return scon;
   }

  /**
   *  Fecho do canal de comunica��o.
   *  Fecho dos streams de entrada e de sa�da do socket.
   *  Fecho do socket de comunica��o.
   */

   public void close ()
   {
      try
      { in.close();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - n�o foi poss�vel fechar o canal de entrada do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { out.close();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - n�o foi poss�vel fechar o canal de sa�da do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { commSocket.close();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - n�o foi poss�vel fechar o socket de comunica��o!");
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Leitura de um objecto do canal de comunica��o.
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
                                 " - o objecto lido n�o � pass�vel de desserializa��o!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - erro na leitura de um objecto do canal de entrada do socket de comunica��o!");
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
   *  Escrita de um objecto no canal de comunica��o.
   *
   *    @param toClient objecto a ser escrito
   */

   public void writeObject (Object toClient)
   {
	   System.out.println("TOCLIENT"+toClient);
      try
      { out.writeObject (toClient);
      }
      catch (InvalidClassException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o objecto a ser escrito n�o � pass�vel de serializa��o!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (NotSerializableException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o objecto a ser escrito pertence a um tipo de dados n�o serializ�vel!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - erro na escrita de um objecto do canal de sa�da do socket de comunica��o!");
        e.printStackTrace ();
        System.exit (1);
      }
   }
}
