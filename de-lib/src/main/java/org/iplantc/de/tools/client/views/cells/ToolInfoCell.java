package org.iplantc.de.tools.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.tools.client.events.ShowToolInfoEvent;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * Created by sriram on 5/19/17.
 */
public class ToolInfoCell extends AbstractCell<Tool>
        implements ShowToolInfoEvent.HasShowToolInfoEventHandlers {

    public interface ToolInfoCellAppearance {
        void render(SafeHtmlBuilder sb, String debugId);

        SafeHtml detailsRenderer();

        String detailsDialogWidth();

        String detailsDialogHeight();

        String attributionLabel();

        String descriptionLabel();
    }

    private final ToolInfoCell.ToolInfoCellAppearance appearance;
    private String baseID;
    private HandlerManager handlerManager;

    public ToolInfoCell() {
        this(GWT.<ToolInfoCell.ToolInfoCellAppearance>create(ToolInfoCell.ToolInfoCellAppearance.class));
    }

    public ToolInfoCell(final ToolInfoCell.ToolInfoCellAppearance appearance) {
        super(CLICK);
        this.appearance = appearance;
    }

    @Override
    public void render(Cell.Context context, Tool value, SafeHtmlBuilder sb) {
        String debugId = baseID + "." + value.getId() + ToolsModule.ToolIds.TOOL_INFO_CELL;
        appearance.render(sb, debugId);
    }

    @Override
    public void onBrowserEvent(final Cell.Context context,
                               final Element parent,
                               final Tool value,
                               final NativeEvent event,
                               final ValueUpdater<Tool> valueUpdater) {
        if (value == null) {
            return;
        }

        Element eventTarget = Element.as(event.getEventTarget());
        if (parent.isOrHasChild(eventTarget)) {
            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    doOnClick(value);
                    break;
                default:
                    break;
            }
        }
    }

    public void setBaseDebugId(String baseID) {
        this.baseID = baseID;
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

    private void doOnClick(Tool value) {
        ensureHandlers().fireEvent(new ShowToolInfoEvent(value));
    }

}
