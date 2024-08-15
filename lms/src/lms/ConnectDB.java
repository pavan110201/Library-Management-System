package lms;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ConnectDB {
      static Connection connection = null;
      static String databaseName = "lms";
      static String url = "jdbc:mysql://localhost:3306/" +databaseName;
      static String username = "root";
      static String password = "Reddy@2025";
      
      
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
          Class.forName("com.mysql.jdbc.Driver").newInstance();
          connection = DriverManager.getConnection(url,username, password);
//          PreparedStatement ps = connection.prepareStatement("INSERT INTO lms1_table (id, name) VALUES ('1', 'pavan');");
//          int result = ps.executeUpdate();
          boolean runApplication = true;
          System.out.println("Welcome to Library Management System..!");
          while(runApplication) {
	          System.out.println("1. Add Book\n2. Add Borrower\n3. Borrow Book\n4. Return Book\n5. Search Books\n6. View Borrowing History\n7. Exit Application");
	          try{
	        	  Scanner scanner = new Scanner(System.in);
	        	  System.out.println("Please enter choice number of operation");
				  String choice = scanner.nextLine();
				  LmsServiceImpl lmsServiceImpl = new LmsServiceImpl(connection);
				  switch (choice) {
				      case "1":
				    	  lmsServiceImpl.addBook();
				          break;
				      case "2":
				    	  lmsServiceImpl.addBorrower();
				          break;
				      case "3":
				    	  lmsServiceImpl.borrowBook();
				          break;
				      case "4":
				    	  lmsServiceImpl.returnBook();
				          break;
				      case "5":
				    	  lmsServiceImpl.searchBook();
				          break;
				      case "6":
				    	  lmsServiceImpl.viewHistory();
				          break;
				      case "7":
				    	  runApplication = false;
				          System.out.println("Exiting the Application..!");
				          break;
				      default:
				          System.out.println("Invalid choice! Please enter a valid option.");
				  }
	          }
	          catch(Error E) {
	        	  System.out.println("Operation failed, please retry..!");
	          }
          }
	}
}
