package app;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import views.ServerWindow;

/**
 * 
 * @author Brian Burroughs - 20062048 
 * 		   Applied Computing
 *
 */

public class MultiThreadedServerA2 {
	// connection and database statement and result objects
	private Connection con;
	private Statement st;
	private ResultSet rs;
	
	private DecimalFormat df = new DecimalFormat("#.##");// round to 2 decimal places
	ServerWindow serverWindow; // gui window for server
	
	// connection to database details
	private String jdbcDriver = "com.mysql.jdbc.Driver"; // jdbc driver
	private String dbAddress = "jdbc:mysql://localhost:3306/"; // port no for database
	private String dbName = "gradedatabase"; // database name
	private String userName = "root";  // database username
	private String password = "";   // database password - blank
	
	private int caGrade, examGrade, studentId; // grades and student is variables
	private String firstName, lastName;
	private String clientInformtion = ""; // output for client window
	

	public static void main(String[] args) {
		new MultiThreadedServerA2();
	}

	public MultiThreadedServerA2() {
		serverWindow = new ServerWindow(); // build the server window gui
		connectToDb();// connect to db on start
		try {
			
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(8000);
			serverWindow.textArea.append("Server started at " + new Date() + '\n');

			while (true) {              // infinite loop listening for socket connection
				Socket socket = serverSocket.accept();
				Client client = new Client(socket);
				client.start();
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	} 

	/**
	 * method to connect to the gradedatabase
	 */
	public Connection connectToDb() {
		try {
			Class.forName(jdbcDriver);
			con = DriverManager.getConnection(dbAddress + dbName, userName, password);
			System.out.println("connected to the database successfully");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error connecting to database", "Message Box",
					JOptionPane.INFORMATION_MESSAGE);
		}
		return con;
	}
	

	private class Client extends Thread {
		
		private Socket socket;
		// The ip address of the client i.e localhost 127.0.0.1
		private InetAddress inetAddress;
		
		private DataInputStream inputFromClient; // incoming stream from client
		private DataOutputStream outputToClient; // outgoing stream to client

	
		public Client(Socket socket) throws IOException {
		
			inputFromClient = new DataInputStream(socket.getInputStream());
			outputToClient = new DataOutputStream(socket.getOutputStream());
			
			inetAddress = socket.getInetAddress();  // 127.0.0.1 i.e local host
			serverWindow.textArea.append(" \nClient's host name: " + inetAddress.getHostName() + "\n"); //local host
			serverWindow.textArea.append("Client's IP Address is: " + inetAddress.getHostAddress() + "\n"); // 127.0.0.1
			
		}

		/**
		 * Run method required for inherited thread class
		 * Sends data about student grades to
		 * the client window
		 */
		public void run() {

			try {
				String threadId = Thread.currentThread().getName(); // current thread number  e.g 1,2 etc..
				serverWindow.textArea.append("Thread Id: " + threadId + "\n");
				serverWindow.textArea
				.append("-------------------------------------------------------------------------------------------------------");

				while (true) {
					clientInformtion = "";
					int studentID = inputFromClient.readInt();
					String module = inputFromClient.readUTF();

					if (checkStudentRegistered(studentID, module)) {
						

						int studentCATotalMarks = rs.getInt("CA_Mark");  	// ca marks for users specified module from database
						int studentExamTotalMarks = rs.getInt("Exam_Mark"); // exam marks for users specified module from database

						double studentCaPercentage = studentCATotalMarks * 0.3;    // ca mark converted to percentage * 0.3 to get mark out of 30 percent
						double studentExamPercentage = studentExamTotalMarks * 0.7; // exam mark converted to percentage * 0.7 to get mark out of 70percent

						int totalMarks = studentCATotalMarks + studentExamTotalMarks; // total mark obtained between CA and Exam
						double totalModulePercentage = studentCaPercentage + studentExamPercentage; // total percentage between Ca and Exam
						double moduleAverage = totalModulePercentage / 2; // divide by 2 for average grade for module, ca and exam = 2
						
					
						clientInformtion += "Welcome " + firstName + " " + lastName + 					   "\n";
						clientInformtion += "Server has sent the following data....						    \n";
						clientInformtion += "Student Id: " + studentId +								   "\n";
						clientInformtion += "First Name: " + firstName +								   "\n";
						clientInformtion += "Surname: "    + lastName +								   	   "\n";
						clientInformtion += "Module: " 	   + module +									   "\n";
						clientInformtion += "CA Mark: " + caGrade + " ("+df.format(studentCaPercentage) +"%)\n"; // ca marks formatted to 2 decimal places
						clientInformtion += "Exam Mark: " + examGrade + " ("+df.format(studentExamPercentage) +"%)\n"; //exam marks formated to two decimal places
						clientInformtion += "Overall Grade: " + totalMarks + " ("+df.format(totalModulePercentage) +"%)\n\n"; // total grade( total percentage in brackets)
					
						clientInformtion += "------------------------Results Breakdown %-------------------------\n";
						clientInformtion += "Ca Total: " + df.format(studentCaPercentage) + "% out of 30%		 \n";
						clientInformtion += "Exam Total: " + df.format(studentExamPercentage) + "% out of 70%	 \n";
						clientInformtion += "Module Total: " + df.format(totalModulePercentage) + "% out of 100% \n\n";

						if(totalModulePercentage >= 40){  // pass if module percentage >= 40 otherwise failed module
							 clientInformtion += "Module Total is equal to or above 40%\nCongratulations you passed: " 
									 + module + "\n";

							}else{
							 clientInformtion += "Module Total is below 40%\nSorry, you have failed: " + module  + "   \n"; // fail if module percentage is below 40
							}
						clientInformtion += "Module Average: "  + df.format(moduleAverage) + "%	"; 
				
					} else {
						clientInformtion += "Sorry "+ studentID + "- Registered Applicants only! \n";
						clientInformtion += "Enter a valid Id or valid module name \n";
						clientInformtion += "Modules available:\nGame Design\nDistributed Systems \n";


					}
					sendToCient(); // send data as bytes to client
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
		
		
		/**
		 * Method converts data to bytes to send to client
		 * 
		 * @throws UnsupportedEncodingException
		 * @throws IOException
		 */
		private void sendToCient() throws UnsupportedEncodingException, IOException {
			byte[] resultAsByteArray = clientInformtion.getBytes("UTF-8");
			this.outputToClient.writeInt(resultAsByteArray.length);
			this.outputToClient.write(resultAsByteArray);
			this.outputToClient.flush();
		}
	}
	
	
	/**
	 * The main intelligence of the app, checks if
	 * a student is registered by checking their student id and 
	 * displays the students grades for the module typed into the Gui.
	 * 
	 * @param studentID - students id no to check
	 * @param module - the module to check grades for
	 * @return true or false depending on the result of the query
	 */
	protected boolean checkStudentRegistered(int studentID, String module) {
		boolean registered = false;
		try {
			st = con.createStatement();
			String query = "select students.fname as fname, students.sname as sname, modulegrades.ModuleName as ModuleName, "
					+ "modulegrades.CA_Mark as CA_Mark, modulegrades.Exam_Mark as Exam_Mark "
					+ "from students, modulegrades " + "where students.STUD_ID = " + studentID
					+ " and students.STUD_ID = modulegrades.STUD_ID and " + "modulegrades.ModuleName = \"" + module + "\";";
			rs = st.executeQuery(query);
			if (rs.first()) {
				studentId = studentID;
				firstName = rs.getString("FNAME");
				lastName = rs.getString("SNAME");
				caGrade = rs.getInt("CA_Mark");
				examGrade = rs.getInt("Exam_Mark");
				registered = true;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error performing query", "Message Box",
					JOptionPane.INFORMATION_MESSAGE);
		}
		return registered; // return true if id is registered, else false
	}

	
	


}