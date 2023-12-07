package assignment2;

public interface Visitor {

	void visitTotalUser(TotalUserButton tub);
	
	void visitTotalGroup(TotalGroupButton tgb);
	
	void visitTotalTweet(TotalTweetButton ttb);
	
	void visitPositivePercentage(PositivePercentageButton ppb);

	void visitValidUserGroup(ValidUserGroupButton vugb);

	void visitFindLastUpdated(FindLastUpdatedButton flub);
}
