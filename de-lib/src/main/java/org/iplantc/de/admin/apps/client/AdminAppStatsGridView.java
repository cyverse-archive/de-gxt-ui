package org.iplantc.de.admin.apps.client;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.apps.App;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 10/21/16.
 */

@JsType
public interface AdminAppStatsGridView extends IsWidget, IsMaskable {

    @JsIgnore
    void addAll(List<App> apps);

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
    }

    interface Presenter {
        void go(HasOneWidget container);

        void setViewDebugId(String baseId);
    }

}
