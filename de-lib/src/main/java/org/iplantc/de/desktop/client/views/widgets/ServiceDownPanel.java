package org.iplantc.de.desktop.client.views.widgets;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.List;

/**
 * @author aramsey
 */
public class ServiceDownPanel extends Composite {

    interface ServiceDownPanelUiBinder extends UiBinder<VBoxLayoutContainer, ServiceDownPanel> {
    }

    @UiField TextButton retry;
    @UiField HTML serviceDownMsg;
    @UiField ServiceDownPanelAppearance appearance;

    public interface ServiceDownPanelAppearance {
        String serviceDownText();

        String retryBtnText();

        String loadingMask();
    }

    private List<SelectEvent.SelectHandler> handlers = Lists.newArrayList();

    public ServiceDownPanel() {
        ServiceDownPanelUiBinder uiBinder = GWT.create(ServiceDownPanelUiBinder.class);
        initWidget(uiBinder.createAndBindUi(this));
        serviceDownMsg.setHTML(appearance.serviceDownText());
        retry.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                mask(appearance.loadingMask());
                for (SelectEvent.SelectHandler handler : handlers) {
                    handler.onSelect(event);
                }
            }
        });

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
