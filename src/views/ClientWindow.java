package views;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class ClientWindow{
	/**
	 * 
	 * @author Brian Burroughs - 20062048 
	 * 		   Applied Computing
	 *
	 */
	
	public JTextField textField_studentID,textField_moduleName;
	private JLabel lblClient, lblStudentId, lblModule;
	public JTextArea textArea;
	public JFrame clientWindow; // client frame
	public JButton btnSubmit; // submit button

	public ClientWindow(){
		buildClientGui();
	}


	/**
	 * method to build the client Gui
	 */
	
	private void buildClientGui() {
		clientWindow = new JFrame("Client Window");
		clientWindow.getContentPane().setBackground(Color.DARK_GRAY);
		clientWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientWindow.setBounds(100, 100, 650, 339);
		clientWindow.setBackground(new Color(135, 206, 250));
		clientWindow.getContentPane().setLayout(null);
		
		lblClient = new JLabel("Client");
		lblClient.setFont(new Font("Dialog", Font.ITALIC, 18));
		lblClient.setForeground(Color.WHITE);
		lblClient.setBounds(148, 63, 62, 15);
		clientWindow.getContentPane().add(lblClient);
		
		lblStudentId = new JLabel("Student Id:");
		lblStudentId.setForeground(Color.WHITE);
		lblStudentId.setBounds(24, 107, 89, 15);
		clientWindow.getContentPane().add(lblStudentId);
		
		textField_studentID = new JTextField();
		textField_studentID.setBounds(115, 105, 138, 19);
		clientWindow.getContentPane().add(textField_studentID);
		textField_studentID.setColumns(10);
		
		lblModule = new JLabel("Module:");
		lblModule.setForeground(Color.WHITE);
		lblModule.setBounds(46, 149, 67, 15);
		clientWindow.getContentPane().add(lblModule);
		
		textField_moduleName = new JTextField();
		textField_moduleName.setColumns(10);
		textField_moduleName.setBounds(115, 147, 138, 19);
		clientWindow.getContentPane().add(textField_moduleName);
		
		textArea = new JTextArea();
		textArea.setBackground(new Color(220, 220, 220));
		textArea.setBounds(276, 12, 341, 277);
		
		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(115, 187, 138, 25);
		clientWindow.getContentPane().add(btnSubmit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(277, 12, 340, 277);
		scrollPane.setViewportView(textArea);
		clientWindow.getContentPane().add(scrollPane);
		

	
		clientWindow.setVisible(true);
	}
}
