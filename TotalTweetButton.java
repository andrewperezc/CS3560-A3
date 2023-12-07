package assignment2;

public class TotalTweetButton extends StatisticButton{

	@Override
	public void accept(StatisticVisitor visitor) {
		visitor.visitTotalTweet(this);
	}

}
