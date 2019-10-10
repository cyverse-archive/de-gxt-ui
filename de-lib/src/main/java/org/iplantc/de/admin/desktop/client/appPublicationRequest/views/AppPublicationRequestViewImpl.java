package org.iplantc.de.admin.desktop.client.appPublicationRequest.views;

import org.iplantc.de.admin.apps.client.ReactAppsAdmin;
import org.iplantc.de.admin.desktop.client.appPublicationRequest.AppPublicationRequestView;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

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
        CyVerseReactComponents.render(ReactAppsAdmin.AppPublicationRequests, props, panel.getElement());
    }

    @Override
    public Widget asWidget() {
        return panel;
    }
}
