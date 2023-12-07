package assignment2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout.Group;

public class TwitterUserManager {

	private static int numTweets;
	private UserGroup group;
	
	private static HashMap<UserID, User> users = new HashMap<>(); //key: UserID, value: associated User object

	private static HashMap<User, DefaultListModel<String>> usersFeedModel = new HashMap<>(); //key : user, value: user's DefaultListModel
	
	private static HashMap<User, List<String>> usersTweets = new HashMap<>(); //key : user, value: List of all tweets user has posted
	private static HashMap<User, List<String>> usersFeed = new HashMap<>(); //key : user, value: List of all tweets from all users followed
	
	
	private static HashMap<User, List<User>> followers = new HashMap<>(); //key : user, value : List of users that follow key[user]
	private static HashMap<User, List<User>> following = new HashMap<>(); //key : user, value : List of users that the key[user] is following
	
	private static HashMap<GroupID, CompositeGroup> groups = new HashMap<>();
	private static HashMap<CompositeGroup, List<User>> groupRoster = new HashMap<>();
	
	private static TwitterUserManager instance;
	
	public static TwitterUserManager getInstance() {
		
		if(instance == null) {
			instance = new TwitterUserManager();
		}
		
		return instance;
	}
	
	public static int getNumTweets() {
		return numTweets;
	}
	
	// User Methods
	public static int getNumUsers() {
		return users.size();
	}
	
	public static User addUser(String groupname, String username) {
		
        UserID userID = new UserID(username);
        if (users.containsKey(userID)) {
        	
            System.out.println("User with this UserID already exists");
            
        } else {
        	
            User user = new User(userID);
            users.put(userID, user);
            
    		if(groupname != null) {
    			// add User to CompositeGroup if the group exists
    			if(groupExists(new GroupID(groupname))) {
    				joinGroup(getGroup(groupname), user);
    				
    			}
    		} 
            return user;
        }
        return null;
	}
	
	public static User getUser(String username) {
		User user = null;
		
		UserID id = new UserID(username);
		if(users.containsKey(id)) {
			user = users.get(id);
		}

		return user;		
	}
	
	public static HashMap<UserID, User> getUsers(){
		return users;
	}
	
	public static boolean userExists(UserID ID) {
		if(users.containsKey(ID)) {
			return true;
			
		} else {
			
			return false;
		}
	}

	public static void updateUsers(User user) {
		user.setLastUpdateTime(System.currentTimeMillis());
	}
	
	public static void addFollower(User user, User follower) {
		
		if(followers.containsKey(user)) {
			
			List<User> currentFollowing = followers.get(user);
			currentFollowing.add(follower);
			followers.put(user, currentFollowing);
			
		} else {
			
			List<User> currentFollowing = new ArrayList<>();
			currentFollowing.add(follower);
			followers.put(user, currentFollowing);
		}
		
		addFollowing(follower, user);
		
		user.getFeedService().follow(new Follower(follower));
	}
	
	public static List<User> getFollowers(User user){
		return followers.get(user);
	}
	
	public static void addFollowing(User user, User userFollowed) {
		
		if(following.containsKey(user)) {
			
			List<User> currentFollowing = following.get(user);
			currentFollowing.add(userFollowed);
			following.put(user, currentFollowing);
			
		} else {
			
			List<User> currentFollowing = new ArrayList<>();
			currentFollowing.add(userFollowed);
			following.put(user, currentFollowing);
		}
		
		// add all tweets of userFollowed to user's feed
		if(usersFeed.get(user) != null && getUsersTweets(userFollowed) != null) {
			
			for(String tweet : getUsersTweets(userFollowed)) {
				addUsersFeedTweet(user, tweet);
			}
		}
	}
	
	public static List<User> getFollowing(User user){
		return following.get(user);
	}
	
	public static void addUserTweet(User user, String tweet) {
	
		numTweets++;
		
		if(getUsersTweets(user) != null) {
			
			List<String> tweets = usersTweets.get(user);
			tweets.add(tweet);
			usersTweets.put(user, tweets);
			
		} else {
			
			List<String> tweets = new ArrayList<>();
			tweets.add(tweet);
			usersTweets.put(user, tweets);
		}
	}
	
	public static List<String> getUsersTweets(User user){
		return usersTweets.get(user);
	}
	
	public static void addUsersFeedTweet(User user, String tweet) {
		
		List<String> feed = usersFeed.get(user);
		
		if(feed !=null) {

			List<String> tweets = feed;
			tweets.add(tweet);
			usersFeed.put(user, tweets);

		} else {
			
			List<String> tweets = new ArrayList<>();
			tweets.add(tweet);
			usersFeed.put(user, tweets);
		}
	}
	
	public static List<String> getUsersFeed(User user){
		return usersFeed.get(user);
	}

	public static void addUserModel(User user, DefaultListModel<String> model) {
		usersFeedModel.put(user, model);
	}
	
	public static DefaultListModel<String> getUserModel(User user) {
		return usersFeedModel.get(user);
	}
	
	/**
	 * Group methods
	 */
	public static int getNumGroups() {
		return groups.size();
	}

	public static HashMap<GroupID, CompositeGroup> getGroups() {
		return groups;
	}
	
	public static CompositeGroup addGroup(String groupname, String targetGroupname) {
		
        GroupID groupID = new GroupID(groupname);
        if (groups.containsKey(groupID)) {
            System.out.println("group with this groupID already exists");
            
        } else {
            CompositeGroup group = new CompositeGroup(groupID);
            groups.put(groupID, group);
            return group;
        }

        return null;
	}
	
	public static CompositeGroup getGroup(String groupname) {
		CompositeGroup group = null;
		
		GroupID gid = new GroupID(groupname);
		if(groups.containsKey(gid)) {
			group = groups.get(gid);
		}

		return group;	
	}
	
	public static boolean groupExists(GroupID GID) {
		if(groups.containsKey(GID)) {
			return true;
			
		} else {
			
			return false;
		}
	}
	
	public static void joinGroup(CompositeGroup group, User member) {
		if(groups.containsKey(group.getGID())) {
			
			List<User> currentRoster = groupRoster.get(group);
			currentRoster.add(member);
			groupRoster.put(group, currentRoster);
			
		} else {
			
			List<User> currentRoster = new ArrayList<>();
			currentRoster.add(member);
			groupRoster.put(group, currentRoster);
		}
	}
	
	public static List<User> getGroupMembers(CompositeGroup group){ 
		return groupRoster.get(group);
	}
	
	public static  HashMap<CompositeGroup, List<User>> getGroupRoster(){
		return groupRoster;
	}
	
	public int gatherAllGroupMembers() {
		return group.getNumberGroupMembers();
	}
	
}
