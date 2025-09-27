package LibraryManagementSystem.searcher;

import java.util.List;

import LibraryManagementSystem.book.BookCopy;
import LibraryManagementSystem.datataccessor.DBAccessor;
import LibraryManagementSystem.datataccessor.ResultConverter;
import LibraryManagementSystem.datataccessor.Results;

public class AuthorBaseBookSearcher  implements BookSearcher{
	
	
	private List<String> authorNames;
	
	private final DBAccessor dbAccessor;

	public AuthorBaseBookSearcher(List<String> authorNames, DBAccessor dbAccessor) {
		super();
		this.authorNames = authorNames;
		this.dbAccessor = dbAccessor;
	}



	@Override
	public List<BookCopy> search() {
		// TODO Auto-generated method stub
		Results results = dbAccessor.getBooksWithAuthorName(authorNames);
		//ResultConverter resultConverter = new ResultConverter();
		return ResultConverter.convertToBookCopies(results);
		


	}

}
