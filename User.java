package assignment2;


public class User implements UserGroup{
	
	private TwitterFeed feed;
	private UserID ID;
	private long creationTime;
	private long lastUpdateTime;
	
	public User(UserID ID) {
		this.ID = ID;
		this.creationTime = System.currentTimeMillis();
		this.lastUpdateTime = 0;
		this.feed = new TwitterFeed();
		TwitterUserManager.addFollower(this, this);
		System.out.println(ID.getUsername() + "'s creation time: " + creationTime);
	}
	
	public void newTweet(String tweet) {

		TwitterUserManager.addUserTweet(this, tweet);
		this.feed.notifyFollowers(tweet);
	}
	
	public TwitterFeed getFeedService() {
		return this.feed;
	}
	
	public UserID getID() {
		return this.ID;
	}
	
	public int getNumberGroupMembers() {
		return 1;
	}

	public long getCreationTime() {
		return this.creationTime;
	}

	public long getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(long time){
		this.lastUpdateTime = time;
	}
	

}
