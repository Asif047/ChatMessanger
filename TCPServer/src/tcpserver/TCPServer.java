package tcpserver;
import java.net.*;
import java.io.*;

public class TCPServer implements Runnable //thread can be created using two ways implement and runable implement
{  private ChatServerThread clients[] = new ChatServerThread[50];
   private ServerSocket server = null;
   private Thread       thread = null;
   private int clientCount = 0;
  
   
   //new
    int j=0;
   int flag=0;
   String[] inputsaver={"","","","","","","","","","","","","","","","","","","",""};
   int[] idsaver={0,0,0,0,0,0,0,0,0,0};
   private DataInputStream  console   = null;
    private DataOutputStream streamOut = null;
   //new

   public TCPServer (int port)
   {  try
      {  System.out.println("Binding to port " + port + ", please wait  ...");
         server = new ServerSocket(port);  
         System.out.println("Server started: " + server);
         start(); //after creating object, run will start
      
      
      }
      catch(IOException ioe)
      {  System.out.println("Can not bind to port " + port + ": " + ioe.getMessage()); }
   }
@Override
   public void run()
   {  while (thread != null)
      {  try
         {  System.out.println("Waiting for a client ..."); 
            addThread(server.accept()); 

         } //create new threads for new clients
      
           
            
      
      
         catch(IOException ioe)
         {  System.out.println("Server accept error: " + ioe); stop(); }
      }
   }
   public void start() throws IOException  { if (thread == null)
      {  
           thread = new Thread(this);  //server is working as thread
         thread.start();
         
         
         
          //new start
         
         //kick client er kaj
         
         console   = new DataInputStream(System.in);
         String kickclient=console.readLine();
         System.out.println("Server input:"+kickclient);
         
         
          if (kickclient.trim().equals("kick"))
      {  
          BufferedReader inFromServer=new BufferedReader(new InputStreamReader(System.in));
          
          System.out.println("Enter client no.:");
           String kickmsg;
          kickmsg=inFromServer.readLine();
          
          int kickmsgInt = Integer.parseInt(kickmsg);
          clients[findClient(kickmsgInt)].send(".bye");
         remove(kickmsgInt); 
      }
         
         
         //new end
          
          thread = new Thread(this);  //server is working as thread
         thread.start();
         
         
      }
   }
   public void stop()   { 
   if (thread != null)
      {  thread.stop(); 
         thread = null;
      }
   }
   private int findClient(int ID) //return index number using id
   {  for (int i = 0; i < clientCount; i++)
         if (clients[i].getID() == ID)
            return i;
      return -1;
   }
   public synchronized void handle (int ID, String input) throws IOException
   {  
       //new
       int uniId;
       
       String unicast="";
       String multicast="";
       String multiclient="";
       String multiclient2="";
       String multiclient3="";
       
       String uniclient="";
       
       String originInput="";
       
       String originInput2="";
    
       if(input.length()>2)
   unicast=input.substring(0,3);
        if(input.length()>4)
   multicast=input.substring(0,5);
        
   if(input.length()>8)
   uniclient=input.substring(4,9);
   
   if(input.length()>8)
   originInput=input.substring(10,input.length());
   
   //System.out.println("unicast:"+unicast);
   //System.out.println("unicast:"+uniclient);
   //System.out.println("unicast:"+originInput);
   
   //uni cast er kaj kora hoise
    if (unicast.trim().equals("uni"))
      {  
          int uniclientint = Integer.parseInt(uniclient);
         // int finduniclientint=findClient(uniclientint);
          
          clients[findClient(uniclientint)].send(ID+": "+originInput);
         
      }
    
    
    //multicast er kaj kora hoise
    else if (multicast.trim().equals("multi"))
     {
          if(input.length()>10)
    multiclient=input.substring(6,11);
          if(input.length()>16)
    multiclient2=input.substring(12,17);
          if(input.length()>22)
    multiclient3=input.substring(18,23);
          
          if(input.length()>23)
   originInput2=input.substring(24,input.length());
          
          
           int multiclientint = Integer.parseInt(multiclient);
            int multiclientint2 = Integer.parseInt(multiclient2);
             int multiclientint3 = Integer.parseInt(multiclient3);
             
             clients[findClient(multiclientint)].send(ID+": "+originInput2);
             clients[findClient(multiclientint2)].send(ID+": "+originInput2);
             clients[findClient(multiclientint3)].send(ID+": "+originInput2);
          
     }
    
    
    //for private chat group
    
    /*
    else if(input.trim().equals("private chat group on"))
    {
        flag=1;
    }
    else if(input.trim().equals("private chat group off"))
        flag=0;
    //private chat group er kaj
    else if(flag==1)
    {
        String client1="";
         
        String client2="";
         
        String client3="";
        if(input.length()>26)
          client1=input.substring(22,27);
        
          if(input.length()>32)
          client2=input.substring(28,33);
         
         if(input.length()>38)
         client3=input.substring(34,39);
         
         int clientint1 = Integer.parseInt(client1);
         int clientint2 = Integer.parseInt(client2);
         int clientint3 = Integer.parseInt(client3);
         
         System.out.println(clientint1);
         System.out.println(clientint2);
         System.out.println(clientint3);
         
         clients[findClient(clientint1)].send(ID+": "+input);
         clients[findClient(clientint2)].send(ID+": "+input);
         clients[findClient(clientint3)].send(ID+": "+input);
         
    }
    
    */
    
    
    
    
    
  
    //new
       
    else if (input.equals(".bye"))
      {  clients[findClient(ID)].send(".bye");
         remove(ID); }
   
   
      else
       {
         for (int i = 0; i < clientCount; i++)
            clients[i].send(ID + ": " + input); 
               }
   System.out.println(ID + ": " + input); // show the output at server by broadcust
   
   
   
   //new2 start
   
   //show history er kaj
    
    
    
       inputsaver[j]=input; 
       idsaver[j]=ID; 
       //System.out.println("current input"+j+" :"+inputsaver[j]);
       if (input.equals("history"))
      {  
          for(int k=0;k<j;k++)
         clients[findClient(ID)].send(idsaver[k]+" :"+inputsaver[k]);
         
      }
       j++;
       
       
    
   
   
   //new2 end
   
   
   
   
   }
   public synchronized void remove(int ID)
   {  int pos = findClient(ID);
      if (pos >= 0)
      {  ChatServerThread toTerminate = clients[pos];
         System.out.println("Removing client thread " + ID + " at " + pos);
         if (pos < clientCount-1)
            for (int i = pos+1; i < clientCount; i++)
               clients[i-1] = clients[i];
         clientCount--;
         try
         {  toTerminate.close(); }
         catch(IOException ioe)
         {  System.out.println("Error closing thread: " + ioe); }
         toTerminate.stop(); }
   }
   private void addThread(Socket socket) //adding new thread using open and start
   {  if (clientCount < clients.length)
      {  System.out.println("Client accepted: " + socket);
         clients[clientCount] = new ChatServerThread(this, socket);
         try
         {  clients[clientCount].open(); 
            clients[clientCount].start();  
            clientCount++; }
         catch(IOException ioe)
         {  System.out.println("Error opening thread: " + ioe); } }
      else
         System.out.println("Client refused: maximum " + clients.length + " reached.");
   }
   public static void main(String args[]) throws Exception{ TCPServer server = null;
         server = new TCPServer(2000); 
   
   
         
   
   }
}