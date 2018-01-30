package org.iplantc.de.theme.base.client.diskResource.view;

import org.iplantc.de.desktop.client.views.windows.SimpleDownloadWindow;

/**
 * Created by sriram on 1/8/18.
 */
public class SimpleDownloadWindowDefaultAppearance
        implements SimpleDownloadWindow.SimpleDownloadWindowAppearance {

    public SimpleDownloadWindowDefaultAppearance() {
        
    }

    @Override
    public String windowWidth() {
        return "320";
    }

    @Override
    public String windowHeight() {
        return "320";
    }
}
