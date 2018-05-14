package org.iplantc.de.admin.apps.client.presenter.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import org.iplantc.de.admin.apps.client.AdminAppStatsGridView;
import org.iplantc.de.admin.apps.client.presenter.callbacks.AppStatsSearchCallback;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.client.services.callbacks.ErrorCallback;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.shared.AppsCallback;
import com.google.gwt.http.client.Response;

/**
 * Created by sriram on 10/21/16.
 */
public class AdminAppsStatsGridPresenterImpl implements AdminAppStatsGridView.Presenter {

    public AdminAppStatsGridView view;
    @Inject
    AppAdminServiceFacade appService;

    AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);

    @Inject
    AdminAppsStatsGridPresenterImpl(AdminAppStatsGridView view) {
        this.view = view;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
        view.load(this);
    }


    @Override
    public void searchApps(String searchString,
                           String startDate, String endDate,
                           AppStatsSearchCallback callback,
                           ErrorCallback errorCallback) {
        appService.searchApp(searchString, startDate, endDate, new AppsCallback<AppListLoadResult>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR , caught.getMessage());
            }

            @Override
            public void onSuccess(AppListLoadResult result) {
                appsToSplittableList(result);
                if (callback != null) {
                    callback.onSearchCompleted(appsToSplittableList(result));
                }
            }
        });
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + Belphegor.AppStatIds.VIEW);
    }

    private Splittable appsToSplittableList(AppListLoadResult result) {
        Splittable sp = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(result));
        return sp;
    }

}

