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
		boolean done = false;
		
		try {
			socket = new Socket(hostName, portNumber);
			System.out.println("Connected to host server!");
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		   			
			String fromServer;
			
			while ((fromServer = in.readLine()) != null) {
			    System.out.println(fromServer);
			    
			}

			
		} catch (IOException e) {
			System.err.println("Error connecting to server :(");
			e.printStackTrace();
		} /*finally {
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}//end catch
		}//end finally*/
	}//end main()

}//end ChattyChatChatClient class
