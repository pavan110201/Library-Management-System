CREATE DATABASE lms;
use lms;

CREATE TABLE Books(
isbn varchar(15) not null,
title varchar(50),
author varchar(50),
genere varchar(25),
publication_year int,
issn varchar(15) not null,
status varchar(25),
PRIMARY KEY(isbn, issn)
);
INSERT INTO Books (isbn, title, author, genere, publication_year, issn, status) 
VALUES 
('100001', '1984', 'George Orwell', 'Dystopian Fiction', 1949, '1001', 'Available'),
('100001', '1984', 'George Orwell', 'Dystopian Fiction', 1949, '1002', 'Available'),
('100002', 'To Kill a Mockingbird', 'Harper Lee', 'Fiction', 1960, '1001', 'Available'),
('100002', 'To Kill a Mockingbird', 'Harper Lee', 'Fiction', 1960, '1002', 'Available'),
('100002', 'To Kill a Mockingbird', 'Harper Lee', 'Fiction', 1960, '1003', 'Available'),
('100002', 'To Kill a Mockingbird', 'Harper Lee', 'Fiction', 1960, '1004', 'Available'),
('100002', 'To Kill a Mockingbird', 'Harper Lee', 'Fiction', 1960, '1005', 'Available'),
('100003', 'The Catcher in the Rye', 'J.D. Salinger', 'Fiction', 1951, '1003', 'Available'),
('100004', 'The Book Thief', 'Markus Zusak', 'Historical Fiction', 2005, '1004', 'Available'),
('100005', 'The Great Gatsby', 'F. Scott Fitzgerald', 'Fiction', 1925, '1005', 'Available'),
('100006', 'The Bell Jar', 'Sylvia Plath', 'Autobiographical Fiction', 1963, '1006', 'Available'),
('100007', 'Lord of the Flies', 'William Golding', 'Allegorical Novel', 1954, '1007', 'Available'),
('100008', 'The Road', 'Cormac McCarthy', 'Post-apocalyptic Fiction', 2006, '1008', 'Available'),
('100009', 'The Kite Runner', 'Khaled Hosseini', 'Fiction', 2003, '1009', 'Available'),
('100010', 'Pride and Prejudice', 'Jane Austen', 'Romance', 1813, '1010', 'Available');


CREATE TABLE Borrowers(
borrower_id int AUTO_INCREMENT,
name varchar(50),
contact varchar(10),
email varchar(50),
PRIMARY KEY(borrower_id)
);

INSERT INTO Borrowers (name, contact, email) 
VALUES 
('Pavan', '9998881010', 'pavan@gmail.com'),
('John', '6822089690', 'john@gmail.com'),
('Kevin', '9998881011', 'kevin@gmail.com'),
('Nancy', '9998881012', 'nancy@gmail.com'),
('Kim', '9998881013', 'kim@gmail.com');


CREATE TABLE Borrowing_Transactions(
transaction_id int AUTO_INCREMENT,
borrower_id int,
isbn varchar(15),
issn varchar(15),
borrowing_date datetime,
return_date datetime,
PRIMARY KEY(transaction_id),
FOREIGN KEY (borrower_id) REFERENCES Borrowers(borrower_id),
FOREIGN KEY (isbn, issn) REFERENCES Books(isbn, issn)
);

INSERT INTO Borrowing_Transactions (borrower_id, isbn, issn, borrowing_date, return_date) 
VALUES 
('1','100001','1001',now(),now()),
('2','100002','1002',now(),now()),
('2','100003','1003',now(),now()),
('4','100004','1004',now(),now()),
('5','100005','1005',now(),now());




