package edu.ucar.unidata.cloudcontrol.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods pertaining to User profiles. 
 */

public class UserProfileUtils { 
	
    /**
     * Looks up and returns the possible access levels for a User profile.
	 * This information is used in the admin interface for User management.
     * 
     * @return  The access levels.
     */
    public static Map<String,String> getAccessLevels() {
	    Map<String,String> accessLevels = new HashMap<String,String>();
		accessLevels.put("1", "User");
		accessLevels.put("2", "Administrator");
		return accessLevels;
    }
	
    /**
     * Looks up and returns the possible account status levels for a User profile.
	 * This information is used in the admin interface for User management.
     * 
     * @return  The access levels.
     */
	public static Map<String,String> getAccountStatus() {
		Map<String,String> accountStatus = new HashMap<String,String>();
		accountStatus.put("1", "Active");
	    accountStatus.put("0", "Disable");
		return accountStatus;
	}
	

}

