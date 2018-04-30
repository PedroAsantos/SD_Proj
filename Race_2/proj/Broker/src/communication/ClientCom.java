package communication;


import java.io.*;
import java.net.*;

/**
 *   Este tipo de dados implementa o canal de comunica��o, lado do cliente, para uma comunica��o baseada em passagem de
 *   mensagens sobre sockets usando o protocolo TCP.
 *   A transfer�ncia de dados � baseada em objectos.
 */

public class ClientCom
{
  /**
   *  Socket de comunica��o
   *    @serialField commSocket
   */

   private Socket commSocket = null;

  /**
   *  Nome do sistema computacional onde est� localizado o servidor.
   */

   private String serverHostName = null;

  /**
   *  N�mero do port de escuta do servidor.
   */

   private int serverPortNumb;

  /**
   *  Stream de entrada do canal de comunica��o.
   */

   private ObjectInputStream in = null;

  /**
   *  Stream de sa�da do canal de comunica��o.
   */

   private ObjectOutputStream out = null;

  /**
   *  Instancia��o de um canal de comunica��o.
   *
   *    @param hostName nome do sistema computacional onde est� localizado o servidor
   *    @param portNumb n�mero do port de escuta do servidor
   */

   public ClientCom (String hostName, int portNumb)
   {
      serverHostName = hostName;
      serverPortNumb = portNumb;
   }

  /**
   *  Abertura do canal de comunica��o.
   *  Instancia��o de um socket de comunica��o e sua associa��o ao endere�o do servidor.
   *  Abertura dos streams de entrada e de sa�da do socket.
   *
   *    @return true, se o canal de comunica��o foi aberto; <br>
   *            false, em caso contr�rio.
   */

   public boolean open ()
   {
      boolean success = true;
      SocketAddress serverAddress = new InetSocketAddress (serverHostName, serverPortNumb);

      try
      { commSocket = new Socket();
        commSocket.connect (serverAddress);
      }
      catch (UnknownHostException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o nome do sistema computacional onde reside o servidor � desconhecido: " +
                                 serverHostName + "!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (NoRouteToHostException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o nome do sistema computacional onde reside o servidor � inating�vel: " +
                                 serverHostName + "!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (ConnectException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o servidor n�o responde em: " + serverHostName + "." + serverPortNumb + "!");
        if (e.getMessage ().equals ("Connection refused"))
           success = false;
           else { System.out.println (e.getMessage () + "!");
                  e.printStackTrace ();
                  System.exit (1);
                }
      }
      catch (SocketTimeoutException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - ocorreu um time out no estabelecimento da liga��o a: " +
                                 serverHostName + "." + serverPortNumb + "!");
        success = false;
      }
      catch (IOException e)                           // erro fatal --- outras causas
      { System.out.println (Thread.currentThread ().getName () +
                                 " - ocorreu um erro indeterminado no estabelecimento da liga��o a: " +
                                 serverHostName + "." + serverPortNumb + "!");
        e.printStackTrace ();
        System.exit (1);
      }

      if (!success) return (success);

      try
      { out = new ObjectOutputStream (commSocket.getOutputStream ());
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - n�o foi poss�vel abrir o canal de sa�da do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { in = new ObjectInputStream (commSocket.getInputStream ());
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - n�o foi poss�vel abrir o canal de entrada do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      return (success);
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
      Object fromServer = null;                            // objecto

      try
      { fromServer = in.readObject ();
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

      return fromServer;
   }

  /**
   *  Escrita de um objecto no canal de comunica��o.
   *
   *    @param toServer objecto a ser escrito
   */

   public void writeObject (Object toServer)
   {
      try
      { out.writeObject (toServer);
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
