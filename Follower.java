package assignment2;

public class Follower {
	private final User follower;
	
	public Follower(User user) {
		this.follower = user;
	}
	
	public void update(String tweet) {
		TwitterUserManager.addUsersFeedTweet(follower, tweet);
		TwitterUserManager.getUserModel(follower).addElement(tweet);
		TwitterUserManager.updateUsers(follower);
	}
}
