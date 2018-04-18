package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.querydsl.Document;
import org.iplantc.de.client.models.querydsl.Fields;
import org.iplantc.de.client.models.querydsl.Metadata;
import org.iplantc.de.client.models.querydsl.QueryAutoBeanFactory;
import org.iplantc.de.client.models.querydsl.SearchResponse;
import org.iplantc.de.client.models.querydsl.Source;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplateList;
import org.iplantc.de.client.models.search.SearchAutoBeanFactory;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.services.SearchServiceFacade;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
import org.iplantc.de.diskResource.client.presenters.grid.proxy.FolderContentsLoadConfig;
import org.iplantc.de.diskResource.client.presenters.grid.proxy.FolderContentsRpcProxyImpl;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SuppressWarnings("nls")
public class SearchServiceFacadeImpl implements SearchServiceFacade {

    public class SubmitQueryCallbackConverter extends AsyncCallbackConverter<String, List<DiskResource>> {
        private final QueryAutoBeanFactory factory;
        private final DiskResourceQueryTemplate queryTemplate;
        private final UserInfo userInfo1;

        public SubmitQueryCallbackConverter(AsyncCallback<List<DiskResource>> callback,
                                            DiskResourceQueryTemplate queryTemplate,
                                            UserInfo userInfo,
                                            QueryAutoBeanFactory queryFactory) {
            super(callback);
            this.queryTemplate = queryTemplate;
            this.userInfo1 = userInfo;
            this.factory = queryFactory;
        }

        @Override
        protected List<DiskResource> convertFrom(String object) {
            if (object == null) {
                return null;
            }
            SearchResponse response = AutoBeanCodex.decode(factory, SearchResponse.class, object).as();
            queryTemplate.setTotal(response.getTotal());

            List<Document> documents = response.getHits();
            List<DiskResource> diskResources = Lists.newArrayList();
            if (documents != null && documents.size() > 0) {
                for (Document document : documents) {
                    diskResources.add(convertDocumentToDiskResource(document));
                }
            }

            return diskResources;
        }

        DiskResource convertDocumentToDiskResource(Document document) {
            Source source = document.getSource();
            if (source == null) {
                return null;
            }
            DiskResource diskResource;
            if (Document.FOLDER_TYPE.equals(document.getType())) {
                diskResource = AutoBeanCodex.decode(factory, Folder.class, AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(document.getSource()))).as();
            } else {
                diskResource = AutoBeanCodex.decode(factory, File.class, AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(document.getSource()))).as();
                ((File)diskResource).setSize(source.getFileSize());
            }

