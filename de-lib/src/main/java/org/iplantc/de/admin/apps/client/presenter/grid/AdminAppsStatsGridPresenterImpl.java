package org.iplantc.de.admin.apps.client.presenter.grid;

import org.iplantc.de.admin.apps.client.AdminAppStatsGridView;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.resources.client.messages.I18N;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

/**
 * Created by sriram on 10/21/16.
 */
public class AdminAppsStatsGridPresenterImpl implements AdminAppStatsGridView.Presenter {

    AdminAppStatsGridView view;

    @Inject
    AppAdminServiceFacade appService;

    @Inject
    public AdminAppsStatsGridPresenterImpl(AdminAppStatsGridView view) {
        this.view = view;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
        load();
    }

    void load() {
        view.mask(I18N.DISPLAY.loadingMask());
        appService.searchApp(null, new AsyncCallback<AppListLoadResult>() {
            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(AppListLoadResult result) {
                view.clear();
                view.addAll(result.getData());
                view.unmask();
            }
        });
    }

}
