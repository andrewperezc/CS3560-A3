package assignment2;

import java.util.ArrayList;
import java.util.List;

public class TwitterFeed {
	private List<Follower> followers;
	
	public TwitterFeed() {
		followers = new ArrayList<>();
	}
	
	public void follow(Follower follower) {
		followers.add(follower);
	}
	
	public void notifyFollowers(String tweet) {	
		followers.forEach(follower -> follower.update(tweet));
	}
}
