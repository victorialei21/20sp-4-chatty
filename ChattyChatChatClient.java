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
			
			BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			/*(for(int i = 0; i < 4; i++) {
				System.out.println(in.readLine());
			}
			
			while(!done) {
				String clientInput = in.readLine();
				out.println(clientInput);
				
				if(clientInput.equals("/quit")) {
					done = true;
				}//end if statement
				
			}//end while loop*/
			
		} catch (IOException e) {
			System.err.println("Error connecting to server :(");
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
				//something here
			}//end catch
		}//end finally
	}//end main()

}//end ChattyChatChatClient class
