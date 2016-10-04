package org.iplantc.de.apps.client.views.list.cells;

import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.apps.App;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author jstroot
 */
public class AppStatusCell extends AbstractCell<App> implements HasCell<App, App> {

    private String baseID;

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

    public interface AppStatusCellAppearance {
        void render(Context context, App value, SafeHtmlBuilder sb, String debugId);
    }

    private final AppStatusCellAppearance appearance;

    public AppStatusCell() {
        this(GWT.<AppStatusCellAppearance> create(AppStatusCellAppearance.class));
    }

    public AppStatusCell(AppStatusCellAppearance appearance) {
        this.appearance = appearance;
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, App value, SafeHtmlBuilder sb) {
        String debugId = baseID + "." + value.getId() + AppsModule.Ids.APP_STATUS_CELL;
        appearance.render(context, value, sb, debugId);
    }

    public void setBaseDebugId(String baseID) {
        this.baseID = baseID;
    }

}
