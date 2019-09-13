package org.iplantc.de.admin.apps.client.presenter.grid;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.apps.client.ReactAppsAdmin;
import org.iplantc.de.admin.apps.client.events.selection.RestoreAppSelected;
import org.iplantc.de.admin.apps.client.gin.factory.AdminAppsGridViewFactory;
import org.iplantc.de.admin.apps.client.views.editor.AppEditor;
import org.iplantc.de.admin.desktop.client.ontologies.service.OntologyServiceFacade;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.DeleteAppsSelected;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.avu.AvuList;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.client.util.OntologyUtil;
import org.iplantc.de.commons.client.CommonUiConstants;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AppsCallback;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;

import java.util.List;

/**
 * @author jstroot
 */
public class AdminAppsGridPresenterImpl implements AdminAppsGridView.Presenter,
                                                   AppInfoSelectedEvent.AppInfoSelectedEventHandler {


    private static class DocSaveCallback implements AsyncCallback<AppDoc> {
        private final IplantAnnouncer announcer;
        private final Appearance appearance;
        private ReactSuccessCallback callback;
        private ReactErrorCallback errorCallback;

        public DocSaveCallback(final IplantAnnouncer announcer,
                               final Appearance appearance,
                               ReactSuccessCallback callback,
                               ReactErrorCallback errorCallback) {
            this.announcer = announcer;
            this.appearance = appearance;
            this.callback = callback;
            this.errorCallback = errorCallback;
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.postReact(caught);
            errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
        }

        @Override
        public void onSuccess(AppDoc result) {
            announcer.schedule(new SuccessAnnouncementConfig(appearance.updateDocumentationSuccess()));
            callback.onSuccess(null);
        }
    }

    class RemoveBetaAvuCallback implements AsyncCallback<List<Avu>> {
        private ReactSuccessCallback callback;
        private ReactErrorCallback errorCallback;

        public RemoveBetaAvuCallback(ReactSuccessCallback callback,
                                     ReactErrorCallback errorCallback) {
            this.callback = callback;
            this.errorCallback = errorCallback;
        }

        @Override
        public void onFailure(Throwable caught) {
            errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            ErrorHandler.postReact(caught);
        }

        @Override
        public void onSuccess(List<Avu> result) {
            announcer.schedule(new SuccessAnnouncementConfig(appearance.betaTagRemovedSuccess()));
            callback.onSuccess(null);
        }
    }

    class AddBetaAvuCallback implements AsyncCallback<List<Avu>> {
        private ReactSuccessCallback callback;
        private ReactErrorCallback errorCallback;

        public AddBetaAvuCallback(ReactSuccessCallback callback,
                                  ReactErrorCallback errorCallback) {
            this.callback = callback;
            this.errorCallback = errorCallback;
        }

        @Override
        public void onFailure(Throwable caught) {
            errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            ErrorHandler.postReact(caught);
        }

        @Override
        public void onSuccess(List<Avu> result) {
            announcer.schedule(new SuccessAnnouncementConfig(appearance.betaTagAddedSuccess()));
            callback.onSuccess(null);
        }
    }

    @Inject OntologyServiceFacade ontologyServiceFacade;
    @Inject AppAdminServiceFacade adminAppService;
    @Inject AppServiceFacade appService;
    @Inject AdminAppsGridView.Presenter.Appearance appearance;
    @Inject AppAutoBeanFactory factory;
    @Inject JsonUtil jsonUtil;
    @Inject IplantAnnouncer announcer;
    @Inject AppEditor appEditor;
    @Inject CommonUiConstants uiConstants;

    private final ListStore<App> listStore;
    private final AdminAppsGridView view;
    OntologyUtil ontologyUtil = OntologyUtil.getInstance();

    @Inject
    AdminAppsGridPresenterImpl(final AdminAppsGridViewFactory viewFactory,
                               final ListStore<App> listStore) {
        this.listStore = listStore;
        view = viewFactory.create(listStore);

        view.addAppInfoSelectedEventHandler(this);

    }

    @Override
    public HandlerRegistration addStoreRemoveHandler(StoreRemoveEvent.StoreRemoveHandler<App> handler) {
        return listStore.addStoreRemoveHandler(handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AdminAppsGridView getView() {
        return view;
    }

    @Override
    public App getAppFromElement(Element eventTarget) {
        return view.getAppFromElement(Element.as(eventTarget));
    }

    @Override
    public List<App> getSelectedApps() {
        return view.getSelectedApps();
    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        if (event.getAppCategorySelection().isEmpty()) {
            return;
        }
        Preconditions.checkArgument(event.getAppCategorySelection().size() == 1);
        view.mask(appearance.getAppsLoadingMask());

        final AppCategory appCategory = event.getAppCategorySelection().iterator().next();
        appService.getApps(appCategory, null, new AppsCallback<List<App>>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(final List<App> apps) {
                listStore.clear();
                listStore.addAll(apps);
                view.unmask();
            }
        });
    }


    @Override
    public void onAppInfoSelected(final AppInfoSelectedEvent event) {
        appEditor.setBaseProps(getBaseProps());
        view.mask(appearance.saveAppLoadingMask());
        App selectedApp = event.getApp();
        //get doc only for public apps!
        if (selectedApp.isPublic()) {
            adminAppService.getAppDetails(selectedApp, new AsyncCallback<App>() {
                @Override
                public void onFailure(Throwable caught) {
                    view.unmask();
                    showAppEditor(selectedApp);
                    ErrorHandler.postReact(caught);
                }

                @Override
                public void onSuccess(final App details) {
                    // Get result
                    view.unmask();
                    selectedApp.setDocumentation(details.getDocumentation());
                    selectedApp.setExtra(details.getExtra());
                    showAppEditor(selectedApp);
                }
            });
        } else {
            view.unmask();
            showAppEditor(selectedApp);
        }
    }

    protected void showAppEditor(App app) {
        appEditor.edit(convertAppToSplittable(app));
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        listStore.clear();
        listStore.addAll(event.getResults().getData());
    }

    @Override
    public void onDeleteAppsSelected(final DeleteAppsSelected event) {
        Preconditions.checkArgument(event.getAppsToBeDeleted().size() == 1);
        final App selectedApp = event.getAppsToBeDeleted().iterator().next();

        view.mask(appearance.deleteAppLoadingMask());
        adminAppService.deleteApp(selectedApp,
                                  new AsyncCallback<Void>() {

                                      @Override
                                      public void onFailure(Throwable caught) {
                                          view.unmask();
                                          ErrorHandler.post(caught);
                                      }

                                      @Override
                                      public void onSuccess(Void result) {
                                          view.unmask();
                                          //  eventBus.fireEvent(new CatalogCategoryRefreshEvent());
                                          view.getGrid().getSelectionModel().deselectAll();
                                          listStore.remove(selectedApp);
                                      }
                                  });
    }

    @Override
    public void onRestoreAppSelected(final RestoreAppSelected event) {
        Preconditions.checkArgument(event.getApps().size() == 1);
        final App selectedApp = event.getApps().iterator().next();
        Preconditions.checkArgument(selectedApp.isDeleted());

        view.mask(appearance.restoreAppLoadingMask());
        adminAppService.restoreApp(selectedApp, new AsyncCallback<App>() {

            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
                JSONObject obj = JSONParser.parseStrict(caught.getMessage()).isObject();
                String reason = jsonUtil.trim(obj.get("reason").toString());
                if (reason.contains("orphaned")) {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.restoreAppFailureMsg(selectedApp.getName())));
                } else {
                    announcer.schedule(new ErrorAnnouncementConfig(reason));
                }
            }

            @Override
            public void onSuccess(App result) {
                view.unmask();
                List<String> categoryNames = Lists.newArrayList();
                for(AppCategory category : result.getGroups()){
                    categoryNames.add(category.getName());
                }

                String joinedCatNames = Joiner.on(",").join(categoryNames);
                announcer.schedule(new SuccessAnnouncementConfig(appearance.restoreAppSuccessMsgTitle() + "\n"
                                                                     + appearance.restoreAppSuccessMsg(result.getName(),
                                                                                                       joinedCatNames)));
                }
                // eventBus.fireEvent(new CatalogCategoryRefreshEvent());
            });
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void onSaveAppSelected(Splittable appSpl,
                                  ReactSuccessCallback callback,
                                  ReactErrorCallback errorCallback) {

        final App app = convertSplittableToApp(appSpl);

        if (app.getName() != null) {
            adminAppService.updateApp(app, new AsyncCallback<App>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.postReact(caught);
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                }

                @Override
                public void onSuccess(App result) {
                    listStore.update(app);
                    callback.onSuccess(null);
                }
            });
        }
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void addAppDocumentation(String systemId,
                                    String appId,
                                    String appDoc,
                                    ReactSuccessCallback callback,
                                    ReactErrorCallback errorCallback) {
        HasQualifiedId hasQualifiedId = factory.hasQualifiedId().as();
        hasQualifiedId.setSystemId(systemId);
        hasQualifiedId.setId(appId);

        adminAppService.saveAppDoc(hasQualifiedId,
                                   appDoc,
                                   new DocSaveCallback(announcer,
                                                       appearance,
                                                       callback,
                                                       errorCallback));
    }

    @Override
    public void updateAppDocumentation(String systemId,
                                       String appId,
                                       String appDoc,
                                       ReactSuccessCallback callback,
                                       ReactErrorCallback errorCallback) {
        HasQualifiedId hasQualifiedId = factory.hasQualifiedId().as();
        hasQualifiedId.setSystemId(systemId);
        hasQualifiedId.setId(appId);

        adminAppService.updateAppDoc(hasQualifiedId,
                                     appDoc,
                                     new DocSaveCallback(announcer,
                                                         appearance,
                                                         callback,
                                                         errorCallback));
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void updateBetaStatus(Splittable appSpl,
                                 ReactSuccessCallback callback,
                                 ReactErrorCallback errorCallback) {
        App app = convertSplittableToApp(appSpl);

        if (app.isBeta()) {
            AvuList betaAvus = ontologyUtil.getBetaAvuList();
            ontologyServiceFacade.addAVUsToApp(app, betaAvus, new AddBetaAvuCallback(callback,
                                                                                     errorCallback));
        } else {
            ontologyServiceFacade.getAppAVUs(app, new AsyncCallback<List<Avu>>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                }

                @Override
                public void onSuccess(List<Avu> result) {
                    AvuList avuList = ontologyUtil.removeBetaAvu(result);
                    ontologyServiceFacade.setAppAVUs(app, avuList, new RemoveBetaAvuCallback(callback,
                                                                                             errorCallback));
                }
            });
        }
    }

    @Override
    public void closeAppDetailsDlg() {
        appEditor.close();
    }

    ReactAppsAdmin.AdminAppDetailsProps getBaseProps() {
        ReactAppsAdmin.AdminAppDetailsProps props = new ReactAppsAdmin.AdminAppDetailsProps();
        props.presenter = this;
        props.restrictedChars = uiConstants.appNameRestrictedChars();
        props.restrictedStartingChars = uiConstants.appNameRestrictedStartingChars();
        props.createDocWikiUrl = uiConstants.publishDocumentationUrl();
        props.documentationTemplateUrl = GWT.getHostPageBaseURL() + uiConstants.documentationTemplateUrl();

        props.parentId = Belphegor.AppIds.APP_EDITOR;

        return props;
    }

    Splittable convertAppToSplittable(App app) {
        return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(app));
    }

    App convertSplittableToApp(Splittable appSpl) {
        return AutoBeanCodex.decode(factory, App.class, appSpl.getPayload()).as();
    }
}
