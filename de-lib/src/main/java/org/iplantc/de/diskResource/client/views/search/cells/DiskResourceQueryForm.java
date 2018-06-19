package org.iplantc.de.diskResource.client.views.search.cells;

import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.diskResource.client.SearchView;
import org.iplantc.de.diskResource.client.views.search.ReactSearchForm;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent;

/**
 * A form the user can fill out to perform advanced searches in the Data window which utilize the search service
 */
public class DiskResourceQueryForm implements SearchView {

    private VerticalLayoutContainer con;
    private final BaseEventPreview eventPreview;
    private boolean showing = false;
    HandlerManager handlerManager;

    @Inject
    public DiskResourceQueryForm() {
        this.con = new VerticalLayoutContainer();
        con.getElement().getStyle().setBackgroundColor("#fff");
        con.setWidth("500px");

        eventPreview = new BaseEventPreview() {

            @Override
            protected boolean onPreview(Event.NativePreviewEvent pe) {
                DiskResourceQueryForm.this.onPreviewEvent(pe);
                return super.onPreview(pe);
            }

            @Override
            protected void onPreviewKeyPress(Event.NativePreviewEvent pe) {
                super.onPreviewKeyPress(pe);
                onEscape(pe);
            }

        };
        eventPreview.getIgnoreList().add(getElement());
        eventPreview.setAutoHide(false);
    }

    @Override
    public void show(Element parent, Style.AnchorAlignment anchorAlignment, ReactSearchForm.SearchFormProps props) {
        getElement().makePositionable(true);

        renderSearchForm(props);

        RootPanel.get().add(this);
        getElement().updateZIndex(0);

        showing = true;

        getElement().alignTo(parent, anchorAlignment, 0, 0);

        getElement().show();
        eventPreview.add();

        fireEvent(new ShowEvent());
    }

    @Override
    public XElement getElement() {
        return con.getElement();
    }

    @Override
    public void renderSearchForm(ReactSearchForm.SearchFormProps props) {
        CyVerseReactComponents.render(ReactSearchForm.SearchForm,
                                      props,
                                      DivElement.as(con.getElement()));
    }

    @Override
    public void hide() {
        if (showing) {
            RootPanel.get().remove(this);
            eventPreview.remove();
            showing = false;
            fireEvent(new HideEvent());
        }
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }

    void onEscape(Event.NativePreviewEvent pe) {
        if (pe.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            pe.getNativeEvent().preventDefault();
            pe.getNativeEvent().stopPropagation();
            hide();
        }
    }

    protected void onPreviewEvent(Event.NativePreviewEvent pe) {
        int type = pe.getTypeInt();
        switch (type) {
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEWHEEL:
            case Event.ONSCROLL:
            case Event.ONKEYPRESS:
                XElement target = pe.getNativeEvent().getEventTarget().cast();

                //Ignore targets who are parents of or children of elements with
                // an attribute role=menuitem (which are material-ui dropdowns)
                if (target.findParent("[role=document]", 10) != null
                    || target.child("[role=document]") != null
                    || target.findParent("[id=menu-]", 10) != null) {
                    return;
                }

                if (!getElement().isOrHasChild(target)) {
                    hide();
                }
        }
    }

    @Override
    public Widget asWidget() {
        return con;
    }

    @Override
    public HandlerRegistration addHideHandler(HideEvent.HideHandler handler) {
        return ensureHandlers().addHandler(HideEvent.getType(), handler);
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }
}
