package org.iplantc.de.admin.desktop.client.apps.views;

import org.iplantc.de.admin.apps.client.ReactAppsAdmin;
import org.iplantc.de.admin.apps.client.AppPublicationRequestView;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 *
 * @author sriram 
 *
 */
public class AppPublicationRequestViewImpl implements AppPublicationRequestView {

    HTMLPanel panel;
    private ReactAppsAdmin.AppPublicationRequestProps props;

    AppPublicationRequestViewImpl() {
        panel = new HTMLPanel("<div></div>");
    }

    @Override
    public void load(Presenter presenter) {
        props = new ReactAppsAdmin.AppPublicationRequestProps();
        props.presenter = presenter;
        props.parentId = panel.getElement().getId();
        props.requests = null;
        props.loading = true;
        render();
    }

    @Override
    public void setRequests(Splittable requests) {
        props.requests = requests;
        render();
    }

    @Override
    public void setLoading(boolean loading) {
        props.loading = loading;
        render();
    }

    private void render() {
        CyVerseReactComponents.render(ReactAppsAdmin.AppPublicationRequests, props, panel.getElement());
    }

    @Override
    public Widget asWidget() {
        return panel;
    }
}
