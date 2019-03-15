package org.iplantc.de.apps.client;

import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.selection.SaveMarkdownSelected;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;

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
public interface AppDetailsView extends IsWidget,
                                        AppUpdatedEvent.AppUpdatedEventHandler,
                                        SaveMarkdownSelected.HasSaveMarkdownSelectedHandlers {

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
    interface Presenter extends SaveMarkdownSelected.SaveMarkdownSelectedHandler {

        void go(App app,
                String searchRegexPattern,
                TreeStore<OntologyHierarchy> hierarchyTreeStore,
                TreeStore<AppCategory> categoryTreeStore);

        void onAppFavoriteSelected(Splittable app);

        void onAppRatingSelected(Splittable app);

        void onAppRatingDeSelected(Splittable app);

        void onClose();

        void onAppDetailsDocSelected();

    }


    void load(Presenter presenter);

    void onClose();

    /**
     * Displays the documentation window
     */
    @JsIgnore
    void showDoc(AppDoc appDoc);

    void onDetailsCategoryClicked(String modelKey);
}
