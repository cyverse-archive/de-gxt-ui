package org.iplantc.de.diskResource.client.views.grid.cells;

import org.iplantc.de.client.util.DiskResourceUtil;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * A <code>Cell</code> for converting bytes as integers into human readable <code>File</code> sizes.
 *
 * @author psarando
 */
public class DiskResourceSizeCell extends AbstractCell<Long> {
    final DiskResourceUtil diskResourceUtil = DiskResourceUtil.getInstance();

    @Override
    public void render(Context context, Long value, SafeHtmlBuilder sb) {
        if (value != null) {
            sb.appendEscaped(diskResourceUtil.formatFileSize(value.toString()));
        }
    }
}
