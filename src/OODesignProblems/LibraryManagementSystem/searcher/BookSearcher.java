
package LibraryManagementSystem.searcher;
import java.util.List;

import LibraryManagementSystem.book.BookCopy;
import LibraryManagementSystem.tester.*;

public interface BookSearcher {

	public List<BookCopy> search();
}
