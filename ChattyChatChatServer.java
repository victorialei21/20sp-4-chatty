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
		        
		        //increment client counter after starting a new handler thread
		        clientNumber++;
		        
			} catch(IOException e) {
				System.err.println("Error adding client to list");
				e.printStackTrace();
			}//end catch block
		}//end while loop
	}//end main
	
}//end ChattyChatChatServer class



class ChatClientHandler implements Runnable {

    Socket socket; 
    String name;
    PrintWriter out;
    BufferedReader in;
        
    public ChatClientHandler (Socket socket) throws IOException {
    	this.socket = socket;
    }//end ChatClientHandler constructor
    
	@Override
	public void run() {
		
		try {

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			//output welcome message
			out.println("Welcome to ChattyChatChat!");
			out.println("To send a general message, simply begin typing");					
			out.println("To set your nickname, type /nick <name>");
			out.println("To send a direct message, type /dm <name> <msg>");
			out.println("To disconnect from the server, type /quit");
						
			String inputLine; 
			
			//reading client's input
			while((inputLine = in.readLine()) != null) {
				
				//split input into an array of strings
				String[] inputArray = inputLine.split(" ");
				
				if(inputArray[0].equals("/nick")) {
					setNickName(inputArray[1]);
					out.println("Your nickname is: " + name);
				}//end if block
				
				else if(inputArray[0].equals("/dm")) {
					String directMsg = "";
					for (int i = 2; i < inputArray.length; i++) {
						directMsg += inputArray[i];
						directMsg += " ";
					}
					sendDM(inputArray[1], directMsg);
				}//end else if block
				
				else if(inputArray[0].equals("/quit")) {
					out.println("/quit initiated, thank you!");
					System.out.println("A client has disconnected");
					break;
				}//end else if block
				
				else {
					String generalMsg = "";
					for (int i = 0; i < inputArray.length; i++) {
						generalMsg += inputArray[i];
						generalMsg += " ";
					}//end for loop
					broadcastMsg(generalMsg);
				}//end else block
				
			}//end while loop	
			
		} catch (IOException e){
			System.err.println("Error connecting to server!");
			e.printStackTrace();
		}//end catch statement
	}//end run()
	
	public void setNickName(String name) {
		
		this.name = name;
	}//end setNickName
	
	public void sendDM(String userToSendTo, String message) {
		
		for(ChatClientHandler usr : ChattyChatChatServer.clientList) {
			if(usr.name != null) {
				if(usr.name.equals(userToSendTo)) {
					usr.out.println(message);
				}//end if block
			}//end if block
		}//end for loop
	}//end sendDM
	
	public void broadcastMsg(String message) {
		
		for(ChatClientHandler usr : ChattyChatChatServer.clientList) {
			usr.out.println(message);
		}//end for loop
	}//end broadcastMsg

}//end ChatClientHandler class
