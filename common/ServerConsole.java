// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import server.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */

import simpleChat.common.Message;
import simpleChat.common.ChatIF;
import simpleChat.EchoServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  EchoServer server;
  
  
  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port) {
      server = new EchoServer(port);
      try {
          server.listen();
      } catch (IOException e) {
          System.out.println("ERROR: cannot listen");
      }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }
  public void command(String message) throws IOException {
	  if (message.startsWith("#")) {
       	String command = message.substring(1);
       	switch (command) {
       	
       	
       	case "quit":
     
       
     	  display("System Shut Down.");
          server.sendToAllClients("Server is down.");
          server.stopListening();
          server.close();
          break;

       case "stop":
    	   

          server.stopListening();
          break;

       case "close":
    	   

          System.out.println("Server Closed.");
          server.sendToAllClients("Server is Closed");
          server.close();
          break;
          
       case "#setport":

      

          String m = message.split(" ")[1] ;
          if ( server.isListening() ) {
              System.out.println("Port set to " + m + ". The server has to be closed to do the changes.");
          }
          else {
              System.out.println("Port set to " + m + ".");
              server.setPort(Integer.parseInt(m));
          }
          break;

       case "#start":
    	   

          server.listen();
          break;

       case "#getport":

          System.out.println(server.getPort());
          break;

      
  }
	  }
  }
  
       	

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
	  int port; //Port to listen on

      try {
          port = Integer.parseInt(args[0]); //Get port from command line
      } catch (Throwable t) {
          port = DEFAULT_PORT; //Set port to 5555
      }

      ServerConsole serv = new ServerConsole(port);
      serv.accept();
  }  //Wait for console data
  }

//End of ConsoleChat class
