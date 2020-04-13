import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChattyChatChatClient {
	public static void main(String[] args) throws IOException {
		int portNumber = Integer.parseInt(args[1]);
		String hostName = args[0];
		Socket socket = null;
		
		try {
			//attempt to connect to server
			socket = new Socket(hostName, portNumber);
			
			System.out.println("Connected to host server\n");
			
			//creating reading and writing threads
			ReadThread rThread = new ReadThread(socket);
			WriteThread wThread = new WriteThread(socket);
			Thread reader = new Thread(rThread);
			Thread writer = new Thread(wThread);
			
			//starting reading and writing threads
			reader.start();
			writer.start();
			
		} catch (IOException e) {
			System.err.println("Error connecting to server :(");
			e.printStackTrace();
		}//end catch block
		
	}//end main()
}//end ChattyChatChatClient class


class ReadThread implements Runnable {
	BufferedReader in;
	Socket socket;
	
	public ReadThread(Socket socket) {
		this.socket = socket;
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Error getting input stream: " + e.getMessage());
            e.printStackTrace();
		}//end catch block
		
}//end ReadThread constructor
	
	@Override
	public void run() {
		
		//flag to stop thread
		boolean done = false;

		while(!done) {
			try {
				
				//if socket is open, proceed
				while (!socket.isClosed()) {
					
					//reads message coming from server
					String fromServer = in.readLine();

					if(fromServer == null || fromServer.startsWith("/quit")) {
						done = true;
						break;
					}
					
					//print message from server
					if(fromServer != null) {
					    System.out.println(fromServer);
					}
				}//end while loop
			} catch(IOException e) {
				System.err.println("Error reading from server: " + e.getMessage());
                e.printStackTrace();
			}//end catch block

			//close socket after /quit is initiated
			try {
				socket.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}//end catch block
		}//while loop
	}//end run()
	
}//end ReadThread class

class WriteThread extends Thread {
	PrintWriter out;
	Socket socket;
    BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
	
	public WriteThread(Socket socket) {
		this.socket = socket;
		
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.err.println("Error getting output stream");
			e.printStackTrace();
		}//end catch block
	}//end constructor
	
	@Override
	public void run() {
		
		try {
			
			//flag to break stop thread
		    boolean done = false;

			while(!done) {
				
				//if socket is open, proceed
				while(!socket.isClosed()) {
					
					//reads line of input from client
					String toSend = read.readLine();
					
					if(toSend == null || toSend.startsWith("/quit")) {
						done = true;
					}
					if (toSend != null) {
						out.println(toSend);
					}
					if(done == true) {
						break;
					}
				}//end while loop
			}//end while loop
		} catch (IOException e) {
			System.err.println("Error writing to server");
			e.printStackTrace();
		}//end catch block
	}//end run()
	
}//end WriteThread class