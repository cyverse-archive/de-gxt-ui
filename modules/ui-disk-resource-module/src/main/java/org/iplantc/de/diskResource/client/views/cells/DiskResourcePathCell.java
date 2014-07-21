package org.iplantc.de.diskResource.client.views.cells;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.diskResource.client.views.cells.events.DiskResourceNameSelectedEvent;
import org.iplantc.de.diskResource.share.DiskResourceModule;
import org.iplantc.de.resources.client.DiskResourceNameCellStyle;
import org.iplantc.de.resources.client.IplantResources;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.debug.client.DebugInfo;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;

public class DiskResourcePathCell extends AbstractCell<DiskResource> {
    private static final DiskResourceNameCellStyle CSS = IplantResources.RESOURCES.diskResourceNameCss();
    private String baseID;

    public void setBaseDebugId(String baseID) {
        this.baseID = baseID;
    }

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span name=\"drPath\" >{0}</span>")
                SafeHtml
 cell(SafeHtml diskResourceName);

        @SafeHtmlTemplates.Template("<span id='{1}' name=\"drPath\">{0}</span>")
                SafeHtml
 debugCell(SafeHtml diskResourceName,
                          String id);
    }

    final Templates templates = GWT.create(Templates.class);
    private HasHandlers hasHandlers;

    private final DiskResourceAutoBeanFactory drFactory = GWT.create(DiskResourceAutoBeanFactory.class);

    public DiskResourcePathCell() {
        this(true);
    }

    public DiskResourcePathCell(boolean previewEnabled) {
        super(CLICK, MOUSEOVER, MOUSEOUT);
        CSS.ensureInjected();
    }

    @Override
    public void render(Cell.Context context, DiskResource value, SafeHtmlBuilder sb) {
        if (DebugInfo.isDebugIdEnabled() && !Strings.isNullOrEmpty(baseID)) {
            final String debugId = baseID + "." + value.getPath() + DiskResourceModule.Ids.PATH_CELL;
            sb.append(templates.debugCell(SafeHtmlUtils.fromString(value.getPath()), debugId));
        } else {
            sb.append(templates.cell(SafeHtmlUtils.fromString(value.getPath())));
        }
    }

    @Override
    public void onBrowserEvent(Cell.Context context,
                               Element parent,
                               DiskResource value,
                               NativeEvent event,
                               ValueUpdater<DiskResource> valueUpdater) {
        if (value == null) {
            return;
        }
        Element eventTarget = Element.as(event.getEventTarget());
        if (parent.isOrHasChild(eventTarget)) {

            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    doOnClick(eventTarget, value, valueUpdater);
                    break;
                case Event.ONMOUSEOVER:
                    doOnMouseOver(eventTarget, value);
                    break;
                case Event.ONMOUSEOUT:
                    doOnMouseOut(eventTarget, value);
                    break;
                default:
                    break;
            }
        }
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        this.hasHandlers = hasHandlers;
    }

    private void doOnMouseOut(Element eventTarget, DiskResource value) {
        eventTarget.getStyle().setCursor(Cursor.DEFAULT);
        eventTarget.getStyle().setTextDecoration(TextDecoration.NONE);
    }

    private void doOnMouseOver(final Element eventTarget, DiskResource value) {
        eventTarget.getStyle().setCursor(Cursor.POINTER);
        eventTarget.getStyle().setTextDecoration(TextDecoration.UNDERLINE);
    }

    private void doOnClick(Element eventTarget,
                           DiskResource value,
                           ValueUpdater<DiskResource> valueUpdater) {

        if (hasHandlers != null) {
            Folder request = drFactory.folder().as();
            request.setPath(DiskResourceUtil.parseParent(value.getPath()));
            hasHandlers.fireEvent(new DiskResourceNameSelectedEvent(request));
        }
    }



}
