package org.iplantc.de.client.models.querydsl;

import java.util.List;

/**
 * An autobean that can contain all the user inputs that can be clauses in an Elasticsearch query
 */
public interface QueryDSLTemplate {

    String getLabel();
    void setLabel(String label);

    Boolean isLabelExact();
    void setLabelExact(Boolean exact);

    String getPathPrefix();
    void setPathPrefix(String pathPrefix);

    String getOwner();
    void setOwner(String owner);

    String getPermission();
    void setPermission(String permission);

    Boolean isPermissionRecurse();
    void setPermissionRecurse(Boolean permissionRecurse);

    List<String> getPermissionUsers();
    void setPermissionUsers(List<String> permissionUsers);

}
