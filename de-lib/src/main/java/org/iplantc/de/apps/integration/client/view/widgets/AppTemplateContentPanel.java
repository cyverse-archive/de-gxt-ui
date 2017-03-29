package org.iplantc.de.apps.integration.client.view.widgets;

import org.iplantc.de.apps.integration.client.view.propertyEditors.PropertyEditorAppearance;
import org.iplantc.de.apps.widgets.client.events.AppTemplateSelectedEvent;
import org.iplantc.de.apps.widgets.client.events.AppTemplateSelectedEvent.AppTemplateSelectedEventHandler;
import org.iplantc.de.apps.widgets.client.events.AppTemplateSelectedEvent.HasAppTemplateSelectedEventHandlers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;
import com.sencha.gxt.widget.core.client.ContentPanel;

public final class AppTemplateContentPanel extends ContentPanel implements HasAppTemplateSelectedEventHandlers {

    public interface ArgumentGroupContentPanelAppearance extends ContentPanelAppearance {
        HeaderDefaultAppearance getHeaderAppearance();
    }

    private final PropertyEditorAppearance appearance;

    @Inject
    public AppTemplateContentPanel(ArgumentGroupContentPanelAppearance panelAppearance,
                                   PropertyEditorAppearance appearance) {
        super(panelAppearance);
        setCollapsible(true);
        setAnimCollapse(false);
        setTitleCollapse(true);
        getHeader().addStyleName(appearance.appHeaderSelect());
        this.appearance = appearance;
        sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
    }

    @Override
    public HandlerRegistration addAppTemplateSelectedEventHandler(AppTemplateSelectedEventHandler handler) {
        return addHandler(handler, AppTemplateSelectedEvent.TYPE);
    }

    @Override
    protected void onClick(Event ce) {
        XElement element = XElement.as(header.getElement());
        if (element.isOrHasChild(ce.getEventTarget().<Element> cast())) {
            fireEvent(new AppTemplateSelectedEvent());
            getHeader().addStyleName(appearance.appHeaderSelect());
        }
        super.onClick(ce);
    }
}
