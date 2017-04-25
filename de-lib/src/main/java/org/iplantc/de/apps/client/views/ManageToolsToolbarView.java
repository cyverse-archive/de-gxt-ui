package org.iplantc.de.apps.client.views;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Created by sriram on 4/20/17.
 */
public interface ManageToolsToolbarView extends IsWidget{

    interface ManageToolsToolbarApperance {

        String tools();

        String requestTool();

        String edit();

        String delete();

        String useInApp();

        String share();

        String shareCollab();

        String sharePublic();

        String submitForPublicUse();

        String refresh();

        ImageResource refreshIcon();

        ImageResource shareToolIcon();

        ImageResource submitForPublicIcon();

        String searchTools();

        String addTool();
    }


}
