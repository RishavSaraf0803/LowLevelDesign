package LibraryManagementSystem.tester;

import java.util.Date;
import java.util.List;

import LibraryManagementSystem.auth.UserAuthenticator;
import LibraryManagementSystem.book.BookCopy;
import LibraryManagementSystem.book.BookDetails;
import LibraryManagementSystem.datataccessor.DBAccessor;
import LibraryManagementSystem.library.Library;
import LibraryManagementSystem.searcher.*;
import LibraryManagementSystem.searcher.NameBaseBookSearcher;
import LibraryManagementSystem.user.Member;

public class Tester {

	private final DBAccessor dbAccessor;
	
	
	public Tester(DBAccessor dbAccessor) {
		super();
		this.dbAccessor = dbAccessor;
	}

	public List<BookCopy> searchBooksByBookName(String bookName){
		if(bookName == null) {
			throw new IllegalArgumentException("Book Name can't be null");
		}
		BookSearcher bookSearcher = new NameBaseBookSearcher(bookName,dbAccessor);
		return bookSearcher.search();
	}
	
	public List<BookCopy> searchBooksByAuthorName(List<String> authorName){
		if(authorName.size() == 0) {
			throw new IllegalArgumentException("Author name can't be empty");
		}
				BookSearcher bookSearcher = new AuthorBaseBookSearcher(authorName, dbAccessor);
				return bookSearcher.search();
	}
	
	public List<BookCopy> searchBooksByBookId(int bookId){
		if(bookId <= 0) {
			throw new IllegalArgumentException("Book Id is not valid");
		}
		List<BookCopy> bookCopy = new IDBaseBookSearcher(bookId,dbAccessor).search();
		return bookCopy;
	}
	
	public Member searchMemberByMemberName(String memberName,String adminToken) throws IllegalAccessException{
		if(!UserAuthenticator.isAdmin(adminToken)) {
			throw new IllegalAccessException("Operation forbidden");
		}
		
		if(memberName == null) {
			throw new IllegalArgumentException("Member name can't be null");
		}
		MemberSearcher memberSearcher = new NameBaseMemberSearcher(memberName, dbAccessor);
		return memberSearcher.search().get(0);
	}
	public void addMember(String  memberName, Date memberShipDate, String adminToken) throws IllegalAccessException {
		
		if(!UserAuthenticator.isAdmin(adminToken)) {
			throw new IllegalAccessException("Operation forbidden");
		}
		MemberAdder memberAdder = new MemberAdder(memberName, memberShipDate);
		memberAdder.addMember();
		return;
		
	}
	
	public void blockMember(int  memberId, String adminToken) throws IllegalAccessException {
		if(memberId <= 0 || !UserAuthenticator.isAdmin(adminToken)) {
			throw new IllegalAccessException("Operation Forbidden");
		}
		List<Member> member = new IDBaseMemberSearcher(memberId, dbAccessor).search();
		new Library().blockMember(member.get(0));
	}
	
	public void addBook(String bookName, Date publicationDate, List<String> authorNames, String adminToken) throws IllegalAccessException {
		if(!UserAuthenticator.isAdmin(adminToken)) {
			throw new IllegalAccessException("Operation forbidden");
		}
		BookCopy bookCopy = new BookCopy(new BookDetails(bookName, publicationDate,authorNames), IDGenerator.getUniqueId());
		new Library(dbAccessor).addBookCopy(bookCopy);
	}
	
	public void deleteBook(int bookCopyId, String adminToken) throws IllegalAccessException {
		if(bookCopyId <= 0 || !UserAuthenticator.isAdmin(adminToken)) {
			throw new IllegalAccessException("Operation forbidden");
		}
		
		BookSearcher bookSearcher = new IDBaseBookSearcher(bookCopyId, dbAccessor);
		List<BookCopy> bookCopies = bookSearcher.search();
		new Library().deleteBookCopy(bookCopies.get(0));
	}
	
	public void issueBook(int bookCopyId, int memberId, String adminToken) throws IllegalAccessException {
		if(bookCopyId <= 0 || memberId <= 0 ||  !UserAuthenticator.isAdmin(adminToken)) {
			throw new IllegalAccessException("Operation forbidden");
		}
		BookSearcher bookSearcher = new IDBaseBookSearcher(bookCopyId);
		List<BookCopy> bookCopy = bookSearcher.search();
		
		if(bookCopy == null || bookCopy.size() == 0) {
			throw new RuntimeException("No book copies retrieved for given Id");
		}
		MemberSearcher memberSearcher = new IDBaseMemberSearcher(memberId);
		List<Member> members = memberSearcher.search();
		if(members == null || members.size() == 0) {
			throw new RuntimeException("No member retrieved for given Id");
		}
		new Library().issueBook(bookCopy.get(0), members.get(0));
		
	}
	public void returnBook(int bookCopyId, int memberId, String adminToken) throws IllegalAccessException {
		if(bookCopyId <= 0 || memberId <= 0 ||  !UserAuthenticator.isAdmin(adminToken)) {
			throw new IllegalAccessException("Operation forbidden");
		}
		BookSearcher bookSearcher = new IDBaseBookSearcher(bookCopyId);
		List<BookCopy> bookCopy = bookSearcher.search();
		if(bookCopy == null || bookCopy.size() == 0) {
			throw new RuntimeException("No book copies retrieved for given Id");
		}
		MemberSearcher memberSearcher = new IDBaseMemberSearcher(memberId);
		List<Member> members = memberSearcher.search();
		if(members == null || members.size() == 0) {
			throw new RuntimeException("No member retrieved for given Id");
		}
			new Library().submitBook(bookCopy.get(0), members.get(0));
	}
	public Member getBorrowerOfBooks(int bookCopyId, String adminToken) throws IllegalAccessException {
		if(bookCopyId <= 0 || !UserAuthenticator.isAdmin(adminToken)) {
			throw new IllegalAccessException("Operation Forbidden");
		}
		List<BookCopy> bookCopies = new IDBaseBookSearcher(bookCopyId).search();
		BookCopy bookCopy = bookCopies.get(0);
		return new Library().getBorrower(bookCopy);
		
	}
	
	public List<BookCopy> getBooksBorrowedByMember(int memberId, String adminToken) throws IllegalAccessException{
		if(memberId <= 0 || !UserAuthenticator.isAdmin(adminToken)) {
			throw new IllegalAccessException("Operation Forbidden");
		}
		List<Member> member = new IDBaseMemberSearcher(memberId).search();
		List<BookCopy> bookCopies = new Library().getBorrowerBooks(member.get(0));
		return bookCopies;
	}
	
}
