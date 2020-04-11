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
				String fromServer;
				
				while ((fromServer = in.readLine()) != null) {
				    System.out.println(fromServer);
				}
			} catch(IOException e) {
				System.err.println("Error reading from server: " + e.getMessage());
                e.printStackTrace();
			}
		}
	}
	
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
		}
	}//end constructor
	
	@Override
	public void run() {
		
		try {
			String toSend;
			
			while((toSend = read.readLine())!= null) {
				out.println(toSend);
			}
			
		} catch (IOException e) {
			System.err.println("Error writing to server");
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}//end run()
}