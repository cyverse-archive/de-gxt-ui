package org.iplantc.de.tools.client.views.manage;

import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.widget.core.client.container.SimpleContainer;

/**
 * Created by sriram on 5/30/17.
 */
public interface ToolInfoView {

    interface ToolInfoAppearance {
        SafeHtml detailsRenderer();

        String detailsDialogWidth();

        String detailsDialogHeight();

        String attributionLabel();

        String descriptionLabel();

        String tabWidth();

        String tabHeight();

        String toolInformation();

        String appsUsingTool();

        String restrictions();

        String memLimit();

        String pidsLimit();

        String timeLimit();

        String networkingMode();

        String bridge();

        String enabled();

        String disabled();

        String notApplicable();
    }

    SimpleContainer getAppListContainer();

}


