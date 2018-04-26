package org.iplantc.de.admin.apps.client;

import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Composite;

/**
 * Created by sriram on 10/21/16.
 */
public class AdminAppStatsGridViewImpl extends Composite implements AdminAppStatsGridView{

    private final Appearance appearance;
    HTMLPanel panel;

    @Inject
    public AdminAppStatsGridViewImpl(AdminAppStatsGridView.Appearance appearance) {
        panel = new HTMLPanel("<div></div>");
        this.appearance = appearance;
        initWidget(panel);
   }

    @Override
    protected void onEnsureDebugId(final String baseID) {
        super.onEnsureDebugId(baseID);
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
}
