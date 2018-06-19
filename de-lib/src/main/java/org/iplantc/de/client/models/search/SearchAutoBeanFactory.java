package org.iplantc.de.client.models.search;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.search.FileSizeRange.FileSizeUnit;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface SearchAutoBeanFactory extends AutoBeanFactory {

    public static final SearchAutoBeanFactory INSTANCE = GWT.create(SearchAutoBeanFactory.class);

    AutoBean<DiskResourceQueryTemplate> dataSearchFilter();

    AutoBean<DateInterval> dateInterval();

    AutoBean<FileSizeRange> fileSizeRange();

    AutoBean<FileSizeUnit> fileSizeUnit();

    AutoBean<DiskResourceQueryTemplateList> drQtList();

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

    AutoBean<DiskResourceQueryTemplate> getQueryTemplate();

}
