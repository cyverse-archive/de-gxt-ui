package org.iplantc.de.admin.apps.client;

import org.iplantc.de.admin.apps.client.presenter.callbacks.AppStatsSearchCallback;
import org.iplantc.de.client.models.IsMaskable;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 10/21/16.
 */

@JsType
public interface AdminAppStatsGridView extends IsWidget, IsMaskable {

    @JsIgnore
    void load(Presenter p);



    @JsType
    interface  Appearance {

        String name();

        String total();

        String completed();

        String failed();

        String lastCompleted();

        String lastUsed();

        String rating();

        String loading();

        String startDate();

        String endDate();

        String searchApps();

        String emptyDate();

        String applyFilter();

        String integrator();

        String system();

        String beta();

        Splittable toolbarStyle();

        Splittable gridStyle();

        Splittable buttonStyle();
        
    }

    @JsType
    interface Presenter {
        @JsIgnore
        void go(HasOneWidget container);

        @JsIgnore
        void setViewDebugId(String baseId);

        void searchApps(String searchString,
                        String startDate,
                        String endDate,
                        AppStatsSearchCallback callback);
    }

}
