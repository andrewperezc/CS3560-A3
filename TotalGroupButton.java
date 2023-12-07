package assignment2;

public class TotalGroupButton extends StatisticButton{

	@Override
	public void accept(StatisticVisitor visitor) {
		visitor.visitTotalGroup(this);
	}

}
