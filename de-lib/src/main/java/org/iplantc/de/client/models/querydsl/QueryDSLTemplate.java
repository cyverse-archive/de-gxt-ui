package org.iplantc.de.client.models.querydsl;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.search.DateInterval;

import java.util.List;

/**
 * An autobean that can contain all the user inputs that can be clauses in an Elasticsearch query
 */
public interface QueryDSLTemplate extends Folder {

    String getNameHas();
    void setNameHas(String label);

    String getNameHasNot();
    void setNameHasNot(String label);

    String getOwnedBy();
    void setOwnedBy(String owner);

    DateInterval getCreatedWithin();
    void setCreatedWithin(DateInterval createdWithin);

    DateInterval getModifiedWithin();
    void setModifiedWithin(DateInterval modifiedWithin);

    String getMetadataAttributeHas();
    void setMetadataAttributeHas(String attribute);

    String getMetadataValueHas();
    void setMetadataValueHas(String value);

    String getSharedWith();
    void setSharedWith(String username);

    int getFileSizeGreater();
    void setFileSizeGreater(int size);

    String getFileSizeGreaterUnit();
    void setFileSizeGreaterUnit(String unit);

    int getFileSizeLessThan();
    void setFileSizeLessThan(int size);

    String getFileSizeLessThanUnit();
    void setFileSizeLessThanUnit(String unit);

    String getTaggedWith();
    void setTaggedWith(String tag);

    List<String> getTags();
    void setTags(List<String> tags);

    Boolean isIncludeTrash();
    void setIncludeTrash(Boolean includeTrash);

    Boolean isLabelExact();
    void setLabelExact(Boolean exact);

    String getPathPrefix();
    void setPathPrefix(String pathPrefix);

    Boolean isPermissionRecurse();
    void setPermissionRecurse(Boolean permissionRecurse);

    List<String> getPermissionUsers();
    void setPermissionUsers(List<String> permissionUsers);

}
