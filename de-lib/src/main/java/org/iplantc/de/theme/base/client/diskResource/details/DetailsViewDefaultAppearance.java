package org.iplantc.de.theme.base.client.diskResource.details;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import org.iplantc.de.diskResource.client.DetailsView;

/**
 * @author jstroot
 */
public class DetailsViewDefaultAppearance implements DetailsView.Appearance {

    public interface Resources extends ClientBundle {
        @Source("DetailsViewStyle.gss")
        DetailsViewStyle css();
    }

    private Resources resources;

    public DetailsViewDefaultAppearance() {
        this(GWT.<Resources>create(Resources.class));
    }

    public DetailsViewDefaultAppearance(final Resources resources) {
        this.resources = resources;
        this.resources.css().ensureInjected();
    }

    @Override
    public DetailsViewStyle css() {
        return resources.css();
    }

}
