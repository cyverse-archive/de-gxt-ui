package org.iplantc.de.apps.client.views.list.widgets;

import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.views.list.cells.AppDotMenuCell;
import org.iplantc.de.client.models.apps.App;

import com.google.gwt.event.shared.HasHandlers;

import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class AppDotMenu extends Menu {

    private AppDotMenuCell.AppDotMenuAppearance appearance;
    private MenuItem infoBtn;
    private MenuItem favoriteBtn;
    private MenuItem commentBtn;

    public AppDotMenu(AppDotMenuCell.AppDotMenuAppearance appearance) {
        super();
        this.appearance = appearance;

        addMenuItems();
    }

    void addMenuItems() {
        infoBtn = new MenuItem(appearance.infoText(), appearance.infoIcon());
        favoriteBtn = new MenuItem();
        commentBtn = new MenuItem(appearance.commentText(), appearance.commentIcon());

        add(infoBtn);
        add(favoriteBtn);
        add(commentBtn);
    }

    public void show(App app, HasHandlers hasHandlers, int x, int y) {

        favoriteBtn.setIcon(appearance.favoriteIcon(app));
        favoriteBtn.setText(appearance.favoriteText(app));

        if (hasHandlers != null) {
            infoBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new AppInfoSelectedEvent(app)));
            favoriteBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new AppFavoriteSelectedEvent(app)));
            commentBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new AppCommentSelectedEvent(app)));
        }

        super.showAt(x, y);
    }
}
