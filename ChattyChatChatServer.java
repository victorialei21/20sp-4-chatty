import java.util.*; 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChattyChatChatServer {

	//vector that stores clients currently on the server
	static Vector<ChatClientHandler> clientList = new Vector<ChatClientHandler>(); 
	static Vector<String> users = new Vector<String>();
	
	//client counter
	static int clientNumber = 0;

	public static void main (String[] args) throws IOException {
		
		int portNumber = Integer.parseInt(args[0]);
		boolean runServer = true;
		ServerSocket listener = null;
		Socket socket = null;
		
		try {
			System.out.println("Binding to port " + portNumber + ", please wait...");
			listener = new ServerSocket(portNumber);
	
		} catch(Exception e) {
			System.err.println("Could not listen on port " + portNumber);
			e.printStackTrace();
			runServer = false;
		}//end catch block

		while(runServer) {
				
			try {	
				//server socket listens until client program connects to same port
				socket = listener.accept();
				
				//confirmation message
		        System.out.println("Server started: " + listener);
				System.out.println("Client request received from: " + socket);
				System.out.println("Creating a new handler for client " + clientNumber); 
				
				//create separate thread for new client
				ChatClientHandler handler = new ChatClientHandler(socket); 
	            
				//instantiate thread
		        Thread thrd = new Thread(handler);
		            
		        //add thread to running list of client handlers 
		        clientList.add(handler);
		        
		        //start this thread
		        thrd.start();
		        
		        clientNumber++;
		        
			} catch(IOException e) {
				System.err.println("Error adding client to list");
			} finally {
				try {
					socket.close();
				} catch(Exception e) {
					System.err.println("Error closing socket");
				}//end catch block
			}//end finally
		}//end while loop
	}//end main
	
}//end ChattyChatChatServer class

class ChatClientHandler implements Runnable {

    Socket socket; 
    boolean loggedIn; 
    PrintWriter out;
    BufferedReader in;
    String name;
    
    public ChatClientHandler (Socket socket) throws IOException {
    	this.socket = socket;
    	this.loggedIn = true;
    	out = new PrintWriter(socket.getOutputStream(), true);
    	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
	@Override
	public void run() {
		
		out.println("Welcome to ChattyChatChat!");
		out.println("To send a general message, simply begin typing");					
		out.println("To set your nickname, type /nick <name>");
		out.println("To send a direct message, type /dm <name> <msg>");
		out.println("To disconnect from the server, type /quit");
		
		try {
			
			String[] userInput;	
			
			userInput = in.readLine().split(" ");
						
			while(userInput[0] != null) {
				
				if(userInput[0] == "/nick") {
					getNickName(userInput[1]);
				}
				else if(userInput[0] == "/dm") {
					String directMsg = null;
					for (int i = 2; i < userInput.length; i++) {
						directMsg += userInput[i];
						directMsg += " ";
					}
					sendDM(userInput[1], directMsg);
				}
				else if(userInput[0] == "/quit") {
					this.socket.close();
					break;
				}
				else {
					
				}
			}
            
		} catch (IOException e){
			System.err.println("Error connecting to server!");
		}//end catch statement
	}
	
	public String getNickName(String name) {
		return name;
		//users.add(name); 
		
	}//end createName
	
	public void sendDM(String userToSendTo, String message) {
		for(ChatClientHandler usr : ChattyChatChatServer.clientList) {
			
			if(usr.name.equals(userToSendTo)) {
				usr.out.println(this.name + " : " + message);
			}//end if block
		}//end for loop
		
		
	}//end sendMessage

}
