package assignment2;

public class PositivePercentageButton extends StatisticButton{

	@Override
	public void accept(StatisticVisitor visitor) {
		visitor.visitPositivePercentage(this);
	}

}
