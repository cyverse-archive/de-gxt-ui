package org.iplantc.de.desktop.client.views.windows;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Label;

import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.List;

/**
 * @author aramsey
 */
public class ServiceDownPanel extends CenterLayoutContainer {

    public interface ServiceDownPanelAppearance {
        String serviceDownText();

        String retryBtnText();
    }

    private List<SelectEvent.SelectHandler> handlers = Lists.newArrayList();

    public ServiceDownPanel() {
        this(GWT.<ServiceDownPanelAppearance>create(ServiceDownPanelAppearance.class));
    }

    public ServiceDownPanel(ServiceDownPanelAppearance appearance) {

        VerticalLayoutContainer vlc = new VerticalLayoutContainer();
        Label label = new Label();
        label.setText(appearance.serviceDownText());

        TextButton retry = new TextButton();
        retry.setText(appearance.retryBtnText());
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

        //KLUDGE to get the panel to center properly
        addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                ((Window)getParent()).forceLayout();
            }
        });
    }

    public void addHandler(SelectEvent.SelectHandler handler) {
        if (!handlers.contains(handler)) {
            handlers.add(handler);
        }
    }
}
