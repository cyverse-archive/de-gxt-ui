package org.iplantc.de.apps.client.views.list.cells;

import org.iplantc.de.client.models.apps.App;

import com.google.common.collect.Lists;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.inject.Inject;

/**
 * @author aramsey
 */
public class AppTileCell extends CompositeCell<App> {

    public interface AppTileCellAppearance {
        <X> void render(Context context, App value, SafeHtmlBuilder sb, HasCell<App, X> hasCell);
    }

    private AppNameCell appNameCell;
    private AppFavoriteCell appFavoriteCell;
    private AppInfoCell appInfoCell;
    private AppStatusCell appStatusCell;
    private AppCommentCell appCommentCell;
    private AppRatingCell appRatingCell;
    private AppIntegratorCell appIntegratorCell;
    private AppCardCell appCardCell;

    private AppTileCellAppearance appearance;

    @Inject
    public AppTileCell(AppTileCellAppearance appearance,
                       AppNameCell appNameCell,
                       AppFavoriteCell appFavoriteCell,
                       AppInfoCell appInfoCell,
                       AppStatusCell appStatusCell,
                       AppCommentCell appCommentCell,
                       AppRatingCell appRatingCell,
                       AppIntegratorCell appIntegratorCell,
                       AppCardCell appCardCell) {
        super(Lists.newArrayList(appNameCell,
                                appFavoriteCell,
                                appInfoCell,
                                appStatusCell,
                                appCommentCell,
                                appRatingCell,
                                appIntegratorCell,
                                appCardCell));

        this.appNameCell = appNameCell;
        this.appNameCell.setSeparateFavoriteCell(true);
        this.appFavoriteCell = appFavoriteCell;
        this.appInfoCell = appInfoCell;
        this.appStatusCell = appStatusCell;
        this.appCommentCell = appCommentCell;
        this.appRatingCell = appRatingCell;
        this.appIntegratorCell = appIntegratorCell;
        this.appCardCell = appCardCell;
        this.appearance = appearance;
    }

    @Override
    protected <X> void render(Context context, App value, SafeHtmlBuilder sb, HasCell<App, X> hasCell) {
        appearance.render(context, value, sb, hasCell);
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        appNameCell.setHasHandlers(hasHandlers);
        appFavoriteCell.setHasHandlers(hasHandlers);
        appInfoCell.setHasHandlers(hasHandlers);
        appCommentCell.setHasHandlers(hasHandlers);
        appRatingCell.setHasHandlers(hasHandlers);
        appCardCell.setHasHandlers(hasHandlers);
    }

    public void setSearchRegexPattern(String pattern) {
        appNameCell.setSearchRegexPattern(pattern);
        appIntegratorCell.setSearchRegexPattern(pattern);
    }

    public void setDebugBaseId(String baseID) {
        appNameCell.setBaseDebugId(baseID);
        appInfoCell.setBaseDebugId(baseID);
        appCardCell.setBaseDebugId(baseID);
        appStatusCell.setBaseDebugId(baseID);
    }

    public void setCardUrl(String appsCardUrl, String appsCardUrlOptions) {
        appCardCell.setCardUrl(appsCardUrl, appsCardUrlOptions);
    }
}
