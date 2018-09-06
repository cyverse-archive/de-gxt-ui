package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.PATCH;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.analysis.AnalysesAutoBeanFactory;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisParameter;
import org.iplantc.de.client.models.analysis.AnalysisParametersList;
import org.iplantc.de.client.models.analysis.AnalysisStepsInfo;
import org.iplantc.de.client.models.analysis.SimpleValue;
import org.iplantc.de.client.models.analysis.sharing.AnalysisSharingRequestList;
import org.iplantc.de.client.models.analysis.sharing.AnalysisUnsharingRequestList;
import org.iplantc.de.client.models.apps.integration.ArgumentType;
import org.iplantc.de.client.models.apps.integration.SelectionItem;
import org.iplantc.de.client.services.AnalysisServiceFacade;
import org.iplantc.de.client.services.converters.DECallbackConverter;
import org.iplantc.de.client.util.AppTemplateUtils;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.services.BaseServiceCallWrapper;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides access to remote services for analyses management operations.
 * @author jstroot
 */
public class AnalysisServiceFacadeImpl implements AnalysisServiceFacade {

    private class StringListAsyncCallbackConverter extends DECallbackConverter<String, List<AnalysisParameter>> {
        private final AnalysesAutoBeanFactory factory;

        public StringListAsyncCallbackConverter(DECallback<List<AnalysisParameter>> callback, AnalysesAutoBeanFactory factory) {
            super(callback);
            this.factory = factory;
        }

        @Override
        protected List<AnalysisParameter> convertFrom(String object) {
            AnalysisParametersList as = AutoBeanCodex.decode(factory, AnalysisParametersList.class, object).as();
            return parse(as.getParameterList());
        }

        List<AnalysisParameter> parse(final List<AnalysisParameter> paramList) {

            List<AnalysisParameter> parsedList = new ArrayList<>();
            for (AnalysisParameter ap : paramList) {
                if (appTemplateUtils.isTextType(ap.getType()) || ap.getType().equals(ArgumentType.Flag)) {
                    parsedList.addAll(parseStringValue(ap));
                } else if (isInputType(ap.getType()) || isReferenceGenomeType(ap.getType().toString())) {
                    if (!isReferenceGenomeType(ap.getType().toString())) {
                        parsedList.addAll(parseStringValue(ap));
                    } else {
                        parsedList.addAll(parseSelectionValue(ap));
                    }
                } else if (appTemplateUtils.isSelectionArgumentType(ap.getType())) {
                    parsedList.addAll(parseSelectionValue(ap));
                } else if (ap.getType().equals(ArgumentType.FileOutput)) {
                    parsedList.addAll(parseStringValue(ap));
                }
            }

            return parsedList;

        }

        Set<String> REFERENCE_GENOME_TYPES
                = Sets.newHashSet("referenceannotation", "referencesequence", "referencegenome");

        boolean isReferenceGenomeType(final String typeName) {
            return REFERENCE_GENOME_TYPES.contains(typeName.toLowerCase());
        }

        final Set<ArgumentType> INPUT_TYPES = Sets.immutableEnumSet(ArgumentType.Input, ArgumentType.FileInput, ArgumentType.FolderInput,
                                                                    ArgumentType.MultiFileSelector,
                                                                    ArgumentType.FileFolderInput);

        boolean isInputType(ArgumentType type) {
            return INPUT_TYPES.contains(type);
        }

        List<AnalysisParameter> parseSelectionValue(final AnalysisParameter ap) {
            Splittable s = ap.getValue();
            Splittable val = s.get("value");
            if ((val != null) && (Strings.isNullOrEmpty(val.getPayload()) || !val.isKeyed())) {
                return Collections.emptyList();
            }
            AutoBean<SelectionItem> ab = AutoBeanCodex.decode(factory, SelectionItem.class, val);
            ap.setDisplayValue(ab.as().getDisplay());
            return Lists.newArrayList(ap);
        }

        List<AnalysisParameter> parseStringValue(final AnalysisParameter ap) {
            List<AnalysisParameter> parsedList = new ArrayList<>();
            Splittable s = ap.getValue();
            AutoBean<SimpleValue> ab = AutoBeanCodex.decode(factory, SimpleValue.class, s);
            ap.setDisplayValue(ab.as().getValue());
            parsedList.add(ap);
            return parsedList;
        }
    }
    
    private class StringAnalaysisStepInfoConverter extends
                                                  DECallbackConverter<String, AnalysisStepsInfo> {

        private final AnalysesAutoBeanFactory factory;

        public StringAnalaysisStepInfoConverter(DECallback<AnalysisStepsInfo> callback,
                                                AnalysesAutoBeanFactory factory) {
            super(callback);
            this.factory = factory;
        }

        @Override
        protected AnalysisStepsInfo convertFrom(String object) {
            AnalysisStepsInfo as = AutoBeanCodex.decode(factory, AnalysisStepsInfo.class, object).as();
            return as;
        }
        
    }

    private final AnalysesAutoBeanFactory factory;
    private final AppTemplateUtils appTemplateUtils;
    private final DiskResourceUtil diskResourceUtil;
    private final DiscEnvApiService deServiceFacade;
    public static final String ANALYSES = "org.iplantc.services.analyses";


