package org.iplantc.de.apps.client;

import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;

import java.util.List;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * Created by jstroot on 3/2/15.
 * @author jstroot
 */
@JsType
public interface AppDetailsView extends IsWidget, AppUpdatedEvent.AppUpdatedEventHandler {

    @JsType
    interface AppDetailsAppearance {

        @JsType
        interface AppDetailsStyle extends CssResource {

            String label();

            String value();

            String hyperlink();

            String detailsCard();

            String detailsTable();

            String detailsRow();

            String tabPanel();
        }

        String descriptionLabel();

        AppDetailsStyle css();

        String detailsLabel();

        @JsIgnore
        SafeHtml getAppDocError(Throwable caught);

        @JsIgnore
        SafeHtml getCategoriesHtml(List<List<String>> appGroupHierarchies);

        @JsIgnore
        SafeHtml highlightText(String value, String searchRegexPattern);

        String publishedOnLabel();

        String integratorNameLabel();

        String integratorEmailLabel();

        String helpLabel();

        String ratingLabel();

        String categoriesLabel();

        String informationTabLabel();

        @JsIgnore
        SafeHtml saveAppDocError(Throwable caught);

        String toolInformationTabLabel();

        String toolNameLabel();

        String toolVersionLabel();

        String toolAttributionLabel();

        String userManual();

        String url();

        String appUrl();

        String copyAppUrl();

        @JsIgnore
        void setTreeIcons(TreeStyle style);

        String completedRun();

        String completedDate();

        String imageLabel();

    }

    @JsType
    interface Presenter {

        void go(App app,
                String searchRegexPattern,
                TreeStore<OntologyHierarchy> hierarchyTreeStore,
                TreeStore<AppCategory> categoryTreeStore);

        void onAppFavoriteSelected(Splittable app,
                                   ReactSuccessCallback callback,
                                   ReactErrorCallback errorCallback);

        void onAppRatingSelected(Splittable app,
                                 int score,
                                 ReactSuccessCallback callback,
                                 ReactErrorCallback errorCallback);

        void onAppRatingDeSelected(Splittable app,
                                   ReactSuccessCallback callback,
                                   ReactErrorCallback errorCallback);

        void onClose();

        void getAppDoc(Splittable appSplittable,
                              ReactSuccessCallback callback,
                              ReactErrorCallback errorCallback);

        void onSaveMarkdownSelected(String appId,
                                    String systemId,
                                    String doc,
                                    ReactSuccessCallback callback,
                                    ReactErrorCallback errorCallback);

    }


    void load(Presenter presenter);

    void onClose();

    void onDetailsCategoryClicked(String modelKey);
}
