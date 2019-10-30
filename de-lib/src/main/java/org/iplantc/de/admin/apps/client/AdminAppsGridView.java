package org.iplantc.de.admin.apps.client;

import org.iplantc.de.admin.apps.client.events.selection.RestoreAppSelected;
import org.iplantc.de.admin.desktop.client.communities.events.CommunitySelectionChanged;
import org.iplantc.de.admin.desktop.client.ontologies.events.HierarchySelectedEvent;
import org.iplantc.de.admin.desktop.client.ontologies.events.PreviewHierarchySelectedEvent;
import org.iplantc.de.admin.desktop.client.ontologies.events.SelectOntologyVersionEvent;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.DeleteAppsSelected;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * Created by jstroot on 3/9/15.
 * @author jstroot
 */
public interface AdminAppsGridView extends IsWidget,
                                           AppCategorySelectionChangedEvent.AppCategorySelectionChangedEventHandler,
                                           AppSearchResultLoadEvent.AppSearchResultLoadEventHandler,
                                           BeforeAppSearchEvent.BeforeAppSearchEventHandler,
                                           HierarchySelectedEvent.HierarchySelectedEventHandler,
                                           PreviewHierarchySelectedEvent.PreviewHierarchySelectedEventHandler,
                                           SelectOntologyVersionEvent.SelectOntologyVersionEventHandler,
                                           CommunitySelectionChanged.CommunitySelectionChangedHandler {

    interface Appearance extends AppsListView.AppsListAppearance {

    }

    @JsType
    interface Presenter extends AppCategorySelectionChangedEvent.AppCategorySelectionChangedEventHandler,
                                DeleteAppsSelected.DeleteAppsSelectedHandler,
            //  StoreRemoveEvent.HasStoreRemoveHandler<App>,
                                RestoreAppSelected.RestoreAppSelectedHandler,
                                AppSearchResultLoadEvent.AppSearchResultLoadEventHandler,
                                AppSelectionChangedEvent.HasAppSelectionChangedEventHandlers,
                                AppInfoSelectedEvent.HasAppInfoSelectedEventHandlers {

        interface Appearance extends AppsListView.AppsListAppearance {

            String confirmDeleteAppTitle();

            String confirmDeleteAppWarning();

            String deleteAppLoadingMask();

            String deleteApplicationError(String name);

            String restoreAppFailureMsg(String name);

            String restoreAppFailureMsgTitle();

            String restoreAppLoadingMask();


            String restoreAppSuccessMsg(String name, String s);

            String restoreAppSuccessMsgTitle();

            String saveAppLoadingMask();

            String updateApplicationError();

            String updateDocumentationSuccess();

            String betaTagAddedSuccess();

            String betaTagRemovedSuccess();
        }

        @JsIgnore
        AdminAppsGridView getView();

        /*@JsIgnore
        App getAppFromElement(Element eventTarget);*/

       /* @JsIgnore
        List<App> getSelectedApps();*/

        @SuppressWarnings("unusable-by-js")
        void onSaveAppSelected(Splittable appSpl,
                               ReactSuccessCallback callback,
                               ReactErrorCallback errorCallback);

        void addAppDocumentation(String systemId,
                                 String appId,
                                 String appDoc,
                                 ReactSuccessCallback callback,
                                 ReactErrorCallback errorCallback);

        void updateAppDocumentation(String systemId,
                                    String appId,
                                    String appDoc,
                                    ReactSuccessCallback callback,
                                    ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void updateBetaStatus(Splittable appSpl,
                              ReactSuccessCallback callback,
                              ReactErrorCallback errorCallback);

        void closeAppDetailsDlg();
    }

    void clearAndAdd(Splittable apps);

 /*   App getAppFromElement(Element as);

    List<App> getSelectedApps();

    void deselectAll();

    void removeApp(App selectedApp);*/

    void load(Presenter presenter);

    void setLoading(boolean loading);

    void setApps(Splittable apps,
                 boolean loading);
}
