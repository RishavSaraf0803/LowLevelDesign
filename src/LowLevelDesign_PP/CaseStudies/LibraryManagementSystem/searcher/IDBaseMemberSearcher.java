package LibraryManagementSystem.searcher;

import java.util.List;

import LibraryManagementSystem.datataccessor.DBAccessor;
import LibraryManagementSystem.datataccessor.ResultConverter;
import LibraryManagementSystem.datataccessor.Results;
import LibraryManagementSystem.user.Member;

public class IDBaseMemberSearcher implements MemberSearcher {

	private final int memberId;
	private final DBAccessor dbAccessor;

	public IDBaseMemberSearcher(int memberId, DBAccessor dbAccessor) {
		super();
		this.memberId = memberId;
		this.dbAccessor = dbAccessor;
	}
	public List<Member> search() {
		Results result = dbAccessor.getMembersWithID(memberId);
		
		return ResultConverter.convertToMembers(result);
	}
}
