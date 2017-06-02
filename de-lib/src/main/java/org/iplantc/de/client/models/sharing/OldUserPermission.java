package org.iplantc.de.client.models.sharing;

/**
 * The autobean representation of the user and the permissions being applied
 * to a disk resource object - eventually this should migrate over to UserPermission
 * like the other resources
 * @author aramsey
 */
public interface OldUserPermission {

    String getUser();
    void setUser(String user);

    /**
     * @return permission for the user
     */
    String getPermission();

    void setPermission(String permission);

}
