package org.iplantc.de.apps.client.views.list.cells;

import org.iplantc.de.client.models.apps.App;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author aramsey
 */
public class AppExecutionSystemCell extends AbstractCell<String> implements HasCell<App, String> {

    @Override
    public Cell<String> getCell() {
        return this;
    }

    @Override
    public FieldUpdater<App, String> getFieldUpdater() {
        return null;
    }

    @Override
    public String getValue(App object) {
        return object.getSystemId();
    }

    public AppExecutionSystemCell() { }


    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
        sb.appendEscaped(value);
    }
}
