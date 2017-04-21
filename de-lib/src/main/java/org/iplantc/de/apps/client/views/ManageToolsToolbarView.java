package org.iplantc.de.apps.client.views;

import com.google.gwt.resources.client.ImageResource;

/**
 * Created by sriram on 4/20/17.
 */
public interface ManageToolsToolbarView {

    interface ManageToolsToolbarApperance {

        String tools();

        String requestTool();

        String edit();

        String delete();

        String useInApp();

        String share();

        String shareCollab();

        String sharePublic();

        String name();

        String version();

        String imaName();

        String status();

        String submitForPublicUse();

        String refresh();

        ImageResource refreshIcon();

        ImageResource shareToolIcon();

        ImageResource submitForPublicIcon();

        String searchTools();
    }


}
