package org.iplantc.de.admin.apps.client.presenter.grid;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.apps.client.ReactAppsAdmin;
import org.iplantc.de.admin.apps.client.events.selection.RestoreAppSelected;
import org.iplantc.de.admin.apps.client.views.editor.AppEditor;
import org.iplantc.de.admin.desktop.client.ontologies.service.OntologyServiceFacade;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.DeleteAppsSelected;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.client.models.apps.AppList;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
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
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jstroot
 */
public class AdminAppsGridPresenterImpl implements AdminAppsGridView.Presenter {

    private HandlerManager handlerManager;

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }


    @Override
    public HandlerRegistration addAppSelectionChangedEventHandler(AppSelectionChangedEvent.AppSelectionChangedEventHandler handler) {
        return ensureHandlers().addHandler(AppSelectionChangedEvent.TYPE, handler);

    }

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
    @Inject
    EventBus eventBus;

    @Inject
    AdminAppsGridView view;
    OntologyUtil ontologyUtil = OntologyUtil.getInstance();

    List<App> selectedApps = new ArrayList<>();

    Splittable apps = null;

    @Inject
    AdminAppsGridPresenterImpl() {

    }

    @Override
    public void go(String baseId) {
        view.load(this, baseId);
    }

    @Override
    public AdminAppsGridView getView() {
        return view;
    }

    @Override
    public List<App> getSelectedApps() {
        return selectedApps;
    }

    @Override
    public void setApps(Splittable apps) {
        if (apps != null) {
            this.apps = apps;
            view.setApps(apps.get("apps"), false);
        } else {
            this.apps = null;
            view.setApps(null, false);
        }
    }

     @Override
     public void onAppSelectionChanged(Splittable splittableSelectedApps) {
        selectedApps.clear();
         for (int i = 0; i < splittableSelectedApps.size(); i++) {
             App app = convertSplittableToApp(splittableSelectedApps.get(i));
             selectedApps.add(app);
         }
         AppSelectionChangedEvent event = new AppSelectionChangedEvent(selectedApps);
         fireEvent(event);
     }

    public void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        if (event.getAppCategorySelection().isEmpty()) {
            return;
        }
        Preconditions.checkArgument(event.getAppCategorySelection().size() == 1);
        view.setLoading(true);

        final AppCategory appCategory = event.getAppCategorySelection().iterator().next();
        appService.getApps(appCategory, null, new AppsCallback<Splittable>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(caught);
                view.setLoading(false);
            }

            @Override
            public void onSuccess(final Splittable apps) {
                setApps(apps);
            }
        });
    }


    @Override
    public void onAppInfoSelected(Splittable selectedApp,
                                  String appId,
                                  String systemId,
                                  boolean isPublic) {
        appEditor.setBaseProps(getBaseProps());
        view.setLoading(true);
        //get doc only for public apps!
        if (isPublic) {
            adminAppService.getAppDetails(appId, systemId,
                                          new AsyncCallback<Splittable>() {
                @Override
                public void onFailure(Throwable caught) {
                    view.setLoading(false);
                    showAppEditor(selectedApp, null);
                    ErrorHandler.postReact(caught);
                }

                @Override
                public void onSuccess(final Splittable details) {
                    view.setLoading(false);
                    showAppEditor(selectedApp, details);
                }
            });
        } else {
            view.setLoading(false);
            showAppEditor(selectedApp, null);
        }
    }

    protected void showAppEditor(Splittable app,
                                 Splittable details) {
        appEditor.edit(app, details);
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        Splittable apps;
        AppListLoadResult results = event.getResults();
        String searchText = event.getSearchText();
        int total = event.getResults() != null ? event.getResults().getTotal() : 0;
        String heading = appearance.searchAppResultsHeader(searchText, total);
        view.setSearchResultsHeader(heading);
        if (results != null) {
            apps = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(results));
            setApps(apps);
        } else {
            setApps(null);
        }
    }

    @Override
    public void onDeleteAppsSelected(final DeleteAppsSelected event) {
        Preconditions.checkArgument(event.getAppsToBeDeleted().size() == 1);
        final App selectedApp = event.getAppsToBeDeleted().iterator().next();

        view.setLoading(true);
        adminAppService.deleteApp(selectedApp,
                                  new AsyncCallback<Void>() {

                                      @Override
                                      public void onFailure(Throwable caught) {
                                          view.setLoading(false);
                                          ErrorHandler.post(caught);
                                      }

                                      @Override
                                      public void onSuccess(Void result) {
                                          view.setLoading(false);
                                          deleteApp(selectedApp);
                                      }
                                  });
    }

    @Override
    public void onRestoreAppSelected(final RestoreAppSelected event) {
        Preconditions.checkArgument(event.getApps().size() == 1);
        final App selectedApp = event.getApps().iterator().next();
        Preconditions.checkArgument(selectedApp.isDeleted());

        view.setLoading(true);
        adminAppService.restoreApp(selectedApp, new AsyncCallback<App>() {

            @Override
            public void onFailure(Throwable caught) {
                view.setLoading(false);
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
                view.setLoading(false);
                List<String> categoryNames = Lists.newArrayList();
                for(AppCategory category : result.getGroups()){
                    categoryNames.add(category.getName());
                }

                String joinedCatNames = Joiner.on(",").join(categoryNames);
                announcer.schedule(new SuccessAnnouncementConfig(appearance.restoreAppSuccessMsgTitle() + "\n"
                                                                     + appearance.restoreAppSuccessMsg(result.getName(),
                                                                                                       joinedCatNames)));
                }

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
                    updateApp(app);
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

    @Override
    public void deleteApp(App selectedApp) {
        if (apps != null) {
            List<App> newAppList = removeAppFromListing(selectedApp.getId());
            if (newAppList != null) {
                AppList appListBean = factory.appList().as();
                appListBean.setApps(newAppList);
                setApps(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(appListBean)));
            }
        }
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

    List<App> SplittableToAppList(Splittable apps) {
        return AutoBeanCodex.decode(factory, AppList.class, apps).as().getApps();
    }

    List<App> removeAppFromListing(String id) {
        if (apps != null) {
            List<App> appList = SplittableToAppList(apps);
            if (appList != null) {
                List<App> newAppList = appList.stream()
                                              .filter(app -> !app.getId().equals(id))
                                              .collect(Collectors.toList());
                return newAppList;
            }
            return null;
        }
        return null;
    }

    void updateApp(App updatedApp) {
        if (apps != null) {
            List<App> newAppList = removeAppFromListing(updatedApp.getId());
            if (newAppList != null) {
                AppList appListBean = factory.appList().as();
                newAppList.add(updatedApp);
                appListBean.setApps(newAppList);
                setApps(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(appListBean)));
            }
        }
    }
}
