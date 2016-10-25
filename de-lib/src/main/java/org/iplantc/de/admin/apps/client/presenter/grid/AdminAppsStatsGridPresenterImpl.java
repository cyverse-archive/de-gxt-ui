package org.iplantc.de.admin.apps.client.presenter.grid;

import org.iplantc.de.admin.apps.client.AdminAppStatsGridView;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.client.models.apps.AppList;
import org.iplantc.de.commons.client.ErrorHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

/**
 * Created by sriram on 10/21/16.
 */
public class AdminAppsStatsGridPresenterImpl implements AdminAppStatsGridView.Presenter{

    AdminAppStatsGridView view;

    @Inject
    AppAdminServiceFacade appService;

    @Inject
    public AdminAppsStatsGridPresenterImpl(AdminAppStatsGridView view) {
        this.view = view;

    }

    @Override
    public void go(HasOneWidget container) {
       view.mask("Loading...");
       container.setWidget(view);
       appService.searchApp("gen", null, null, new AsyncCallback<AppList>() {
           @Override
           public void onFailure(Throwable caught) {
               ErrorHandler.post("unable to get app list", caught);
           }

           @Override
           public void onSuccess(AppList result) {
              view.clear();
              view.addAll(result.getApps());
              view.unmask();
           }
       });
    }

}
