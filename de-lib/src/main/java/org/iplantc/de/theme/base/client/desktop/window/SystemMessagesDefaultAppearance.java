package org.iplantc.de.theme.base.client.desktop.window;

import org.iplantc.de.systemMessages.client.view.MessagesView;

/**
 * Created by sriram on 1/8/18.
 */
public class SystemMessagesDefaultAppearance implements MessagesView.MessagesAppearance {


    public SystemMessagesDefaultAppearance() {
        
    }

    @Override
    public String windowHeight() {
        return "400";
    }

    @Override
    public String windowWidth() {
        return "600";
    }
}
