package org.iplantc.de.client.models.querydsl;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * The autobean factory for all beans used to communicate with the search service
 */
public interface QueryAutoBeanFactory extends AutoBeanFactory {

    AutoBean<SearchResponse> getSearchResponse();

    AutoBean<DiskResource> getDiskResource();

    AutoBean<File> getFile();

    AutoBean<Folder> getFolder();

    AutoBean<DocumentList> getDocumentList();

    AutoBean<Document> getDocument();

    AutoBean<Metadata> getMetadata();

    AutoBean<MetadataList> getMetadataList();

    AutoBean<Source> getSource();

    AutoBean<DocumentUserPermissionList> getDocumentUserPermissionList();

    AutoBean<QueryDSLTemplate> getQueryDSLTemplate();
}
