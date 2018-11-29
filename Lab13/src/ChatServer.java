import java.net.*;
import java.io.*;
public class ChatServer extends Thread {
	public Socket connectionSocket;
	public BufferedReader inFromClient;
	public BufferedReader inFromUser;
	public PrintWriter outToClient;
	public boolean sender_mode;
	public static boolean operation;
	public String inputLine, outputLine;
	public ChatServer(Socket connectionSocket, BufferedReader inFromClient) {
		this.connectionSocket = connectionSocket;
		this.inFromClient = inFromClient;
		sender_mode = false;
		operation = true;
	}
	public ChatServer(Socket connectionSocket, BufferedReader inFromUser,PrintWriter outToClient) {
		this.connectionSocket = connectionSocket;
		this.inFromUser = inFromUser;
		this.outToClient = outToClient;
		sender_mode = true;
		operation = true;
		}
	public void run() {
			if (sender_mode == true) {
				try {		
					while (true) {
						if(outputLine!=null) {
							System.out.println("Server: " + outputLine);
							outToClient.println(outputLine);
						}
						if (outputLine.equals("Bye.")) {
							operation = false;
							System.out.println("true mode break");
							System.out.println("rthread connection socket closed");
							connectionSocket.close();
							break;
						
						}
					}
				}
				catch (IOException ex) {
					System.out.println(ex);
				}
			}
			else {
				try {
					while (true) {
						if(inputLine!=null) {
							if (inputLine.length() > 0) {
								System.out.println("Client: " + inputLine);
							}	
							if (inputLine.equals("Client: Bye.")) {
								operation = false;
							}
						}
						if (operation == false) {
							System.out.println("false mode break");
							System.out.println("sthread connection socket closed");
							connectionSocket.close();
							break;
						}
					}
				}
				catch (IOException ex) {
					System.out.println(ex);
				}
				
			}

	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage: java ChatServer <port number>");
			System.exit(1);
		}
		int portNumber = Integer.parseInt(args[0]);
		ServerSocket welcomeSocket = new ServerSocket(portNumber);
		Socket connectionSocket = welcomeSocket.accept();
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); //for user input
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
						PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream(),true);
						outToClient.println("You are connected to this server\n");
						ChatServer sthread = new ChatServer(connectionSocket, inFromUser,outToClient);
						ChatServer rthread = new ChatServer(connectionSocket, inFromClient);
						sthread.start();
						rthread.start();
	}
}