package assignment2;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class StatisticVisitor implements Visitor{
	
	public void getStatistics(StatisticButton button) {
		button.accept(this);
	}
    
    private void showPopupWindow(String title, String contentText) {
        JFrame popupFrame = new JFrame(title);
        popupFrame.setSize(420, 240);
        popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel popupPanel = new JPanel(new BorderLayout());
        popupPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        popupPanel.add(new JLabel(contentText), BorderLayout.NORTH);

        popupFrame.add(popupPanel);
        popupFrame.setVisible(true);
    }

	@Override
	public void visitTotalUser(TotalUserButton tub) {
		showPopupWindow("Total Users", "" + TwitterUserManager.getNumUsers());
	}

	@Override
	public void visitTotalGroup(TotalGroupButton tgb) {
		showPopupWindow("Total Users", "" + TwitterUserManager.getNumGroups());
	}

	@Override
	public void visitTotalTweet(TotalTweetButton ttb) {
		showPopupWindow("Total number of Tweets", "" + TwitterUserManager.getNumTweets());
	}

	@Override
	public void visitPositivePercentage(PositivePercentageButton ppb) {
		double numPos = 0;
		
		for(Map.Entry<UserID, User> entry : TwitterUserManager.getUsers().entrySet()) {
			
				User user = entry.getValue();
				List<String> usersTweets = TwitterUserManager.getUsersTweets(user);
				
				if(usersTweets != null) {
					
					for(String tweet : usersTweets) {
						
						tweet.toLowerCase();
						
						if(tweet.contains("happy") || tweet.contains("glad") || tweet.contains("good")) {
							numPos++;
						}
					}
				}
		}
		double percentPos = numPos * 100 / TwitterUserManager.getNumTweets();
		
		showPopupWindow("Total number of Tweets",  percentPos + "%");
	}

	@Override
	public void visitValidUserGroup(ValidUserGroupButton vugb) {
		String usersValid = "valid.";
		String groupsValid = "valid.";

		for(UserID id : TwitterUserManager.getUsers().keySet()) {
			if(id.getUsername().contains(" ")){
				usersValid = "not valid.";
			}
		}

		for(GroupID gid : TwitterUserManager.getGroups().keySet()) {
			if(gid.getGroupname().contains(" ")){
				groupsValid = "not valid.";
			}
		}

		showPopupWindow("Validity Check", "Users are " + usersValid + " Groups are " + groupsValid);
	}	
	
	@Override
	public void visitFindLastUpdated(FindLastUpdatedButton flub) {
		long mostRecentTime = 0;
		String mostRecentlyUpdatedUsername = "";
		boolean isAnUpdatedUser = false;

		for(UserID id : TwitterUserManager.getUsers().keySet()) {

			String username = id.getUsername();
			long time = TwitterUserManager.getUsers().get(id).getLastUpdateTime();

			if(time > mostRecentTime){
				mostRecentTime = time;
				mostRecentlyUpdatedUsername = username;
				isAnUpdatedUser = true;
			}
		}

		if(isAnUpdatedUser){
			showPopupWindow("Last Updated User", "The most recently updated user is User " + mostRecentlyUpdatedUsername);
		} else {
			showPopupWindow("Last Updated User", "There are no updated users.");
		}
		
	}
	
	
}
