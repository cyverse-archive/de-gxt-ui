package org.iplantc.de.apps.client.views.details.dialogs;

import org.iplantc.de.apps.client.AppDetailsView;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.DetailsCategoryClicked;
import org.iplantc.de.apps.client.events.selection.DetailsHierarchyClicked;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.TreeStore;

/**
 * @author jstroot
 */
public class AppDetailsDialog extends IPlantDialog {

    @Inject AsyncProviderWrapper<AppDetailsView.Presenter> presenterProvider;

    @Inject
    AppDetailsDialog() {
        setModal(true);
        setResizable(false);
        setPixelSize(450, 300);
        getButtonBar().clear();
    }

    public void show(final App app,
                     final String searchRegexPattern,
                     final TreeStore<OntologyHierarchy> hierarchyTreeStore,
                     final TreeStore<AppCategory> categoryTreeStore,
                     final EventHandler eventHandler) {

        setHeadingText(app.getName());
        presenterProvider.get(new AsyncCallback<AppDetailsView.Presenter>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(final AppDetailsView.Presenter result) {
                result.go(AppDetailsDialog.this, app, searchRegexPattern, hierarchyTreeStore, categoryTreeStore);
                if (eventHandler != null) {
                    result.addAppFavoriteSelectedEventHandlers((AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler)eventHandler);
                    result.addAppRatingSelectedHandler((AppRatingSelected.AppRatingSelectedHandler)eventHandler);
                    result.addAppRatingDeselectedHandler((AppRatingDeselected.AppRatingDeselectedHandler)eventHandler);
                    result.addDetailsHierarchyClickedHandler((DetailsHierarchyClicked.DetailsHierarchyClickedHandler)eventHandler);
                    result.addDetailsCategoryClickedHandler((DetailsCategoryClicked.DetailsCategoryClickedHandler)eventHandler);
                }
            }
        });

        super.show();
    }

    @Override
    public void show() {
        throw new UnsupportedOperationException("This method is not supported. Use show(App) instead.");
    }

}
