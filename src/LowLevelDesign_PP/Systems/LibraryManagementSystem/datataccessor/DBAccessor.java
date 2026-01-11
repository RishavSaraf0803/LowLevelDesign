package LibraryManagementSystem.datataccessor;

import java.util.List;

import LibraryManagementSystem.book.BookCopy;
import LibraryManagementSystem.user.Member;

public class DBAccessor {

	public Results getBooksWithName(String bookName) {
		return null;
		
	}
	public Results getBooksWithAuthorName(List<String> authors) {
		return null;
	}
	
	public Results getBooksWithBookID(int bookId) {
		return null;
		
	}
	public Results getMembersWithName(String memberName) {
		return null;
		
	}
	
	public Results getMembersWithID(int memberId) {
		return null;
		
	}
	public void insertBookCopy(BookCopy bookCopy) {
		
	}
	public void deleteBookCopy(BookCopy bookCopy) {
		
	}
	public void markAsBlocked(Member member) {
		
	}
	public void issueBookCopyToMember(BookCopy bookCopy, Member member) {
		
	}
	public void submitBookCopyFromMember(BookCopy bookCopy, Member member) {
		
	}
	
	public boolean isCopyAvailable(BookCopy bookCopy) {
		return false;
		
	}
	public Results getBoorower(BookCopy bookCopy) {
		return null;
		
	}
	
	
}
