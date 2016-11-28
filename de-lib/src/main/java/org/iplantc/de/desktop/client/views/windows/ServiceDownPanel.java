package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.shared.services.DEServiceAsync;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author aramsey
 */
public class ServiceDownPanel extends CenterLayoutContainer {

    @Inject DEServiceAsync deService;

    public ServiceDownPanel(SelectEvent.SelectHandler handler) {
        VerticalLayoutContainer vlc = new VerticalLayoutContainer();
        Label label = new Label();
        label.setText("This service currently appears to be down.");

        TextButton retry = new TextButton();
        retry.setText("Try Again");
        retry.addSelectHandler(handler);

        vlc.add(label);
        vlc.add(retry);

        add(vlc);
    }
}
