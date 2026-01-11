package LibraryManagementSystem.searcher;

import java.util.List;

import LibraryManagementSystem.book.BookCopy;
import LibraryManagementSystem.datataccessor.DBAccessor;
import LibraryManagementSystem.datataccessor.ResultConverter;
import LibraryManagementSystem.datataccessor.Results;

public class IDBaseBookSearcher implements BookSearcher{

	
	private int bookId;
	private final DBAccessor dbAccessor;
	
	public IDBaseBookSearcher(int bookId, DBAccessor dbAccessor) {
		super();
		this.bookId = bookId;
		this.dbAccessor = dbAccessor;
	}

	@Override
	public List<BookCopy> search() {
		// TODO Auto-generated method stub
		Results results = dbAccessor.getBooksWithBookID(bookId);
		
		return ResultConverter.convertToBookCopies(results);
	}
	

}
