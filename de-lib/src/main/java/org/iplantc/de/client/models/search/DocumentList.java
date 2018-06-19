package org.iplantc.de.client.models.search;

import org.iplantc.de.client.models.search.Document;

import java.util.List;

/**
 * The autobean representation of a list of Document types
 */
public interface DocumentList {

    List<Document> getDocuments();
    void setDocuments(List<Document> documents);
}
