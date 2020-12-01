package main;

import java.net.ServerSocket;
import java.net.Socket;

import client.ClientThread;
import database.IToDoDatabase;
import database.InMemoryToDoDatabase;
import login.ILoginHandler;
import login.LoginHandler;
import model.Token;
import model.User;

public class Main {
	
	private static final int PORT = 50002;
	
	public static void main(String[] args) {
		System.out.println("Todo Server MiniProject has started!");
		
		IToDoDatabase toDoDatabase = new InMemoryToDoDatabase();
		ILoginHandler loginHandler = new LoginHandler(toDoDatabase);
		toDoDatabase.setLoginHandler(loginHandler);

		try (ServerSocket listener = new ServerSocket(PORT)) {
			
			while (true) {
				// Wait for request, then create input/output streams to talk to the client
				System.out.println("Waiting for a client to connect...");
				Socket socket = listener.accept();
				ClientThread clientThread = new ClientThread(socket, toDoDatabase); // Dependency injection
				clientThread.start();
			}
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}
}