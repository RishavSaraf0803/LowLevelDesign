package LibraryManagementSystem.searcher;

import java.util.List;

import LibraryManagementSystem.datataccessor.DBAccessor;
import LibraryManagementSystem.datataccessor.ResultConverter;
import LibraryManagementSystem.datataccessor.Results;
import LibraryManagementSystem.user.Member;

public class NameBaseMemberSearcher implements MemberSearcher{

	private String memberName;

	private final DBAccessor dbAccessor;

	public NameBaseMemberSearcher(String memberName, DBAccessor dbAccessor) {
		super();
		this.memberName = memberName;
		this.dbAccessor = dbAccessor;
	}

	@Override
	public List<Member> search() {
		// TODO Auto-generated method stub
		Results result = dbAccessor.getMembersWithName(memberName);
		ResultConverter resultconverter = new ResultConverter();
		List<Member> members = resultconverter.convertToMembers(result);
		return members;
	}
	
	
}