    @Inject
    public AnalysisServiceFacadeImpl(final DiscEnvApiService deServiceFacade,
                                     final AnalysesAutoBeanFactory factory,
                                     final AppTemplateUtils appTemplateUtils,
                                     final DiskResourceUtil diskResourceUtil) {
        this.deServiceFacade = deServiceFacade;
        this.factory = factory;
        this.appTemplateUtils = appTemplateUtils;
        this.diskResourceUtil = diskResourceUtil;
    }
    @Override
    public void getAnalyses(int limit,
                            int offSet,
                            Map<String, String> filters,
                            String sortField,
                            String sortDir,
                            DECallback<String> callback) {
        StringBuilder address = new StringBuilder(ANALYSES);
        address.append("?limit=" + limit); //$NON-NLS-1$
        address.append("&offset=" + offSet); //$NON-NLS-1$

        if (!Strings.isNullOrEmpty(sortField)) {
            address.append("&sort-field="); //$NON-NLS-1$
            address.append(sortField);
        }

        if (!Strings.isNullOrEmpty(sortDir)) {
            address.append("&sort-dir="); //$NON-NLS-1$
            address.append(sortDir);
        }

        if (filters != null && filters.size() > 0) {
            int filterIndex = 0;
            JSONArray jsonFilters = new JSONArray();
            for (String field : filters.keySet()) {
                String value = filters.get(field);
                if (!Strings.isNullOrEmpty(field) && value != null) {
                    JSONObject jsonFilter = new JSONObject();

                    jsonFilter.put("field", new JSONString(field)); //$NON-NLS-1$
                    jsonFilter.put("value", new JSONString(value)); //$NON-NLS-1$

                    jsonFilters.set(filterIndex++, jsonFilter);
                }
            }
            if (jsonFilters.size() > 0) {
                address.append("&filter="); //$NON-NLS-1$
                address.append(URL.encodeQueryString(jsonFilters.toString()));
            }
        }
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address.toString());
        deServiceFacade.getServiceData(wrapper, callback);
    }


    @Override
    public void deleteAnalyses(List<Analysis> analysesToBeDeleted, DECallback<String> callback) {
        String address = ANALYSES + "/shredder"; //$NON-NLS-1$ //$NON-NLS-2$
        final Splittable stringIdListSplittable = diskResourceUtil.createStringIdListSplittable(analysesToBeDeleted);
        final Splittable payload = StringQuoter.createSplittable();
        stringIdListSplittable.assign(payload, "analyses");
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload.getPayload());

        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void renameAnalysis(Analysis analysis, String newName, DECallback<Void> callback) {
        String address = ANALYSES + "/" + analysis.getId();
        Splittable body = StringQuoter.createSplittable();
        StringQuoter.create(newName).assign(body, "name");

        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address, body.getPayload());
        deServiceFacade.getServiceData(wrapper, new DECallbackConverter<String, Void>(callback) {
            @Override
            protected Void convertFrom(String object) {
                return null;
            }
        });
    }

    // TODO: Change status to use AnalysisExecutionStatus instead of a string
    @Override
    public void stopAnalysis(Analysis analysis, DECallback<String> callback, String status) {
        String address = ANALYSES + "/" + analysis.getId() + "/stop?job_status=" + status;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, "{}");

        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void getAnalysisParams(Analysis analysis, DECallback<List<AnalysisParameter>> callback) {
        String address = ANALYSES + "/" + analysis.getId() + "/parameters";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);

        deServiceFacade.getServiceData(wrapper, new StringListAsyncCallbackConverter(callback, factory));
    }

    @Override
    public void updateAnalysisComments(final Analysis analysis, final String newComment, DECallback<Void> callback) {
        String address = ANALYSES + "/" + analysis.getId();
        Splittable body = StringQuoter.createSplittable();
        StringQuoter.create(newComment).assign(body, "description");

        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address, body.getPayload());
        deServiceFacade.getServiceData(wrapper, new DECallbackConverter<String, Void>(callback) {
            @Override
            protected Void convertFrom(String object) {
                return null;
            }
        });
    }

    @Override
    public void getAnalysisSteps(Analysis analysis, DECallback<AnalysisStepsInfo> callback) {
        String address = ANALYSES + "/" + analysis.getId() + "/steps";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deServiceFacade.getServiceData(wrapper, new StringAnalaysisStepInfoConverter(callback, factory));

    }

    @Override
    public void shareAnalyses(AnalysisSharingRequestList request, DECallback<String> callback) {
        final String payload = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request)).getPayload();
        GWT.log("analyis sharing request:" + payload);
        String address = ANALYSES + "/" + "sharing";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload);
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void unshareAnalyses(AnalysisUnsharingRequestList request, DECallback<String> callback) {
        final String payload = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request)).getPayload();
        GWT.log("analysis un-sharing request:" + payload);
        String address = ANALYSES + "/" + "unsharing";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload);
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void getPermissions(List<Analysis> analyses, DECallback<String> callback) {
        Splittable anaObj = StringQuoter.createSplittable();
        Splittable idArr = StringQuoter.createIndexed();

        for(Analysis a : analyses) {
            Splittable item = StringQuoter.create(a.getId());
            item.assign(idArr, idArr.size());
        }

        idArr.assign(anaObj, "analyses");
        String address = ANALYSES + "/" + "permission-lister";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(BaseServiceCallWrapper.Type.POST, address, anaObj.getPayload());
        deServiceFacade.getServiceData(wrapper, callback);
    }

}

