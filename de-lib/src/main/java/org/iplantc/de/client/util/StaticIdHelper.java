package org.iplantc.de.client.util;

import com.google.common.base.Preconditions;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * @author aramsey
 */
public class StaticIdHelper {

    private static StaticIdHelper INSTANCE;

    StaticIdHelper() {

    }

    public static StaticIdHelper getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new StaticIdHelper();
        }

        return INSTANCE;
    }

    /**
     * Sets the static IDs for the column headers in a grid
     * This does not work if called simply within a view's onEnsureDebugId method
     * as the headers will not have been rendered yet.
     * This can be called successfully using the grid's ViewReadyHandler (which can
     * then be added to the grid within the onEnsureDebugId method)
     * @param baseID
     * @param grid
     */
    public void gridColumnHeaders(String baseID, Grid<?> grid) {
        Preconditions.checkNotNull(baseID);
        Preconditions.checkNotNull(grid);

        XElement header = grid.getView().getHeader().getElement();
        Element columnHeader = header.child("td");

        while (columnHeader != null) {
            String headerName = columnHeader.getInnerText().toLowerCase().replace(" ", "_");
            columnHeader.setId(baseID + "." + headerName);
            columnHeader = columnHeader.getNextSiblingElement();
        }
    }

}
