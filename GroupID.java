package assignment2;

import java.util.Objects;

public class GroupID {
	private String groupname;
	
	public GroupID(String groupname) {
		this.groupname = groupname;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }
        GroupID groupID = (GroupID) o;
        
        return Objects.equals(groupname, groupID.groupname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupname);
    }
    
    public String getGroupname() {
    	return this.groupname;
    }
}
