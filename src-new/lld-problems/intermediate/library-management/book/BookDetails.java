package LibraryManagementSystem.book;

import java.util.Date;
import java.util.List;

public class BookDetails {

	private final String bookName;
	private final Date publicationDate;
	private final List<String> authors;
	public BookDetails(String bookName, Date publicationDate, List<String> authors) {
		super();
		this.bookName = bookName;
		this.publicationDate = publicationDate;
		this.authors = authors;
	}
	
	
}
