package org.iplantc.de.apps.client.views.list;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent.AppCommentSelectedEventHandler;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppNameSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.models.AppProperties;
import org.iplantc.de.apps.client.views.list.cells.AppDotMenuCell;
import org.iplantc.de.apps.client.views.list.cells.AppInfoCell;
import org.iplantc.de.apps.client.views.list.cells.AppIntegratorCell;
import org.iplantc.de.apps.client.views.list.cells.AppNameCell;
import org.iplantc.de.apps.client.views.list.cells.AppRatingCell;
import org.iplantc.de.apps.client.views.list.cells.AppStatusCell;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppNameComparator;
import org.iplantc.de.client.models.apps.AppRatingComparator;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jstroot
 */
public class AppColumnModel extends ColumnModel<App> implements AppInfoSelectedEvent.HasAppInfoSelectedEventHandlers,
                                                                AppNameSelectedEvent.HasAppNameSelectedEventHandlers,
                                                                AppFavoriteSelectedEvent.HasAppFavoriteSelectedEventHandlers,
                                                                AppCommentSelectedEvent.HasAppCommentSelectedEventHandlers,
                                                                AppRatingSelected.HasAppRatingSelectedEventHandlers,
                                                                AppRatingDeselected.HasAppRatingDeselectedHandlers {

    public AppColumnModel(final AppsListView.AppsListAppearance appearance) {
        super(createColumnConfigList(appearance));

        // Set handler managers on appropriate cells so they can fire events.
        for (ColumnConfig<App, ?> colConfig : configs) {
            Cell<?> cell = colConfig.getCell();
            if (cell instanceof AppNameCell) {
                ((AppNameCell)cell).setHasHandlers(ensureHandlers());
            } else if(cell instanceof AppRatingCell) {
                ((AppRatingCell)cell).setHasHandlers(ensureHandlers());
            } else if (cell instanceof AppDotMenuCell) {
                ((AppDotMenuCell)cell).setHasHandlers(ensureHandlers());
            }
        }
    }

    public static List<ColumnConfig<App, ?>> createColumnConfigList(final AppsListView.AppsListAppearance appearance) {
        AppProperties props = GWT.create(AppProperties.class);
        List<ColumnConfig<App, ?>> list = new ArrayList<>();

        ColumnConfig<App, String> system = new ColumnConfig<>(props.systemId(), appearance.executionSystemWidth(), appearance.executionSystemLabel());

        ColumnConfig<App, App> status = new ColumnConfig<>(new IdentityValueProvider<App>(""), 25);
        status.setHeader("");

        ColumnConfig<App, App> name = new ColumnConfig<>(new IdentityValueProvider<App>("name"), //$NON-NLS-1$
                                                         180,
                                                         appearance.nameColumnLabel());

        ColumnConfig<App, String> integrator = new ColumnConfig<>(props.integratorName(),
                                                                  115,
                                                                  appearance.integratedByColumnLabel());

        ColumnConfig<App, App> rating = new ColumnConfig<>(new IdentityValueProvider<App>("rating"), 125, appearance.ratingColumnLabel()); //$NON-NLS-1$

        ColumnConfig<App, App> dotMenu = new ColumnConfig<>(new IdentityValueProvider<App>(), 30); //$NON-NLS-1$

        dotMenu.setHeader("");

        name.setComparator(new AppNameComparator());
        rating.setComparator(new AppRatingComparator());
        status.setSortable(false);
        dotMenu.setSortable(false);
        system.setSortable(false);

        status.setMenuDisabled(true);
        status.setHideable(false);
        status.setResizable(false);
        name.setResizable(true);
        // rating.setResizable(false);
        dotMenu.setResizable(false);

        status.setFixed(true);
        rating.setFixed(true);
        dotMenu.setFixed(true);
        dotMenu.setHideable(false);

        status.setCell(new AppStatusCell());
        AppNameCell appNameCell = new AppNameCell();
        appNameCell.setSeparateFavoriteCell(true);
        name.setCell(appNameCell);
        integrator.setCell(new AppIntegratorCell());
        rating.setCell(new AppRatingCell());
        dotMenu.setCell(new AppDotMenuCell());

        rating.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        list.add(status);
        list.add(name);
        list.add(integrator);
        list.add(rating);
        list.add(system);
        list.add(dotMenu);

        return list;
    }

    //<editor-fold desc="Handler Registrations">
    @Override
    public HandlerRegistration addAppInfoSelectedEventHandler(AppInfoSelectedEvent.AppInfoSelectedEventHandler handler) {
        return ensureHandlers().addHandler(AppInfoSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAppNameSelectedEventHandler(AppNameSelectedEvent.AppNameSelectedEventHandler handler) {
        return ensureHandlers().addHandler(AppNameSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAppFavoriteSelectedEventHandlers(AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler handler) {
        return ensureHandlers().addHandler(AppFavoriteSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAppCommentSelectedEventHandlers(AppCommentSelectedEventHandler handler) {
        return ensureHandlers().addHandler(AppCommentSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAppRatingDeselectedHandler(AppRatingDeselected.AppRatingDeselectedHandler handler) {
        return ensureHandlers().addHandler(AppRatingDeselected.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAppRatingSelectedHandler(AppRatingSelected.AppRatingSelectedHandler handler) {
        return ensureHandlers().addHandler(AppRatingSelected.TYPE, handler);
    }
    //</editor-fold>

    public void ensureDebugId(String baseID) {
        for (ColumnConfig<App, ?> cc : configs) {
            Cell<?> cell = cc.getCell();
            if (cell instanceof AppInfoCell) {
                ((AppInfoCell)cell).setBaseDebugId(baseID);
            } else if (cell instanceof AppNameCell) {
                ((AppNameCell)cell).setBaseDebugId(baseID);
            } else if (cell instanceof AppStatusCell) {
                ((AppStatusCell)cell).setBaseDebugId(baseID);
            } else if (cell instanceof AppDotMenuCell) {
                ((AppDotMenuCell)cell).setBaseDebugId(baseID);
            }
        }

    }

    public void setSearchRegexPattern(String pattern) {
        for (ColumnConfig<App, ?> cc : configs) {
            if (cc.getCell() instanceof AppNameCell) {
                ((AppNameCell)cc.getCell()).setSearchRegexPattern(pattern);
            } else if (cc.getCell() instanceof AppIntegratorCell) {
                ((AppIntegratorCell)cc.getCell()).setSearchRegexPattern(pattern);
            }
        }
    }
}
