package LibraryManagementSystem.searcher;
import java.util.List;

import LibraryManagementSystem.book.BookCopy;
import LibraryManagementSystem.datataccessor.DBAccessor;
import LibraryManagementSystem.datataccessor.ResultConverter;
import LibraryManagementSystem.datataccessor.Results;
import LibraryManagementSystem.tester.*;
public class NameBaseBookSearcher implements BookSearcher {
	private String bookName;
	private final DBAccessor dbAccessor;
	
	public NameBaseBookSearcher(String name, DBAccessor dbAccessor){
		this.bookName = name;
		this.dbAccessor = dbAccessor;
	}
	
	public List<BookCopy> search(){
		Results results = dbAccessor.getBooksWithName(bookName);
		ResultConverter resultConverter = new ResultConverter();
		List<BookCopy> bookCopies = resultConverter.convertToBookCopies(results);
		return bookCopies;
	}

}
