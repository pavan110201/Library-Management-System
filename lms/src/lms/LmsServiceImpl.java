package lms;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class LmsServiceImpl {
	private Connection connection;
	
	public LmsServiceImpl(Connection connection) {
		this.connection = connection;
	}

	Scanner scanner = new Scanner(System.in);
	//Method for adding book
	public void addBook() throws SQLException {
		System.out.println("Adding Book to database\nPlease enter ISBN, title, author, genere, publication year, issn");
		String input = scanner.nextLine();
		String[] bookDetails = input.split(",");
		try {
			PreparedStatement ps = connection.prepareStatement(
	        		"INSERT INTO Books (isbn, title, author, genere, publication_year, issn, status)"
	        		+ "VALUES (?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, bookDetails[0].trim());
			ps.setString(2, bookDetails[1].trim());
			ps.setString(3, bookDetails[2].trim());
			ps.setString(4, bookDetails[3].trim());
			ps.setInt(5, Integer.parseInt(bookDetails[4].trim()));
			ps.setString(6, bookDetails[5].trim());
			ps.setString(7, "Available");
	        ps.executeUpdate();
	        System.out.println("Book successfully added to the database..!");
		}
		catch(Exception e) {
			System.out.println("Operation failed for providing invalid data, error: "+ e);
		}
		return;
	}
	
	//Method for adding borrower
	public void addBorrower() {
		System.out.println("Adding Borrower to database\nPlease enter name, contact and email");
		String input = scanner.nextLine();
		String[] bookDetails = input.split(",");
		try {
			PreparedStatement ps = connection.prepareStatement(
	        		"INSERT INTO Borrowers (name, contact, email)"
	        		+ "VALUES (?, ?, ?)");
			ps.setString(1, bookDetails[0].trim());
			ps.setString(2, bookDetails[1].trim());
			ps.setString(3, bookDetails[2].trim());
	        ps.executeUpdate();
	        System.out.println("Borrower successfully added to the database..!");
		}
		catch(Exception e) {
			System.out.println("Operation failed for providing invalid data, error:"+ e);
		}
		return;
	}
	
	//Method for borrowing a book
	public void borrowBook() throws SQLException {
		System.out.println("Borrowing a book transaction..!\nPlease enter borrowerId, ISBN");
		String input = scanner.nextLine();
		try {
			String[] bookDetails = input.split(",");
			int borrowerId = Integer.parseInt(bookDetails[0].trim());
			String isbn = bookDetails[1].trim();
			BooksDto availableBook = this.findAvailableBook(isbn);
			if(availableBook == null) {
				System.out.println("No Book with ISBN: "+ isbn +" available for Borrowing..!");
				return;
			}
			PreparedStatement ps = connection.prepareStatement(
	        		"INSERT INTO Borrowing_Transactions (borrower_id, isbn, issn, borrowing_date)"
	        		+ "VALUES (?, ?, ?, ?)");
			ps.setInt(1, borrowerId);
			ps.setString(2, availableBook.getIsbn());
			ps.setString(3, availableBook.getIssn());
			ps.setString(4, LocalDateTime.now().toString());
	        ps.executeUpdate();
	        this.updateBookStatus(availableBook.getIsbn(), availableBook.getIssn() , "Not Available");
	        System.out.println("Borrow successful for book: "+isbn+ " borrower id: " + borrowerId);
		}
		catch(Exception e) {
			System.out.println("Operation failed for providing invalid data, error:"+ e);
		}
		return;
	}
	
	private BooksDto findAvailableBook(String isbn) throws SQLException {
		String query = "SELECT * FROM Books WHERE isbn = '"+ isbn +"' and status = 'Available'";
		PreparedStatement ps = connection.prepareStatement(query.toString());
        ResultSet res = ps.executeQuery();
        if(res.next()) {
        	BooksDto booksDto = new BooksDto();
        	booksDto.setIsbn(res.getString("isbn"));
        	booksDto.setTitle(res.getString("title"));
        	booksDto.setAuthor(res.getString("author"));
        	booksDto.setGenere(res.getString("genere"));
        	booksDto.setPublicationYear(res.getInt("publication_year"));
        	booksDto.setStatus(res.getString("status"));
        	booksDto.setIssn(res.getString("issn"));
        	return booksDto;
        }
        return null;
	}
	
	private void updateBookStatus(String isbn, String issn, String status) throws SQLException {
		String query = "SELECT * FROM Books WHERE isbn = '"+ isbn +"' and issn = '"+ issn +"'";
		PreparedStatement ps = connection.prepareStatement(query.toString());
        ResultSet res = ps.executeQuery();
        if(res.next()) {
    		String updateQuery = "UPDATE Books SET status = '"+ status +"' WHERE isbn = '"+ isbn +"' and issn = '"+ issn +"'";
    		PreparedStatement ps1 = connection.prepareStatement(updateQuery.toString());
            ps1.executeUpdate();
            return;
        }
        System.out.println("No Books found with ISBN: "+ isbn +" and ISSN: "+ issn +" to update status..!");
	}
	
	//Method for returning a book
	public void returnBook() {
		System.out.println("Returning the borrowed book transaction..!\nPlease enter borrowerId, ISBN and ISSN to return: ");
		String input = scanner.nextLine();
		try {
			String[] bookDetails = input.split(",");
			int borrowerId = Integer.parseInt(bookDetails[0].trim());
			String isbn = bookDetails[1].trim();
			String issn = bookDetails[2].trim();
			PreparedStatement ps = connection.prepareStatement(
	        		"UPDATE Borrowing_Transactions SET return_date = ? "
	        		+ "WHERE isbn = ? and issn = ? and borrower_id = ? and return_date is null");
			ps.setString(1, LocalDateTime.now().toString());
			ps.setString(2, isbn);
			ps.setString(3, issn);
			ps.setInt(4, borrowerId);
	        int res = ps.executeUpdate();
	        if(res == 0) {
	        	System.out.println("No Pending borrow transaction found to return the book..!\nPlease enter valid details");
	        	return;
	        }
	        this.updateBookStatus(isbn, issn , "Available");
	        System.out.println("Return successful for book: "+isbn+ " borrower id: " + borrowerId);
		}
		catch(Exception e) {
			System.out.println("Operation failed for providing invalid data, error:"+ e);
		}
		return;
	}
	
	//Method for book search
	public void searchBook() {
		System.out.println("Search for Book\nPlease enter ISBN, title, author, genere, publication year");
		String input = scanner.nextLine();
		String[] bookDetails = input.split(",");
		try {
			StringBuilder query = new StringBuilder("SELECT * FROM Books WHERE ");
			String isbn = bookDetails[0].trim();
			String title = bookDetails[1].trim();
			String author = bookDetails[2].trim();
			String genere = bookDetails[3].trim();
			String publicationYear = bookDetails[4].trim();
			
			List<String> conditions = new ArrayList<>();
			if(!isbn.isBlank()) {
				conditions.add("isbn = '" + isbn + "'");
			}
			if(!title.isBlank()) {
				conditions.add("title = '" + title+ "'");
			}
			if(!author.isBlank()) {
				conditions.add("author = '" + author+ "'");
			}
			if(!genere.isBlank()) {
				conditions.add("genere = '" + genere+ "'");
			}
			if(!publicationYear.isBlank()) {
				conditions.add("publication_year = " + publicationYear);
			}
			
			String query1 = query.toString() + conditions.stream().collect(Collectors.joining(" and "));		
			PreparedStatement ps = connection.prepareStatement(query1);
	        ResultSet res = ps.executeQuery();
	        
	        System.out.println("Books Retrieved with matching filters!");
        	System.out.println("ISBN\tTitle\tAuthor\tGenere\tPublication Year\tISSN\tStatus");
	        while (res.next()) {
                String resIsbn = res.getString("isbn");
                String resTitle = res.getString("title");
                String resAuthor = res.getString("author");
                String resGenere = res.getString("genere");
                String resPublicationYear = res.getString("publication_year");
                String resStatus = res.getString("status");
                String resIssn = res.getString("issn");
                
                System.out.println(resIsbn+"\t"+resTitle+"\t"+resAuthor+"\t"+resGenere+"\t"+resPublicationYear+"\t"+resIssn+"\t"+resStatus);
            }
	        System.out.println("\nOperation complete..!");
		}
		catch(Exception e) {
			System.out.println("Operation failed for providing invalid data, error:"+ e);
		}
		return;
		
		
	}
	
	//Method for borrowing history
	public void viewHistory() {
		System.out.println("View Borrowers history\nPlease enter Borrower id:");
		try {
			String borrowerId = scanner.nextLine();
			StringBuilder query = new StringBuilder("SELECT * FROM Borrowing_Transactions WHERE ");
			if(!borrowerId.isBlank()) {
				query.append("borrower_id = " + borrowerId);
			}
			else {
				throw new Error("Please provide valid borrower id");
			}
			
			PreparedStatement ps = connection.prepareStatement(query.toString());
	        ResultSet res = ps.executeQuery();
	        
	        System.out.println("Borrowing history retrieved with matching filters!");
	        System.out.println("Transaction Id\tBorrower Id\tISBN\tISSN\tBorrowing Date\t\tReturn Date");
	        while (res.next()) {
                String resIsbn = res.getString("isbn");
                String resTransId = res.getString("transaction_id");
                String resBorrowerId = res.getString("borrower_id");
                String resBorrowDt = res.getString("borrowing_date");
                String resReturnDt = res.getString("return_date");
                String resIssn = res.getString("issn");
                
                System.out.println(resTransId+"\t\t"+resBorrowerId+"\t\t"+resIsbn+"\t"+resIssn+"\t"+resBorrowDt+"\t"+resReturnDt);
            }
	        System.out.println("\nOperation complete..!");
		}
		catch(Exception e) {
			System.out.println("Operation failed for providing invalid data, error:"+ e);
		}
		return;
	}
}
