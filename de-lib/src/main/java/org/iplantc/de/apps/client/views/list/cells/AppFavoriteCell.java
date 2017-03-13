package org.iplantc.de.apps.client.views.list.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
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
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * @author jstroot
 */
public class AppFavoriteCell extends AbstractCell<App> implements HasCell<App, App> {

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

    public interface AppFavoriteCellAppearance {
        String addAppToFav();

        String appUnavailable();

        String favoriteClass();

        String favoriteDisabledClass();

        String remAppFromFav();

        String favoriteAddClass();

        void render(SafeHtmlBuilder sb, String imgName, String imgClassName, String imgToolTip,
                    String debugId);

        String featureNotSupported();
    }

    private final AppFavoriteCellAppearance appearance;
    private String baseID;
    private HasHandlers hasHandlers;

    public AppFavoriteCell() {
        this(GWT.<AppFavoriteCellAppearance> create(AppFavoriteCellAppearance.class));
    }

    public AppFavoriteCell(final AppFavoriteCellAppearance appearance) {
        super(CLICK);
        this.appearance = appearance;
    }

    @Override
    public void render(Cell.Context context, App value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }

        String imgName, imgClassName, imgToolTip;
        
        if (value.getAppType().equalsIgnoreCase(App.EXTERNAL_APP)) {
            imgName = "disabled";
            imgClassName = appearance.favoriteDisabledClass();
            imgToolTip = appearance.featureNotSupported();
        } else if (!value.isDisabled() && value.isFavorite()) {
            imgName = "fav";
            imgClassName = appearance.favoriteClass();
            imgToolTip = appearance.remAppFromFav();
        } else if (!value.isDisabled() && !value.isFavorite()) {
            imgName = "fav";
            imgClassName = appearance.favoriteAddClass();
            imgToolTip = appearance.addAppToFav();
        } else{
            imgName = "disabled";
            imgClassName = appearance.favoriteDisabledClass();
            imgToolTip = appearance.appUnavailable();
        }

        String debugId = baseID + "." + value.getId() + AppsModule.Ids.APP_FAVORITE_CELL;
        appearance.render(sb, imgName, imgClassName, imgToolTip, debugId);
    }

    @Override
    public void onBrowserEvent(final Cell.Context context,
                               final Element parent,
                               final App value,
                               final NativeEvent event,
                               final ValueUpdater<App> valueUpdater) {
        if (value == null) {
            return;
        }

        Element eventTarget = Element.as(event.getEventTarget());
        if (parent.isOrHasChild(eventTarget) && eventTarget.getAttribute("name").equalsIgnoreCase("fav")
                && !value.isDisabled()) {

            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    if (hasHandlers != null) {
                        hasHandlers.fireEvent(new AppFavoriteSelectedEvent(value));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void setBaseDebugId(String baseID) {
        this.baseID = baseID;
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        this.hasHandlers = hasHandlers;
    }

}
