package org.iplantc.de.client.models.querydsl;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.sharing.OldUserPermission;

import java.util.Date;
import java.util.List;

/**
 * The autobean representation of the "_source" JSON object returned in an Elasticsearch query response
 */
public interface Source extends HasId {

    List<OldUserPermission> getUserPermissions();

    String getPath();

    long getFileSize();

    String getCreator();

    String getFileType();

    Date getDateModified();

    String getLabel();

    Date getDateCreated();

    List<Metadata> getMetadataList();
}
