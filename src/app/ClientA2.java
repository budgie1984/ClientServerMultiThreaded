package app;

import java.io.*;
import java.net.*;
import java.awt.event.*;

import views.ClientWindow;

/**
 * 
 * @author Brian Burroughs - 20062048 
 * 		   Applied Computing
 *
 */

public class ClientA2 {

	private DataOutputStream streamToServer;
	private DataInputStream streamFromServer;
	private InetAddress inetAddress;
	
	ClientWindow clientWindow;
	Socket socket;
	Double radius;

	public static void main(String[] args) {
		new ClientA2();
	}

	/**
	 * Construct a new client
	 */
	public ClientA2() {
		try {
			// Create a socket to connect to the server port 8000
			socket = new Socket("localhost", 8000);
			inetAddress = socket.getInetAddress();
		
			// Create an input stream to receive data from the server
			streamFromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			streamToServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			clientWindow.textArea.append(ex.toString() + '\n');
		}
		
		clientWindow = new ClientWindow();
		clientWindow.btnSubmit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					
					int StudentID = Integer.parseInt(clientWindow.textField_studentID.getText());
					streamToServer.writeInt(StudentID);  // send student id to server
					
					String module = clientWindow.textField_moduleName.getText();
					streamToServer.writeUTF(module);	// send module to server
									
					receiveStreamFromServer(); // receive byte stream from server

				} catch (Exception ex) {
					clientWindow.textArea.append("An error has occured - Socket is closed now \n");
					try {
						socket.close();
					} catch (IOException e1) {
						clientWindow.textArea.append("An error has occured \n");
					}
				}
				
			}
		});

	}


	
	/**
	 * 
	 * Receives data stream from server
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	
	private void receiveStreamFromServer() throws UnsupportedEncodingException, IOException{

		int length = streamFromServer.readInt();
		byte[] dataFromServer = new byte[length];
		streamFromServer.readFully(dataFromServer);
		String resultString = new String(dataFromServer, "UTF-8");
		clientWindow.textArea.append(resultString + "\n");
	}
	
	
}