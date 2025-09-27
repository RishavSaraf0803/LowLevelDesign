package LibraryManagementSystem.library;

import java.util.List;

import LibraryManagementSystem.book.BookCopy;
import LibraryManagementSystem.datataccessor.DBAccessor;
import LibraryManagementSystem.datataccessor.ResultConverter;
import LibraryManagementSystem.datataccessor.Results;
import LibraryManagementSystem.user.Member;

public class Library {
	
	
	private final DBAccessor dbAccessor;
	
	
	
	public Library(DBAccessor dbAccessor) {
		super();
		this.dbAccessor = dbAccessor;
	}
	public void addBookCopy(BookCopy bookCopy) {
		if(bookCopy == null) {
			//..
		}
		dbAccessor.insertBookCopy(bookCopy);
		
	}
	public  void deleteBookCopy(BookCopy bookCopy) {
		//
		//check if bookCopy is available
		//deletion
		if(dbAccessor.isCopyAvailable(bookCopy)) {
			dbAccessor.deleteBookCopy(bookCopy);
		}
	}
	
	public void blockMember(Member member) {
		
	}
	public void issueBook(BookCopy bookCopy, Member member) {
		
	}
	
	public void submitBook(BookCopy bookCopy, Member member) {
		
	}
	
	public Member getBorrower(BookCopy bookCopy) {
		Results results = dbAccessor.getBoorower(bookCopy);
		Member member = ResultConverter.convertToMember(results);
		return member;
	}
	public List<BookCopy> getBorrowerBooks(Member member){
		return null;
	}

}
