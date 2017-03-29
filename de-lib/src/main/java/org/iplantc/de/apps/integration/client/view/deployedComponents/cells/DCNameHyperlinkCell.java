package org.iplantc.de.apps.integration.client.view.deployedComponents.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import org.iplantc.de.apps.integration.client.events.ShowToolInfoEvent;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.inject.Inject;

/**
 * This is a custom cell which is clickable hyper-link of an DC name.
 * 
 * @author sriram
 * 
 */
public class DCNameHyperlinkCell extends AbstractCell<Tool> implements ShowToolInfoEvent.HasShowToolInfoEventHandlers {

    public interface DCNameHyperlinkCellAppearance {

        String ELEMENT_NAME = "DCName";
        SafeHtml render(Tool tool);
    }

    private DCNameHyperlinkCellAppearance appearance;
    private HandlerManager handlerManager;

    @Inject
    public DCNameHyperlinkCell(DCNameHyperlinkCellAppearance appearance) {
        super(CLICK, MOUSEOVER, MOUSEOUT);
        this.appearance = appearance;
    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, Tool value,
            NativeEvent event, ValueUpdater<Tool> valueUpdater) {
        Element eventTarget = Element.as(event.getEventTarget());
        if ((value == null) && !parent.isOrHasChild(eventTarget)) {
            return;
        }

        if (eventTarget.getAttribute("name").equalsIgnoreCase(DCNameHyperlinkCellAppearance.ELEMENT_NAME)) {

            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    doOnClick(value);
                    break;
                case Event.ONMOUSEOVER:
                    doOnMouseOver(eventTarget);
                    break;
                case Event.ONMOUSEOUT:
                    doOnMouseOut(eventTarget);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void render(Cell.Context context, Tool value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }
        sb.appendHtmlConstant("&nbsp;");
        sb.append(appearance.render(value));

    }

    private void doOnClick(final Tool value) {
        ensureHandlers().fireEvent(new ShowToolInfoEvent(value));
    }

    private void doOnMouseOut(Element eventTarget) {
        eventTarget.getStyle().setTextDecoration(TextDecoration.NONE);
    }

    private void doOnMouseOver(Element eventTarget) {
        eventTarget.getStyle().setTextDecoration(TextDecoration.UNDERLINE);
    }

    @Override
    public HandlerRegistration addShowToolInfoEventHandlers(ShowToolInfoEvent.ShowToolInfoEventHandler handler) {
        return ensureHandlers().addHandler(ShowToolInfoEvent.TYPE, handler);
    }

    protected HandlerManager ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }
}
