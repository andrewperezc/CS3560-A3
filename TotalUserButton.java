package assignment2;

public class TotalUserButton extends StatisticButton{

	@Override
	public void accept(StatisticVisitor visitor) {
		visitor.visitTotalUser(this);		
	}

}
