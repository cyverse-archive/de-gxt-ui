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

    private final Appearance appearance;
    HTMLPanel panel;

    @Inject
    public AdminAppStatsGridViewImpl(AdminAppStatsGridView.Appearance appearance) {
        panel = new HTMLPanel("<div></div>");
        this.appearance = appearance;
   }

    @Override
    public void load(Presenter presenter) {
        Scheduler.get().scheduleFinally(() -> {
            ReactAppStats.AppStatsProps props = new ReactAppStats.AppStatsProps();
            props.appearance = appearance;
            props.presenter = presenter;
            CyVerseReactComponents.render(ReactAppStats.appStats, props, panel.getElement());
        });
    }


    @Override
    public Widget asWidget() {
        return panel;
    }

}

