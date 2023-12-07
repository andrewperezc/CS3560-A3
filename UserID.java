package assignment2;

import java.util.Objects;

public class UserID {
	private String username;
	
	public UserID(String username) {
		this.username = username;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }
        UserID userID = (UserID) o;
        
        return Objects.equals(username, userID.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
    
    public String getUsername() {
    	return this.username;
    }

}
