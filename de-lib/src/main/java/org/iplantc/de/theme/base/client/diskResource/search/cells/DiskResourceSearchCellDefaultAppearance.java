package org.iplantc.de.theme.base.client.diskResource.search.cells;

import org.iplantc.de.diskResource.client.views.search.cells.DiskResourceSearchCell;
import org.iplantc.de.theme.base.client.diskResource.search.SearchMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import com.sencha.gxt.cell.core.client.form.FieldCell;
import com.sencha.gxt.theme.base.client.field.DateCellDefaultAppearance;
import com.sencha.gxt.theme.base.client.field.TriggerFieldDefaultAppearance;

/**
 * This class is a clone-and-own of {@link DateCellDefaultAppearance}.
 * 
 * @author jstroot
 * 
 */
public class DiskResourceSearchCellDefaultAppearance extends TriggerFieldDefaultAppearance implements DiskResourceSearchCell.DiskResourceSearchCellAppearance {

    public interface DiskResourceSearchCellResources extends TriggerFieldResources {

        @Override
        @Source({ "com/sencha/gxt/theme/base/client/field/ValueBaseField.gss",
                  "com/sencha/gxt/theme/base/client/field/TextField.gss",
                  "DiskResourceSearchField.gss"})
        DiskResourceSearchCellStyle css();
        
        @Override
        @Source("org/iplantc/de/resources/client/magnifier.png")
        ImageResource triggerArrow();

        @Override
        @Source("org/iplantc/de/resources/client/magnifier.png")
        ImageResource triggerArrowOver();

        /*
         * @Override
         * 
         * @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
         * ImageResource textBackground();
         */

        // TODO Override images
        @Override
        @Source("org/iplantc/de/resources/client/magnifier.png")
        ImageResource triggerArrowClick();

        @Override
        @Source("org/iplantc/de/resources/client/magnifier.png")
        ImageResource triggerArrowFocus();

    }

    public interface DiskResourceSearchCellStyle extends TriggerFieldStyle {

    }

    private final SearchMessages searchMessages;
    public DiskResourceSearchCellDefaultAppearance() {
        this(GWT.<DiskResourceSearchCellResources> create(DiskResourceSearchCellResources.class),
             GWT.<SearchMessages> create(SearchMessages.class));
    }

    DiskResourceSearchCellDefaultAppearance(final DiskResourceSearchCellResources resources,
                                            final SearchMessages searchMessages) {
        super(resources);
        this.searchMessages = searchMessages;
    }

    @Override
    public String advancedSearchToolTip() {
        return searchMessages.advancedSearchToolTip();
    }
}
