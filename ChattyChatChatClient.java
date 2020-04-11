import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChattyChatChatClient {
	public static void main(String[] args) {
		int portNumber = Integer.parseInt(args[1]);
		String hostName = args[0];
		Socket socket = null;
		
		try {
			socket = new Socket(hostName, portNumber);
			System.out.println("Connected to host server\n");
			
			new ReadThread(socket).start();
			new WriteThread(socket).start();
			
		} catch (IOException e) {
			System.err.println("Error connecting to server :(");
			e.printStackTrace();
		} 
	
	}//end main()

}//end ChattyChatChatClient class


class ReadThread extends Thread {
	BufferedReader in;
	Socket socket;
	boolean done = false;

	
	public ReadThread(Socket socket) {
		this.socket = socket;
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Error getting input stream: " + e.getMessage());
            e.printStackTrace();
		}
	}//end constructor
	
	@Override
	public void run() {
			
		while(true) {
			try {
				
				while (!done) {
					String fromServer = in.readLine();

					if(fromServer == null || fromServer.startsWith("/quit")) {
						done = true;
					}
					if(fromServer != null) {
					    System.out.println(fromServer);
					}
				}//end while loop
			} catch(IOException e) {
				System.err.println("Error reading from server: " + e.getMessage());
                e.printStackTrace();
			} finally {
				try {
					in.close();
					socket.close();
				} catch (IOException e) {
					System.err.println("Error closing input stream and socket");
					e.printStackTrace();
				}
			}
		}
		
	}//end run()
	
}//end ReadThread class

class WriteThread extends Thread {
	PrintWriter out;
	Socket socket;
    BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
    boolean done = false;
	
	public WriteThread(Socket socket) {
		this.socket = socket;
		
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.err.println("Error getting output stream");
			e.printStackTrace();
		}
	}//end constructor
	
	@Override
	public void run() {
		
		try {
			
			while(!done) {
				String toSend = read.readLine();
				
				if(toSend == null || toSend.startsWith("/quit")) {
					done = true;
				}
				if(toSend != null) {
					out.println(toSend);
				}
			}//end while loop
			
		} catch (IOException e) {
			System.err.println("Error writing to server");
			e.printStackTrace();
		} finally {
			out.close();
		}
		
	}//end run()
}