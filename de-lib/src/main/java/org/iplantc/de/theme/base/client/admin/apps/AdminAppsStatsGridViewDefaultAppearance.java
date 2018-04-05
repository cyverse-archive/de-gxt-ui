package org.iplantc.de.theme.base.client.admin.apps;

import org.iplantc.de.admin.apps.client.AdminAppStatsGridView;
import org.iplantc.de.theme.base.client.admin.BelphegorDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

/**
 * Created by sriram on 10/24/16.
 */
public class AdminAppsStatsGridViewDefaultAppearance implements AdminAppStatsGridView.Appearance {

    BelphegorDisplayStrings bds;

    public AdminAppsStatsGridViewDefaultAppearance() {
        bds = GWT.create(BelphegorDisplayStrings.class);
    }

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

    @Override
    public String startDate() {
        return bds.startDate();
    }

    @Override
    public String endDate() {
        return bds.endDate();
    }

    @Override
    public String searchApps() {
        return bds.searchApps();
    }

    @Override
    public String emptyDate() {
        return bds.emptyDate();
    }

    @Override
    public String applyFilter() {
        return bds.applyFilter();
    }

    @Override
    public String integrator() {
        return bds.integrator();
    }

    @Override
    public String system() {
        return bds.system();
    }

    @Override
    public String beta() {
        return bds.beta();
    }

    @Override
    public Splittable toolbarStyle() {
        return StringQuoter.split("{\"height\": \"30px\",\"backgroundColor\": \"white\"}");
    }

    @Override
    public Splittable gridStyle() {
        return StringQuoter.split("{\"height\": \"800px\"}");
    }

    @Override
    public Splittable buttonStyle() {
        return  StringQuoter.split("{\"height\": \"30px\"}");
    }
}
