package org.iplantc.de.admin.apps.client;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.apps.App;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * Created by sriram on 10/21/16.
 */
public interface AdminAppStatsGridView extends IsWidget, IsMaskable {


    void clear();

    void addAll(List<App> apps);

    interface  Appearance extends AppsListView.AppsListAppearance {

    }

    interface Presenter {
        void go(HasOneWidget container);
        void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event);
    }
}
