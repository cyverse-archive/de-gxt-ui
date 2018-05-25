package org.iplantc.de.admin.apps.client;

import org.iplantc.de.admin.apps.client.presenter.callbacks.AppStatsSearchCallback;
import org.iplantc.de.client.services.callbacks.ErrorCallback;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 10/21/16.
 */

@JsType
public interface AdminAppStatsGridView extends IsWidget {

    @JsIgnore
    void load(Presenter p);

    @JsType
    interface Presenter {
        @JsIgnore
        void go(HasOneWidget container);

        @JsIgnore
        void setViewDebugId(String baseId);

        void searchApps(String searchString,
                        String startDate,
                        String endDate,
                        AppStatsSearchCallback callback,
                        ErrorCallback errorCallback);
    }

}
