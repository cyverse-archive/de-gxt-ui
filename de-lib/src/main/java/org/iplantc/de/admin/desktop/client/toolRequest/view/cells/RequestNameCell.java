package org.iplantc.de.admin.desktop.client.toolRequest.view.cells;

import org.iplantc.de.admin.desktop.client.toolRequest.ToolRequestView;
import org.iplantc.de.client.models.toolRequests.ToolRequest;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Created by sriram on 6/2/17.
 */
public class RequestNameCell extends AbstractCell<ToolRequest> {

    private final ToolRequestView.ToolRequestViewAppearance appearance =
            GWT.create(ToolRequestView.ToolRequestViewAppearance.class);

    @Override
    public void render(Context context, ToolRequest toolRequest, SafeHtmlBuilder safeHtmlBuilder) {
        appearance.renderRequestName(safeHtmlBuilder, toolRequest);
    }
}
