package org.iplantc.de.apps.client.views.list.widgets;

import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.views.list.cells.AppDotMenuCell;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.apps.App;

import com.google.gwt.event.shared.HasHandlers;

import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * A menu containing the App Info, App Favorite, and App Comment buttons as menu items
 * @author aramsey
 */
public class AppDotMenu extends Menu {

    private AppDotMenuCell.AppDotMenuAppearance appearance;
    private MenuItem infoBtn;
    private MenuItem favoriteBtn;
    private MenuItem commentBtn;

    public AppDotMenu(AppDotMenuCell.AppDotMenuAppearance appearance,
                      App app,
                      HasHandlers hasHandlers) {
        super();
        this.appearance = appearance;

        addMenuItems(app);
        addHandlers(hasHandlers, app);
    }

    void addHandlers(HasHandlers hasHandlers, App app) {
        if (hasHandlers != null) {
            infoBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new AppInfoSelectedEvent(app)));
            favoriteBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new AppFavoriteSelectedEvent(app)));
            commentBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new AppCommentSelectedEvent(app)));
        }
    }

    void addMenuItems(App app) {
        infoBtn = new MenuItem(appearance.infoText(), appearance.infoIcon());
        favoriteBtn = new MenuItem(appearance.favoriteText(app), appearance.favoriteIcon(app));
        commentBtn = new MenuItem(appearance.commentText(app), appearance.commentIcon());

        add(infoBtn);
        add(favoriteBtn);
        add(commentBtn);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        infoBtn.setId(baseID + AppsModule.Ids.APP_INFO_CELL);
        favoriteBtn.setId(baseID + AppsModule.Ids.APP_FAVORITE_CELL);
        commentBtn.setId(baseID + AppsModule.Ids.APP_COMMENT_CELL);
    }
}
