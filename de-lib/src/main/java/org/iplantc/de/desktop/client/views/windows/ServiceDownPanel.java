package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.shared.services.DEServiceAsync;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.List;

/**
 * @author aramsey
 */
public class ServiceDownPanel extends CenterLayoutContainer {

    @Inject DEServiceAsync deService;
    private List<SelectEvent.SelectHandler> handlers = Lists.newArrayList();

    public ServiceDownPanel() {
        VerticalLayoutContainer vlc = new VerticalLayoutContainer();
        Label label = new Label();
        label.setText("This service currently appears to be down.");

        TextButton retry = new TextButton();
        retry.setText("Try Again");
        retry.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                for (SelectEvent.SelectHandler handler : handlers) {
                    handler.onSelect(event);
                }
            }
        });

        vlc.add(label);
        vlc.add(retry);

        add(vlc);
    }

    public void addHandler(SelectEvent.SelectHandler handler) {
        if (!handlers.contains(handler)) {
            handlers.add(handler);
        }
    }
}
