package org.iplantc.de.diskResource.client.presenters.metadata;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.DiskResourceMetadataList;
import org.iplantc.de.client.models.diskResources.MetadataTemplate;
import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.client.models.ontologies.AstroThesaurusResponse;
import org.iplantc.de.client.models.ontologies.AstroThesaurusResult;
import org.iplantc.de.client.models.ontologies.OntologyAutoBeanFactory;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceQueryParams;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceResponse;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.OntologyLookupServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.events.selection.MetadataInfoBtnSelected;
import org.iplantc.de.diskResource.client.events.selection.SaveMetadataSelected;
import org.iplantc.de.diskResource.client.presenters.callbacks.DiskResourceMetadataUpdateCallback;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.AstroThesaurusLoadConfig;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.AstroThesaurusProxy;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.OntologyLookupServiceLoadConfig;
import org.iplantc.de.diskResource.client.views.metadata.MetadataTemplateView;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.MetadataTemplateDescDlg;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.SelectMetadataTemplateDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;

import java.util.List;

/**
 * @author jstroot sriram psarando
 */
public class MetadataPresenterImpl implements MetadataView.Presenter,
                                              MetadataInfoBtnSelected.MetadataInfoBtnSelectedHandler {

    private DiskResource resource;
    private final MetadataView view;
    private final DiskResourceServiceFacade drService;
    List<MetadataTemplateInfo> templates;

    private DiskResourceMetadataList metadata;

    @Inject DiskResourceAutoBeanFactory autoBeanFactory;
    @Inject OntologyAutoBeanFactory ontologyAutoBeanFactory;

    @Inject MetadataView.Presenter.Appearance appearance;

    @Inject AsyncProviderWrapper<SelectMetadataTemplateDialog> selectMetaTemplateDlgProvider;
    @Inject AsyncProviderWrapper<MetadataTemplateDescDlg> metadataTemplateDescDlgProvider;
    @Inject MetadataTemplateView templateViewDialog;
    @Inject OntologyLookupServiceFacade svcFacade;
    @Inject EventBus eventBus;

    @Inject
    public MetadataPresenterImpl(final MetadataView view,
                                 final DiskResourceServiceFacade drService) {

        this.view = view;
        this.drService = drService;
    }

    void loadMetadata() {
        drService.getDiskResourceMetaData(resource, new DiskResourceMetadataListAsyncCallback());
    }

    @Override
    public void go(final DiskResource selected) {
        this.resource = selected;
        view.init(this, DiskResourceUtil.getInstance().isWritable(selected), selected);
        view.mask();

        drService.getMetadataTemplateListing(new AsyncCallback<List<MetadataTemplateInfo>>() {
            @Override
            public void onFailure(Throwable arg0) {
                view.unmask();
                ErrorHandler.post(appearance.templateListingError(), arg0);
            }

            @Override
            public void onSuccess(final List<MetadataTemplateInfo> result) {
                templates = result;
                loadMetadata();
            }
        });
    }

    @SuppressWarnings("unusable-by-js")
    @Override
    public void setDiskResourceMetadata(Splittable metadata, ReactSuccessCallback resolve, ReactErrorCallback reject) {
        final DiskResourceMetadataList mdList =
                AutoBeanCodex.decode(autoBeanFactory, DiskResourceMetadataList.class, metadata).as();

        drService.setDiskResourceMetaData(resource, mdList, new DiskResourceMetadataUpdateCallback(data -> {
            if (resolve != null) {
                resolve.onSuccess(data);
            }
            view.closeMetadataDialog();
        }, (httpStatusCode, errorMessage) -> {
            if (reject != null) {
                reject.onError(httpStatusCode, errorMessage);
            }
            view.closeMetadataDialog();
        }));
    }

    @SuppressWarnings("unusable-by-js")
    @Override
    public void onSelectTemplateBtnSelected(Splittable formValues) {
        metadata = decodeMetadata(formValues);
        view.mask();

        selectMetaTemplateDlgProvider.get(new AsyncCallback<SelectMetadataTemplateDialog>() {
            @Override
            public void onFailure(Throwable throwable) {
                view.unmask();
            }

            @Override
            public void onSuccess(SelectMetadataTemplateDialog dialog) {
                view.unmask();
                view.closeMetadataDialog();

                dialog.setModal(true);
                dialog.addDialogHideHandler(hideEvent -> {
                    PredefinedButton hideButton = hideEvent.getHideButton();
                    if (PredefinedButton.OK.equals(hideButton)) {
                        MetadataTemplateInfo selectedTemplate = dialog.getSelectedTemplate();
                        if (selectedTemplate != null) {
                            onTemplateSelected(selectedTemplate.getId());
                        }
                    } else {
                        view.showMetadataDialog();
                    }
                });
                dialog.show(templates, true);
                dialog.addMetadataInfoBtnSelectedHandler(MetadataPresenterImpl.this);
            }
        });
    }

    DiskResourceMetadataList decodeMetadata(Splittable formValues) {
        return AutoBeanCodex.decode(autoBeanFactory, DiskResourceMetadataList.class, formValues).as();
    }

    @Override
    public void closeMetadataDialog() {
        view.closeMetadataDialog();
    }

    @Override
    public void onMetadataInfoBtnSelected(MetadataInfoBtnSelected event) {
        metadataTemplateDescDlgProvider.get(new AsyncCallback<MetadataTemplateDescDlg>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(MetadataTemplateDescDlg result) {
                MetadataTemplateInfo info = event.getTemplateInfo();
                result.show(info);
            }
        });
    }

    public void onTemplateSelected(String templateId) {
        drService.getMetadataTemplate(templateId, new MetadataTemplateAsyncCallback());
    }

    @SuppressWarnings("unusable-by-js")
    @Override
    public void updateMetadataFromTemplateView(Splittable metadata,
                                               ReactSuccessCallback resolve,
                                               ReactErrorCallback reject) {
        resolve.onSuccess(metadata);
        view.updateMetadataFromTemplateView(metadata);
        closeMetadataTemplateDialog();
    }

    @Override
    public void closeMetadataTemplateDialog() {
        templateViewDialog.closeDialog();
        view.showMetadataDialog();
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void searchOLSTerms(String inputValue,
                               Splittable loaderSettings,
                               ReactSuccessCallback callback) {
        if (Strings.isNullOrEmpty(inputValue)) {
            callback.onSuccess(null);
            return;
        }

        OntologyLookupServiceQueryParams olsQueryParams =
                AutoBeanCodex.decode(ontologyAutoBeanFactory, OntologyLookupServiceQueryParams.class, loaderSettings)
                             .as();

        OntologyLookupServiceLoadConfig loadConfig = new OntologyLookupServiceLoadConfig(olsQueryParams);
        loadConfig.setQuery(inputValue);

        svcFacade.searchOntologyLookupService(loadConfig, new AsyncCallback<OntologyLookupServiceResponse>() {
            @Override
            public void onSuccess(OntologyLookupServiceResponse result) {
                callback.onSuccess(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(result.getResults())));
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void searchAstroThesaurusTerms(String inputValue, ReactSuccessCallback callback) {
        if (Strings.isNullOrEmpty(inputValue)) {
            callback.onSuccess(null);
            return;
        }

        AstroThesaurusLoadConfig uatLoadConfig = new AstroThesaurusLoadConfig();
        uatLoadConfig.setQuery(inputValue);

        svcFacade.searchUnifiedAstronomyThesaurus(uatLoadConfig, new AsyncCallback<AstroThesaurusResponse>() {
            @Override
            public void onSuccess(AstroThesaurusResponse response) {
                final AstroThesaurusResult result = response.getResult();
                result.setItems(AstroThesaurusProxy.normalizeAstroThesaurusResults(result.getItems()));

                callback.onSuccess(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(result)));
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void onSaveMetadataToFileBtnSelected() {
        eventBus.fireEvent(new SaveMetadataSelected(resource));
    }

    @Override
    public void setViewDebugId(String debugId) {
        view.asWidget().ensureDebugId(debugId);
    }

    private class DiskResourceMetadataListAsyncCallback
            implements AsyncCallback<DiskResourceMetadataList> {
        @Override
        public void onSuccess(final DiskResourceMetadataList result) {
            metadata = result;
            view.loadMetadata(result);
            view.unmask();
        }


        @Override
        public void onFailure(Throwable caught) {
            view.unmask();
            ErrorHandler.post(appearance.loadMetadataError(), caught);
        }
    }

    private class MetadataTemplateAsyncCallback implements AsyncCallback<MetadataTemplate> {

        @Override
        public void onFailure(Throwable arg0) {
            ErrorHandler.post(appearance.templateinfoError(), arg0);
        }

        @Override
        public void onSuccess(MetadataTemplate result) {
            templateViewDialog.openDialog(MetadataPresenterImpl.this, result, metadata, isWritable());
        }

        private boolean isWritable() {
            return DiskResourceUtil.getInstance().isWritable(resource);
        }
    }
}
