package lms;

import java.time.LocalDateTime;

public class BorrowingTransactionsDto {
	private int transactionId;
	private int borrowerId;
	private String isbn;
	private String issn;
	private LocalDateTime borrowingDate;
	private LocalDateTime returnDate;
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public int getBorrowerId() {
		return borrowerId;
	}
	public void setBorrowerId(int borrowerId) {
		this.borrowerId = borrowerId;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getIssn() {
		return issn;
	}
	public void setIssn(String issn) {
		this.issn = issn;
	}
	public LocalDateTime getBorrowingDate() {
		return borrowingDate;
	}
	public void setBorrowingDate(LocalDateTime borrowingDate) {
		this.borrowingDate = borrowingDate;
	}
	public LocalDateTime getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(LocalDateTime returnDate) {
		this.returnDate = returnDate;
	}
}