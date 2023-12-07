package assignment2;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class UserView extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<String> followingListModel;
	private DefaultListModel<String>  tweetFeedListModel;
	
	private User currentUser;
	
	private JFrame userViewFrame;
	private JTextField userIdTextArea;
	private JTextField tweetMessageTextArea;
	private JButton followUserButton;
	private JButton postTweetButton;
	private JList<String> followingList;
	private JPanel tweetPanel;
	
	public void handleOpenUserViewButton(String username) {
		
		if(username == null || username.equals("Root") || username.contains("Group")) {
			System.out.println("No user selected.");
			return;
		}
		
		this.currentUser = TwitterUserManager.getUser(username);
		
        userViewFrame = new JFrame(username + "'s Twitter");
        userViewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userViewFrame.setSize(400, 445);

        // "User Id" text area
        userIdTextArea = new JTextField();
        userIdTextArea.setPreferredSize(new Dimension(220, 35));

        // "Follow User" button
        followUserButton = new JButton("Follow User");

        // "Current Following" list view
        this.followingListModel = new DefaultListModel<>();
        List<User> followedUsersList = TwitterUserManager.getFollowing(currentUser);
        
        if(followedUsersList != null && followedUsersList.size()>1) {
        	for(User followedUser : followedUsersList) {
        		String name = followedUser.getID().getUsername();
        		
        		if(name != currentUser.getID().getUsername()) {
        			
        			followingListModel.addElement(name);
        		}
            }
        }
        
        followingList = new JList<>(followingListModel);
        followingList.setPreferredSize(new Dimension(250,400));
        
        JScrollPane followingScrollPane = new JScrollPane(followingList);
        followingScrollPane.setWheelScrollingEnabled(true);
        followingScrollPane.setAutoscrolls(true);

        // "Tweet Message" text area
        tweetMessageTextArea = new JTextField();
        tweetMessageTextArea.setPreferredSize(new Dimension(220, 35));

        // "Post Tweet" button
        postTweetButton = new JButton("Post Tweet");

        // "Tweet Feed" list view
        tweetFeedListModel = new DefaultListModel<>();
        
        List<String> tweetList = TwitterUserManager.getUsersFeed(currentUser);
        
        if(tweetList != null) {
            for(String tweet : tweetList) {
            	tweetFeedListModel.addElement(tweet);
            } 
        }
        
        JList<String> tweetFeedList = new JList<>(tweetFeedListModel);
        tweetFeedList.setPreferredSize(new Dimension(250,400));
        
        JScrollPane tweetFeedScrollPane = new JScrollPane(tweetFeedList);
        tweetFeedScrollPane.setWheelScrollingEnabled(true);
        tweetFeedScrollPane.setAutoscrolls(true);

        // Create the layout for the User View
        JPanel userViewPanel = new JPanel();

        // Create the top panel for "User Id" and "Follow User" components
        JPanel topPanel = new JPanel();
        topPanel.add(userIdTextArea);
        topPanel.add(followUserButton);

        // Add the top panel to the User View layout
        userViewPanel.add(topPanel);

        // Create the panel for "Current Following" component
        JPanel followingPanel = new JPanel();
        followingPanel.add(followingScrollPane);

        // Add the following panel to the User View layout
        userViewPanel.add(followingPanel);

        // Create the panel for "Tweet Message" and "Post Tweet" components
        tweetPanel = new JPanel();
        tweetPanel.add(tweetMessageTextArea);
        tweetPanel.add(postTweetButton);

        // Add the tweet panel to the User View layout
        userViewPanel.add(tweetPanel);

        // Create the panel for "tweet Feed" component
        JPanel tweetFeedPanel = new JPanel();
        tweetFeedPanel.setSize(500, 500);
        tweetFeedPanel.add(tweetFeedScrollPane);
        
        // Add the tweet feed panel to the User View layout
        userViewPanel.add(tweetFeedPanel);
        

        userViewFrame.add(userViewPanel);
        userViewFrame.setVisible(true);
       
        
        this.followUserButton.addActionListener(e -> this.handleFollowUserButton(this.userIdTextArea.getText()));
        this.postTweetButton.addActionListener(e -> this.handlePostTweetButton(this.tweetMessageTextArea.getText()));
        
        TwitterUserManager.addUserModel(currentUser, tweetFeedListModel);
    }
	
	private void handleFollowUserButton(String targetUserID) {
		
    	User targetUser = TwitterUserManager.getUser(targetUserID);
    	
    	// targetUser doesn't match a registered User
    	if(targetUser == null) {
    		System.out.println("There exists no user '" + targetUserID +"'. "
    				+ "Please enter the UserID of a current user. ");
    		this.userIdTextArea.setText("");
    		return;
    	} 
    	
    	// User tries to follow themself	
    	if(targetUser == this.currentUser) {
    		System.out.println("Users cannot follow themselves.");
    		userIdTextArea.setText("");
    		return;
    	}
    	
    	// targetUser does not have followers
    	if(TwitterUserManager.getFollowers(targetUser) == null) {
    	
    		// targetUser is not already followed by currentUser
    		System.out.println(currentUser.getID().getUsername() + " now follows " + targetUserID + "!");
               	
           	TwitterUserManager.addFollower(targetUser, currentUser);
            followingListModel.addElement(targetUserID);
           	
           	this.userIdTextArea.setText("");
            return;
            
    	} else {
        	// currentUser tries to follow the same person twice
        	if(TwitterUserManager.getFollowers(targetUser).contains(currentUser)) {	
        	
        		System.out.println("Cannot follow a user more than once.");
        		userIdTextArea.setText("");
    			return;	
        	} else {
           		// targetUser is not already followed by currentUser
               	TwitterUserManager.addFollower(targetUser, this.currentUser);
                followingListModel.addElement(targetUserID);
               	
                System.out.println(currentUser.getID().getUsername() + " now follows " + targetUserID + "!");
               	userIdTextArea.setText("");
                return;
        	}
    	}
    }

    private void handlePostTweetButton(String tweet) {
    	
    	if(tweet.length() < 1) {
    		System.out.println("Tweet must contain characters.");
    		return;
    	}

    	List<DefaultListModel<String>> followersModel = new ArrayList<>();
    	
    	for(User follower : TwitterUserManager.getFollowers(currentUser)) {
    		followersModel.add(TwitterUserManager.getUserModel(follower));
    	}
    	
    	this.currentUser.newTweet(currentUser.getID().getUsername() + " : " + tweet);
        System.out.println("Updated time : " + System.currentTimeMillis());
    	
    	System.out.println(currentUser.getID().getUsername() + " posted a tweet.");
    	this.tweetMessageTextArea.setText("");
    }
    
    
    

}
