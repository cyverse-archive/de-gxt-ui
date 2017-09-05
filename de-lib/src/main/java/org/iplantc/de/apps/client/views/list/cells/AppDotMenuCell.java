package org.iplantc.de.apps.client.views.list.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.apps.client.views.list.widgets.AppDotMenu;
import org.iplantc.de.apps.shared.AppsModule;
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
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * @author aramsey
 */
public class AppDotMenuCell extends AbstractCell<App> implements HasCell<App, App> {

    public interface AppDotMenuAppearance  {
        void render(Context context,
                    App value,
                    SafeHtmlBuilder sb,
                    String debugId);

        ImageResource dotMenuIcon();

        ImageResource infoIcon();

        String infoText();

        String favoriteText(App app);

        ImageResource favoriteIcon(App app);

        String commentText();

        ImageResource commentIcon();
    }

    private AppDotMenuAppearance appearance;
    private String baseDebugId;
    private HasHandlers hasHandlers;

    public AppDotMenuCell() {
        this(GWT.create(AppDotMenuAppearance.class));
    }

    public AppDotMenuCell(final AppDotMenuAppearance appearance) {
        super(CLICK);
        this.appearance = appearance;
    }

    @Override
    public void render(Context context, App value, SafeHtmlBuilder sb) {
        String debugId = baseDebugId + "." + value.getId() + AppsModule.Ids.APP_DOT_MENU;
        appearance.render(context, value, sb, debugId);
    }

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

    @Override
    public void onBrowserEvent(Context context,
                               Element parent,
                               App value,
                               NativeEvent event,
                               ValueUpdater<App> valueUpdater) {
        Element eventTarget = Element.as(event.getEventTarget());
        if ((value == null) || !parent.isOrHasChild(eventTarget)) {
            return;
        }

        if (parent.isOrHasChild(eventTarget)) {
            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    AppDotMenu menu = new AppDotMenu(appearance);
                    menu.show(value, hasHandlers, event.getClientX(), event.getClientY());
                    break;
                default:
                    break;
            }
        }
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        this.hasHandlers = hasHandlers;
    }

    public void setBaseDebugId(String baseDebugId) {
        this.baseDebugId = baseDebugId;
    }
}
