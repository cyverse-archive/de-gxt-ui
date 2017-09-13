package org.iplantc.de.diskResource.client.views.grid.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.diskResource.client.views.widgets.DiskResourceDotMenu;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * A cell that contains the "dot" menu (3 vertical dots).  When clicked the menu
 * containing the DiskResource favorites, sharing, metadata, comments, and data links buttons is displayed
 * @author aramsey
 */
public class DiskResourceDotMenuCell extends AbstractCell<DiskResource> implements HasCell<DiskResource, DiskResource> {

    public interface DiskResourceDotMenuAppearance  {
        void render(Context context, DiskResource value, SafeHtmlBuilder sb, String debugId);

        ImageResource dotMenuIcon();

        String favoriteText(DiskResource diskResource);

        ImageResource favoriteIcon(DiskResource diskResource);

        String commentText();

        ImageResource commentIcon();

        String dataLinkText();

        ImageResource dataLinkIcon();

        String shareText();

        ImageResource shareIcon();

        String metadataText();

        ImageResource metadataIcon();
    }

    private DiskResourceDotMenuAppearance appearance;
    private String baseDebugId;
    private HasHandlers hasHandlers;

    public DiskResourceDotMenuCell() {
        this(GWT.create(DiskResourceDotMenuAppearance.class));
    }

    public DiskResourceDotMenuCell(final DiskResourceDotMenuAppearance appearance) {
        super(CLICK);
        this.appearance = appearance;
    }

    @Override
    public void render(Context context, DiskResource value, SafeHtmlBuilder sb) {
        if (!value.isFilter()) {
            String debugId = getDebugId(value);
            appearance.render(context, value, sb, debugId);
        }
    }

    @Override
    public Cell<DiskResource> getCell() {
        return this;
    }

    @Override
    public FieldUpdater<DiskResource, DiskResource> getFieldUpdater() {
        return null;
    }

    @Override
    public DiskResource getValue(DiskResource object) {
        return object;
    }

    @Override
    public void onBrowserEvent(Context context,
                               Element parent,
                               DiskResource value,
                               NativeEvent event,
                               ValueUpdater<DiskResource> valueUpdater) {
        Element eventTarget = Element.as(event.getEventTarget());
        if ((value == null) || !parent.isOrHasChild(eventTarget)) {
            return;
        }

        if (parent.isOrHasChild(eventTarget)) {
            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    if (!value.isFilter()) {
                        DiskResourceDotMenu menu = new DiskResourceDotMenu(appearance, value, hasHandlers);
                        menu.showAt(event.getClientX(), event.getClientY());
                        menu.ensureDebugId(getDebugId(value));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    String getDebugId(DiskResource value) {
        return baseDebugId + "." + value.getPath() + DiskResourceModule.Ids.DOT_MENU;
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        this.hasHandlers = hasHandlers;
    }

    public void setBaseDebugId(String baseDebugId) {
        this.baseDebugId = baseDebugId;
    }
}
