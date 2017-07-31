/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread //
{  private TCPServer       server    = null;
   private Socket           socket    = null;
   private int              ID        = -1;
   private DataInputStream  streamIn  =  null;
   private DataOutputStream streamOut = null;
   
   
   //new
   
   String inputsaver;
   
   //new

   public ChatServerThread(TCPServer _server, Socket _socket)
   {  super();
      server = _server;
      socket = _socket;
      ID     = socket.getPort();
      
   }
   public void send(String msg)  //msg send er kaj kore
   {   try
       {  streamOut.writeUTF(msg);
          streamOut.flush();
         
       }
       catch(IOException ioe)
       {  System.out.println(ID + " ERROR sending: " + ioe.getMessage());
          server.remove(ID);
          stop();
       }
   }
   public int getID()
   {  return ID;
   }
   public void run() 
   {  System.out.println("Server Thread " + ID + " running.");
      while (true)
      {  try
         {  
             
             server.handle(ID, streamIn.readUTF());
             
            
         }
         catch(IOException ioe) 
         {  System.out.println(ID + " ERROR reading: " + ioe.getMessage()); //local port working as ai id
            server.remove(ID); //exception remove korar kaj kore
            stop();
         }
      }
   }
   public void open() throws IOException
   {  streamIn = new DataInputStream(new 
                        BufferedInputStream(socket.getInputStream()));
      streamOut = new DataOutputStream(new
                        BufferedOutputStream(socket.getOutputStream()));
   }
   public void close() throws IOException
   {  if (socket != null)    socket.close();
      if (streamIn != null)  streamIn.close();
      if (streamOut != null) streamOut.close();
   }
}