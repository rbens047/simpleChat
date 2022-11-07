// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
	ChatIF serverUI;
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }
  public EchoServer(int port, ChatIF sUI) throws IOException
  {
    super(port);
    this.serverUI = sUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
  (Object msg, ConnectionToClient client)
{
	  if((boolean)(client.getInfo("isFirstMessage"))){
			
		  String[] msg_ = (String.valueOf(msg)).split(" ", 2);
			if(msg_[0].equals("#login")){
				client.setInfo(" ", msg_[1]);
			}
			else{
				try{
					client.sendToClient("You are not loged in");
					client.close();
				}
				catch(IOException e){
				}
			}
		}
		else{
			if((String.valueOf(msg)).startsWith("#login")){
				try{
					client.sendToClient("ERROR: YOU ARE LOGGED IN ALREADY!");
				}
				catch(IOException e){
				}
			}
			System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
		    this.sendToAllClients(String.valueOf(client.getInfo("loginID"))+ ": "+ msg);
		}
}

public void handleMessageFromServerUI(String message)
{
  if (message.startsWith("#"))
  {
  	handleCommand(message);
  }
  else
  {
    // send message to clients
    serverUI.display(message);
    this.sendToAllClients("SERVER MSG> " + message);
  }
}

private void handleCommand(String command) {
	  if(command.equals("#quit")) {
		  serverUI.display("END.");
		  quit();
	  }
	  
	  else if(command.toLowerCase().equals("#close")) {
		  try
	      {
	        close();
	      }
	      catch(IOException e) {}
	  }
	  else if(command.toLowerCase().startsWith("#setport")) {
		  if (!isListening() && getNumberOfClients() == 0)
	      {
	        int newPort = Integer.parseInt(command.substring(9));
	        setPort(newPort);
	        serverUI.display
	          ("Server port changed to " + getPort());
	      }
	      else
	      {
	        serverUI.display("The server must be closed.");
	      }
	  }
	  else if(command.toLowerCase().equals("#getport")) {
		  serverUI.display("Current port is: " + getPort());
	  }
	  else if (command.equalsIgnoreCase("#start")){
	      if (!isListening()){
	    	  try{
	    		  listen();
	    		 }
	        catch(Exception ex){
	        	serverUI.display("Error - Could not listen for clients!");
	        	}
	      }
	      else{
	    	  serverUI.display
	    	  ("The server is already listening for clients.");
	      }
	    }
	  else
		  serverUI.display("The command does not exist.");
}
  
/**
 * This method overrides the one in the superclass.  Called
 * when the server starts listening for connections.
 */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println
      ("Client disconnected.");
  }
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println
      ("Client connected.");
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
