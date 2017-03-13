package org.iplantc.de.apps.client.views.list.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent;
import org.iplantc.de.client.models.apps.App;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * @author jstroot
 */
public class AppCommentCell extends AbstractCell<App> implements HasCell<App, App> {

    @Override
    public Cell<App> getCell() {
        return this;
    }

    @Override
    public FieldUpdater<App, App> getFieldUpdater() {
        return null;
    }

    @Override
    public App getValue(App object) {
        return object;
    }

    public interface AppCommentCellAppearance {
        void render(Context context, App value, SafeHtmlBuilder sb);

        String featureNotSupported();
    }

    private final AppCommentCellAppearance appearance;
    private HasHandlers hasHandlers;

    public AppCommentCell() {
        this(GWT.<AppCommentCellAppearance> create(AppCommentCellAppearance.class));
    }

    public AppCommentCell(AppCommentCellAppearance appearance) {
        super(CLICK, MOUSEOVER, MOUSEOUT);
        this.appearance = appearance;
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, App value, SafeHtmlBuilder sb) {
        appearance.render(context, value, sb);
    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, App value, NativeEvent event,
            ValueUpdater<App> valueUpdater) {
        if (value == null) {
            return;
        }

        Element eventTarget = Element.as(event.getEventTarget());
        if (parent.isOrHasChild(eventTarget)) {

            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    if (hasHandlers != null && !value.getAppType().equalsIgnoreCase(App.EXTERNAL_APP)) {
                        hasHandlers.fireEvent(new AppCommentSelectedEvent(value));
                    }
                    break;
                case Event.ONMOUSEOVER:
                    if (value.getAppType().equalsIgnoreCase(App.EXTERNAL_APP)) {
                        eventTarget.setTitle(appearance.featureNotSupported());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void setHasHandlers(HasHandlers handlerManager) {
        hasHandlers = handlerManager;
    }

}
