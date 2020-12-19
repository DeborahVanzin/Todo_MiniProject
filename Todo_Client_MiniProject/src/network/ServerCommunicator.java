package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerCommunicator implements IServerCommunicator {
	
	private String serverUri;
	private int port;
	
	public ServerCommunicator(String serverUri, int port) {
		this.serverUri = serverUri;
		this.port = port;
	}

	@Override
	public String sendCommand(String command) {
		String answer = "";
		try {
			Socket socket = new Socket(serverUri, port);
			OutputStream outputStream = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(outputStream, true);
			writer.println(command);
			
			InputStream inputStream = socket.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(reader);
			answer = bufferedReader.readLine();
			
			bufferedReader.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return answer;
	}
}
