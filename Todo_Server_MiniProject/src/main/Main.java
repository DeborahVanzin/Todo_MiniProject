package main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import client.ClientThread;
import database.IToDoDatabase;
import database.InMemoryToDoDatabase;
import login.ILoginHandler;
import login.LoginHandler;
import model.Token;
import model.User;

public class Main {
	private static Logger logger = Logger.getLogger(Main.class.getName());
	private static final int PORT = 50002; // Port that application listens on
	
	public static void main(String[] args) {
		System.out.println("Todo Server MiniProject has started!");
		
		IToDoDatabase toDoDatabase = new InMemoryToDoDatabase();
		ILoginHandler loginHandler = new LoginHandler(toDoDatabase);
		toDoDatabase.setLoginHandler(loginHandler);

		try (ServerSocket listener = new ServerSocket(PORT)) {
			
			while (true) {
				// Wait for a client to connect and then create a separate thread to serve the client
				logger.info("Waiting for a client to connect...");
				Socket socket = listener.accept();
				ClientThread clientThread = new ClientThread(socket, toDoDatabase, loginHandler);
				clientThread.start();
			}
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}
}