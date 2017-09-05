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
    private AppStatusCell appStatusCell;
    private AppRatingCell appRatingCell;
    private AppIntegratorCell appIntegratorCell;
    private AppCardCell appCardCell;
    private AppDotMenuCell appDotMenuCell;

    private AppTileCellAppearance appearance;

    @Inject
    public AppTileCell(AppTileCellAppearance appearance,
                       AppNameCell appNameCell,
                       AppStatusCell appStatusCell,
                       AppRatingCell appRatingCell,
                       AppIntegratorCell appIntegratorCell,
                       AppDotMenuCell appDotMenuCell,
                       AppCardCell appCardCell) {
        super(Lists.newArrayList(appNameCell,
                                 appStatusCell,
                                 appRatingCell,
                                 appIntegratorCell, appDotMenuCell,
                                 appCardCell));

        this.appNameCell = appNameCell;
        this.appNameCell.setSeparateFavoriteCell(true);
        this.appStatusCell = appStatusCell;
        this.appRatingCell = appRatingCell;
        this.appIntegratorCell = appIntegratorCell;
        this.appDotMenuCell = appDotMenuCell;
        this.appCardCell = appCardCell;
        this.appearance = appearance;
    }

    @Override
    protected <X> void render(Context context, App value, SafeHtmlBuilder sb, HasCell<App, X> hasCell) {
        appearance.render(context, value, sb, hasCell);
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        appNameCell.setHasHandlers(hasHandlers);
        appRatingCell.setHasHandlers(hasHandlers);
        appCardCell.setHasHandlers(hasHandlers);
        appDotMenuCell.setHasHandlers(hasHandlers);
    }

    public void setSearchRegexPattern(String pattern) {
        appNameCell.setSearchRegexPattern(pattern);
        appIntegratorCell.setSearchRegexPattern(pattern);
    }

    public void setDebugBaseId(String baseID) {
        appNameCell.setBaseDebugId(baseID);
        appCardCell.setBaseDebugId(baseID);
        appStatusCell.setBaseDebugId(baseID);
        appDotMenuCell.setBaseDebugId(baseID);
    }

    public void setCardUrl(String appsCardUrl, String appsCardUrlOptions) {
        appCardCell.setCardUrl(appsCardUrl, appsCardUrlOptions);
    }
}
