package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Created by sriram on 4/27/17.
 */
public interface EditToolView extends IsWidget{

    Tool getTool();

    interface EditToolViewAppearance {
        String toolName();

        String description();

        String imgName();

        String tag();

        String dockerUrl();

        String cpuShare();

        String memLimit();

        String nwMode();

        String timeLimit();

        String edit();

        String create();

        String restrictions();

        String toolInfo();

        String version();
    }

    boolean validate();
}
