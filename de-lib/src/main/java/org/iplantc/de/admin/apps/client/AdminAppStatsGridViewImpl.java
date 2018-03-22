package org.iplantc.de.admin.apps.client;

import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.Composite;

import java.util.List;

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
    public void addAll(List<App> apps) {
        Scheduler.get().scheduleFinally(() -> {
            ReactAppStats.AppStatsProps props = new ReactAppStats.AppStatsProps();
            props.appearance = appearance;
            Splittable[] stats = new Splittable[apps.size()];
            for (int i = 0; i < apps.size(); i++) {
                stats[i] = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(apps.get(i)));
            }
            props.appStats = stats;
            CyVerseReactComponents.render(ReactAppStats.appStats, props, panel.getElement());
        });

    }

    @Override
    protected void onEnsureDebugId(final String baseID) {
        super.onEnsureDebugId(baseID);

    }
}
