package org.iplantc.de.theme.base.client.notifications;

import org.iplantc.de.notifications.client.views.NotificationView;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;

import com.google.gwt.core.client.GWT;

/**
 * @author aramsey
 */
public class NotificationViewDefaultAppearance implements NotificationView.NotificationViewAppearance {

    private IplantDisplayStrings iplantDisplayStrings;
    private IplantErrorStrings iplantErrorStrings;

    public NotificationViewDefaultAppearance() {
        this(GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             (GWT.<IplantErrorStrings>create(IplantErrorStrings.class)));
    }

    public NotificationViewDefaultAppearance(IplantDisplayStrings iplantDisplayStrings,
                                             IplantErrorStrings iplantErrorStrings) {
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantErrorStrings = iplantErrorStrings;
    }


    @Override
    public String notifications() {
        return iplantDisplayStrings.notifications();
    }

    @Override
    public String notificationDeleteFail() {
        return iplantErrorStrings.notificationDeletFail();
    }

    @Override
    public String notificationMarkAsSeenFail() {
        return iplantErrorStrings.markAsSeenFailed();
    }

    @Override
    public String notificationMarkAsSeenSuccess() {
        return iplantDisplayStrings.markAsSeenSuccess();
    }


    @Override
    public String windowWidth() {
        return "600";
    }

    @Override
    public String windowHeight() {
        return "375";
    }

    @Override
    public int windowMinWidth() {
        return 500;
    }

    @Override
    public int windowMinHeight() {
        return 300;
    }

    @Override
    public String notificationUrlPrompt(String url) {
        return iplantDisplayStrings.notificationUrlPrompt(url);
    }
}