            setDateKeys(source, diskResource);
            setPermission(document, diskResource);
            setInfoType(source, diskResource);
            return diskResource;
        }

        void setPermission(Document document, DiskResource diskResource) {
            Fields fields = document.getFields();
            PermissionValue permission = fields.getPermissions().get(0);
            diskResource.setPermission(permission);
        }

        void setDateKeys(Source source, DiskResource diskResource) {
            diskResource.setDateCreated(source.getDateCreated());
            diskResource.setLastModified(source.getDateModified());
        }

        void setInfoType(Source source, DiskResource diskResource) {
            List<Metadata> metadataList = source.getMetadataList();
            if (metadataList == null || metadataList.isEmpty()) {
                return;
            }
            List<Metadata> fileTypes = metadataList
                                             .stream()
                                             .filter(metadata -> Metadata.FILETYPE_METADATA_KEY.equals(
                                                     metadata.getAttribute()))
                                             .collect(Collectors.toList());
            if (fileTypes != null && fileTypes.size() > 0) {
                diskResource.setInfoType(fileTypes.get(0).getValue());
            }
        }
    }

    class QueryTemplateListCallbackConverter extends AsyncCallbackConverter<String, List<DiskResourceQueryTemplate>> {
        private final SearchAutoBeanFactory factory;

        public QueryTemplateListCallbackConverter(AsyncCallback<List<DiskResourceQueryTemplate>> callback, SearchAutoBeanFactory searchAbFactory) {
            super(callback);
            this.factory = searchAbFactory;
        }

        @Override
        protected List<DiskResourceQueryTemplate> convertFrom(String object) {
            if (Strings.isNullOrEmpty(object)) {
                return Collections.emptyList();
            }
            final List<DiskResourceQueryTemplate> queryTemplateList = getQueryTemplateList(object);
            final List<DiskResourceQueryTemplate> retQueryTemplateList = Lists.newArrayList();
            for (DiskResourceQueryTemplate qt : queryTemplateList) {
                qt.setDirty(false);
                qt.setFiles(Lists.<File> newArrayList());
                qt.setFolders(Lists.<Folder> newArrayList());
                DiskResourceQueryTemplate savedFlagSet = setSavedFlag(qt);
                retQueryTemplateList.add(savedFlagSet);
            }

            return retQueryTemplateList;
        }

        /**
         * Helper method to encapsulate autobean manipulation
         * 
         * @param object
         * @return
         */
        List<DiskResourceQueryTemplate> getQueryTemplateList(String object) {
            // Expecting the string to be JSON list
            Splittable split = StringQuoter.createSplittable();
            Splittable splitList = StringQuoter.split(object);
            if(!splitList.isIndexed()){
                return Collections.emptyList();
            }
            splitList.assign(split, DiskResourceQueryTemplateList.LIST_KEY);
            AutoBean<DiskResourceQueryTemplateList> decode = AutoBeanCodex.decode(factory, DiskResourceQueryTemplateList.class, split);
            return decode.as().getQueryTemplateList();
        }

        /**
         * Helper method to encapsulate autobean manipulation
         * 
         * @param qt
         * @return a query template whose isSaved() method will return true.
         */
        DiskResourceQueryTemplate setSavedFlag(DiskResourceQueryTemplate qt) {
            // Make sure all saved templates are set as saved.
            final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(qt));
            StringQuoter.create(true).assign(encode, "saved");
            return AutoBeanCodex.decode(factory, DiskResourceQueryTemplate.class, encode).as();
        }
    }

    class SavedSearchCallbackConverter extends AsyncCallbackConverter<String, List<DiskResourceQueryTemplate>> {
        private final SearchAutoBeanFactory factory;
        private final List<DiskResourceQueryTemplate> submittedTemplates;

        public SavedSearchCallbackConverter(AsyncCallback<List<DiskResourceQueryTemplate>> callback, List<DiskResourceQueryTemplate> queryTemplates, SearchAutoBeanFactory searchAbFactory) {
            super(callback);
            this.submittedTemplates = queryTemplates;
            this.factory = searchAbFactory;
        }

        @Override
        protected List<DiskResourceQueryTemplate> convertFrom(String object) {
            List<DiskResourceQueryTemplate> savedTemplates = Lists.newArrayList();

            for (DiskResourceQueryTemplate qt : submittedTemplates) {
                DiskResourceQueryTemplate savedFlagSet = setSavedFlag(qt);
                savedTemplates.add(savedFlagSet);
            }

            return savedTemplates;
        }

        /**
         * Helper method to encapsulate autobean manipulation
         * 
         * @param qt
         * @return a query template whose isSaved() method will return true.
         */
        DiskResourceQueryTemplate setSavedFlag(DiskResourceQueryTemplate qt) {
            // Make sure all saved templates are set as saved.
            final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(qt));
            StringQuoter.create(true).assign(encode, "saved");
            return AutoBeanCodex.decode(factory, DiskResourceQueryTemplate.class, encode).as();
        }
    }

    private final DiscEnvApiService deServiceFacade;
    private final DiskResourceAutoBeanFactory drFactory;
    private final SearchAutoBeanFactory searchAbFactory;
    private final QueryAutoBeanFactory queryAutoBeanFactory;
    private final UserInfo userInfo;
    private final DEProperties deProperties;
    private final String SEARCH = "org.iplantc.services.diskResources.search";
    private final String SEARCH_DOCUMENTATION = "org.iplantc.services.diskResources.searchDocumentation";
    final Logger LOG = Logger.getLogger(SearchServiceFacadeImpl.class.getName());

    @Inject
    public SearchServiceFacadeImpl(final DiscEnvApiService deServiceFacade,
                                   final DEProperties deProperties,
                                   final SearchAutoBeanFactory searchAbFactory,
                                   final DiskResourceAutoBeanFactory drFactory,
                                   final QueryAutoBeanFactory queryAutoBeanFactory,
                                   final UserInfo userInfo) {
        this.deServiceFacade = deServiceFacade;
        this.deProperties = deProperties;
        this.searchAbFactory = searchAbFactory;
        this.drFactory = drFactory;
        this.queryAutoBeanFactory = queryAutoBeanFactory;
        this.userInfo = userInfo;
    }

    @Override
    public List<DiskResourceQueryTemplate> createFrozenList(List<DiskResourceQueryTemplate> queryTemplates) {
        List<DiskResourceQueryTemplate> toSave = Lists.newArrayList();
        for (DiskResourceQueryTemplate qt : queryTemplates) {
            DiskResourceQueryTemplate frozenTemplate = freezeQueryTemplate(qt);
            toSave.add(frozenTemplate);
        }
        return Collections.unmodifiableList(toSave);
    }

    @Override
    public void getSavedQueryTemplates(AsyncCallback<List<DiskResourceQueryTemplate>> callback) {
        String address = deProperties.getMuleServiceBaseUrl() + "saved-searches";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deServiceFacade.getServiceData(wrapper, new QueryTemplateListCallbackConverter(callback, searchAbFactory));
    }

    @Override
    public void saveQueryTemplates(List<DiskResourceQueryTemplate> queryTemplates, AsyncCallback<List<DiskResourceQueryTemplate>> callback) {
        String address = deProperties.getMuleServiceBaseUrl() + "saved-searches";

        /*
         * TODO check to see if query templates all have names, and that they are unique.throw illegal
         * argument exception
         */
        String payload = templateListToIndexedSplittablePayload(queryTemplates);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload);
        deServiceFacade.getServiceData(wrapper, new SavedSearchCallbackConverter(callback, queryTemplates, searchAbFactory));
    }

    @Override
    public void deleteQueryTemplates(List<DiskResourceQueryTemplate> queryTemplates,
                                     AsyncCallback<List<DiskResourceQueryTemplate>> callback) {
        String address = deProperties.getMuleServiceBaseUrl() + "saved-searches";

        /*
         * TODO check to see if query templates all have names, and that they are unique.throw illegal
         * argument exception
         */
        String payload = templateListToIndexedSplittablePayload(queryTemplates);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.DELETE, address, payload);
        deServiceFacade.getServiceData(wrapper, new SavedSearchCallbackConverter(callback,
                                                                                 queryTemplates,
                                                                                 searchAbFactory));
    }

    @Override
    public void submitSearchQuery(DiskResourceQueryTemplate template,
                                  FolderContentsLoadConfig loadConfig,
                                  FolderContentsRpcProxyImpl.QueryResultsCallback queryResultsCallback) {
        DataSearchQueryBuilderv2 builder = new DataSearchQueryBuilderv2(template);
        String address = SEARCH;

        Splittable query = builder.getFullQuery();

        int limit = loadConfig.getLimit();
        int offset = loadConfig.getOffset();

        StringQuoter.create(limit).assign(query, "size");
        StringQuoter.create(offset).assign(query, "from");

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, query.getPayload());
        deServiceFacade.getServiceData(wrapper, new SubmitQueryCallbackConverter(queryResultsCallback, template, userInfo, queryAutoBeanFactory));
    }

    String convertSortField(String sortField) {
        if ("id".equalsIgnoreCase(sortField)) {
            return "entity.id";
        }

        if ("size".equalsIgnoreCase(sortField)) {
            return "entity.fileSize";
        }

        if ("dateCreated".equalsIgnoreCase(sortField)) {
            return "entity.dateCreated";
        }

        if ("lastModified".equalsIgnoreCase(sortField)) {
            return "entity.dateModified";
        }

        if ("name".equalsIgnoreCase(sortField)) {
            return "entity.label";
        }

        if ("path".equalsIgnoreCase(sortField)) {
            return "entity.path";
        }

        return sortField;
    }

    DiskResourceQueryTemplate freezeQueryTemplate(DiskResourceQueryTemplate qt) {
        // Create copy of template
        Splittable qtSplittable = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(qt));
        AutoBean<DiskResourceQueryTemplate> decode = AutoBeanCodex.decode(searchAbFactory, DiskResourceQueryTemplate.class, qtSplittable);

        // Freeze the autobean
        decode.setFrozen(true);
        return decode.as();
    }

    String templateListToIndexedSplittablePayload(List<DiskResourceQueryTemplate> queryTemplates) {
        Splittable indexedSplittable = StringQuoter.createIndexed();
        int index = 0;
        for (DiskResourceQueryTemplate qt : queryTemplates) {
            final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(qt));
            StringQuoter.createIndexed().assign(encode, "files");
            StringQuoter.createIndexed().assign(encode, "folders");
            encode.assign(indexedSplittable, index++);
        }
        return indexedSplittable.getPayload();
    }
}
