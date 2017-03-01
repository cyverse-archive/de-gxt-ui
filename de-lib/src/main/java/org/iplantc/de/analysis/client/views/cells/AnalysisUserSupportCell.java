package org.iplantc.de.analysis.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import org.iplantc.de.analysis.client.events.selection.AnalysisUserSupportRequestedEvent;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisExecutionStatus;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * Created by sriram on 11/17/16.
 */
public class AnalysisUserSupportCell extends AbstractCell<Analysis> {

    public interface AnalysisUserSupportCellAppearance {
        String ELEMENT_NAME = "support";
        void render(Context context, Analysis value, SafeHtmlBuilder sb);

        void doOnMouseOut(Element eventTarget, Analysis value);

        void doOnMouseOver(Element eventTarget, Analysis value);
    }

    private final AnalysisUserSupportCellAppearance appearance;
    private HasHandlers hasHandlers;

    public AnalysisUserSupportCell() {
        this(GWT.<AnalysisUserSupportCellAppearance>create(AnalysisUserSupportCellAppearance.class));
    }

    public AnalysisUserSupportCell(AnalysisUserSupportCellAppearance appearance){
        super(CLICK, MOUSEOVER, MOUSEOUT);
        this.appearance = appearance;
    }

    @Override
    public void render(Context context, Analysis value, SafeHtmlBuilder sb) {
        appearance.render(context, value, sb);
    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, Analysis value, NativeEvent event, ValueUpdater<Analysis> valueUpdater) {
        if (value == null) {
            return;
        }

        Element eventTarget = Element.as(event.getEventTarget());
        if (parent.isOrHasChild(eventTarget)) {

            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    doOnClick(value);
                    break;
                case Event.ONMOUSEOVER:
                    appearance.doOnMouseOver(eventTarget, value);
                    break;
                case Event.ONMOUSEOUT:
                    appearance.doOnMouseOut(eventTarget, value);
                    break;
                default:
                    break;
            }
        }
    }

    public void setHasHandlers(HasHandlers handlerManager) {
        hasHandlers = handlerManager;
    }

    private void doOnClick(Analysis value) {
        if (hasHandlers != null && value.isShareable()
            && !value.getStatus().equalsIgnoreCase(AnalysisExecutionStatus.CANCELED.toString())) {
            hasHandlers.fireEvent(new AnalysisUserSupportRequestedEvent(value));
        }

   }
}
