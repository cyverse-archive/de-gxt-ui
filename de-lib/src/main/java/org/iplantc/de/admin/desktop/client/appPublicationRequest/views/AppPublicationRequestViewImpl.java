package org.iplantc.de.admin.desktop.client.appPublicationRequest.views;

import org.iplantc.de.admin.desktop.client.appPublicationRequest.AppPublicationRequestView;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class AppPublicationRequestViewImpl implements AppPublicationRequestView {

    HTMLPanel panel;

    AppPublicationRequestViewImpl() {
        panel = new HTMLPanel("<div></div>");
    }

    @Override
    public void load(Presenter presenter) {
        
    }

    @Override
    public Widget asWidget() {
        return panel;
    }
}
