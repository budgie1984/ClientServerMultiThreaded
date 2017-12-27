package views;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Font;
import javax.swing.ScrollPaneConstants;

public class ServerWindow {
	/**
	 * 
	 * @author Brian Burroughs - 20062048 
	 * 		   Applied Computing
	 *
	 */
	
	public JFrame serverWindow; // server frame
	public JTextArea textArea; // text window
	private JScrollPane scrollPane; // scroll bar object
	
	public ServerWindow(){
		buildServerWindow();
	}


	/**
	 * method to build the server Gui
	 */
	public void  buildServerWindow() {
		serverWindow = new JFrame("Server Window");
		serverWindow.setBackground(new Color(135, 206, 250));
		serverWindow.getContentPane().setBackground(Color.DARK_GRAY);
		serverWindow.getContentPane().setLayout(null);
		textArea = new JTextArea();
		textArea.setBackground(new Color(220, 220, 220));
		textArea.setBounds(12, 72, 414, 178);
		
		JLabel lblServer = new JLabel("Server");
		lblServer.setFont(new Font("Dialog", Font.ITALIC, 18));
		lblServer.setForeground(Color.WHITE);
		lblServer.setBounds(175, 25, 88, 23);
		serverWindow.getContentPane().add(lblServer);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(12, 72, 414, 178);
		scrollPane.setViewportView(textArea);
		serverWindow.getContentPane().add(scrollPane);
		
		serverWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverWindow.setBounds(100, 100, 450, 300);
		serverWindow.setVisible(true);
	}
}
