package org.iplantc.de.admin.apps.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

/**
 * Created by sriram on 10/21/16.
 */
public class AdminAppStatsGridViewImpl implements AdminAppStatsGridView{

    HTMLPanel panel;

    @Inject
    public AdminAppStatsGridViewImpl() {
        panel = new HTMLPanel("<div></div>");
   }

    @Override
    public void load(Presenter presenter) {
        Scheduler.get().scheduleFinally(() -> {
            ReactAppsAdmin.AppStatsProps props = new ReactAppsAdmin.AppStatsProps();
            props.presenter = presenter;
            CyVerseReactComponents.render(ReactAppsAdmin.AppStats, props, panel.getElement());
        });
    }


    @Override
    public Widget asWidget() {
        return panel;
    }

}

