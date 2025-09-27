package LibraryManagementSystem.auth;

public class UserAuthenticator {
	
	public static boolean isAdmin(String token) {
		return true;
	}
	public static boolean  isMember(String token) {
		return false;
	}

}
