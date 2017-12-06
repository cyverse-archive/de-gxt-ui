package org.iplantc.de.client.models.querydsl;

import org.iplantc.de.client.models.sharing.PermissionValue;

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

    PermissionValue getPermissionValue();
    void setPermissionValue(PermissionValue permissionValue);

    Boolean isPermissionRecurse();
    void setPermissionRecurse(Boolean permissionRecurse);

}
