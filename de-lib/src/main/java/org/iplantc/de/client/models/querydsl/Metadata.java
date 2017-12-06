package org.iplantc.de.client.models.querydsl;

/**
 * The autobean representation of the metadata returned in an Elasticsearch query response
 */
public interface Metadata {

    String FILETYPE_METADATA_KEY = "ipc-filetype";

    String getAttribute();

    String getValue();

    String getUnit();
}
