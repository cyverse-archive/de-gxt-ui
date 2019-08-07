package org.iplantc.de.theme.base.client.fileViewers.cells;

import org.iplantc.de.client.models.viewer.VizUrl;
import org.iplantc.de.fileViewers.client.views.cells.UrlCell;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author jstroot
 */
public class UrlCellDefaultAppearance implements UrlCell.UrlCellAppearance {
    @Override
    public void render(SafeHtmlBuilder sb, VizUrl model) {
        // TODO JDS We should use CssResource here
        sb.appendHtmlConstant("<div style=\"cursor:pointer;text-decoration:underline;white-space:pre-wrap;\">"
                                  + model.getUrl() + "</div>");
    }

    @Override
    public String urlExternalWindowWidthHeight() {
        return "width=800,height=600";
    }
}
