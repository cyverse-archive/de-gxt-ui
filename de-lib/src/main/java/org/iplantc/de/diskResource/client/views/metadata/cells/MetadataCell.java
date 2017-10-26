package org.iplantc.de.diskResource.client.views.metadata.cells;

import org.iplantc.de.diskResource.client.MetadataView;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.inject.Inject;

public final class MetadataCell extends AbstractCell<String> {
    private MetadataView.Appearance appearance;

    @Inject
    public MetadataCell(MetadataView.Appearance appearance) {
        this.appearance = appearance;
    }

    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
        appearance.renderMetadataCell(sb, value);
    }
}
