package org.iplantc.de.theme.base.client.admin.apps;

import org.iplantc.de.admin.apps.client.AdminAppStatsGridView;
import org.iplantc.de.theme.base.client.admin.BelphegorDisplayStrings;

import com.google.gwt.core.client.GWT;

/**
 * Created by sriram on 10/24/16.
 */
public class AdminAppsStatsGridViewDefaultAppearance implements AdminAppStatsGridView.Appearance {

    BelphegorDisplayStrings bds = GWT.create(BelphegorDisplayStrings.class);

    @Override
    public String name() {
        return bds.appName();
    }

    @Override
    public String total() {
        return bds.total();
    }

    @Override
    public String completed() {
        return bds.completed();
    }

    @Override
    public String failed() {
        return bds.failed();
    }

    @Override
    public String lastCompleted() {
        return bds.lastComplted();
    }

    @Override
    public String lastUsed() {
        return bds.lastUsed();
    }

    @Override
    public String rating() {
        return bds.rating();
    }

    @Override
    public String loading() {
        return bds.loading();
    }
}
