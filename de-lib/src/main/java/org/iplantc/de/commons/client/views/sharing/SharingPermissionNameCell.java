package org.iplantc.de.commons.client.views.sharing;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.client.models.sharing.Sharing;
import org.iplantc.de.commons.share.CommonsModule;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author aramsey
 */
public class SharingPermissionNameCell extends AbstractCell<Sharing> {

    public interface SharingPermissionNameCellAppearance {
        void render(SafeHtmlBuilder safeHtmlBuilder, Sharing sharing, String debugID);
    }

    private final SharingPermissionNameCellAppearance appearance =
            GWT.create(SharingPermissionNameCellAppearance.class);
    private String baseDebugId;

    public SharingPermissionNameCell() {
        super(CLICK);
    }

    @Override
    public void render(Context context, Sharing value, SafeHtmlBuilder sb) {
        String debugID = baseDebugId + value.getId() + CommonsModule.IDs.SHARING_NAME_CELL;
        appearance.render(sb, value, debugID);
    }

    public void setBaseDebugId(String baseDebugId) {
        this.baseDebugId = baseDebugId;
    }
}
