package org.iplantc.de.admin.apps.client.presenter.grid;

import org.iplantc.de.admin.apps.client.AdminAppStatsGridView;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;

import com.google.common.base.Preconditions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by sriram on 10/21/16.
 */
public class AdminAppsStatsGridPresenterImpl implements AdminAppStatsGridView.Presenter{

    AdminAppStatsGridView view;
    @Inject
    AppServiceFacade appService;

    @Override
    public void go(HasOneWidget container) {

    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        if (event.getAppCategorySelection().isEmpty()) {
            return;
        }
        Preconditions.checkArgument(event.getAppCategorySelection().size() == 1);
        view.mask("Loading...");

        final AppCategory appCategory = event.getAppCategorySelection().iterator().next();
        appService.getApps(appCategory, new AsyncCallback<List<App>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(final List<App> apps) {
                view.clear();
                view.addAll(apps);
                view.unmask();
            }
        });
    }
}
