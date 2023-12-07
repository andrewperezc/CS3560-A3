package assignment2;

public class FindLastUpdatedButton extends StatisticButton{

	@Override
	public void accept(StatisticVisitor visitor) {
		visitor.visitFindLastUpdated(this);
	}

}