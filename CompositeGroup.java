package assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CompositeGroup implements UserGroup{
	private GroupID GID;
	private List<UserGroup> subgroups = new ArrayList<>();
	
	public CompositeGroup(GroupID GID, UserGroup...groups) {
		this.GID = GID;
		subgroups.addAll(Arrays.asList(groups));
	}
	
	public GroupID getGID() {
		return this.GID;
	}
	
	public int getNumberGroupMembers(){
		return subgroups.stream().mapToInt(UserGroup :: getNumberGroupMembers).sum();
	}
	
}
