package assignment2;

public class ValidUserGroupButton extends StatisticButton {

    @Override
    public void accept(StatisticVisitor visitor) {
        visitor.visitValidUserGroup(this);
    }
}
