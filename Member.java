package assignment2;

public abstract class Member implements UserGroup{
	protected User user;	
	
	public Member(UserID ID) {
		user = new User(ID);
	}
}
