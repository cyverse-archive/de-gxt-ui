package org.iplantc.de.admin.apps.client.presenter.grid;

import org.iplantc.de.admin.apps.client.AdminAppStatsGridView;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.shared.AppsCallback;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

/**
 * Created by sriram on 10/21/16.
 */
public class AdminAppsStatsGridPresenterImpl implements AdminAppStatsGridView.Presenter {

    public AdminAppStatsGridView view;
    @Inject
    AppAdminServiceFacade appService;
    @Inject
    AdminAppStatsGridView.Appearance appearance;

    @Inject
    AdminAppsStatsGridPresenterImpl(AdminAppStatsGridView view) {
        this.view = view;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
        load();
    }

    void load() {
        view.mask(appearance.loading());
        appService.searchApp("", new AppsCallback<AppListLoadResult>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
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

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + Belphegor.AppStatIds.VIEW);
    }

}
