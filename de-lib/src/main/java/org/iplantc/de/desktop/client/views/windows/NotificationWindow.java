package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.NotifyWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.notifications.client.views.NotificationView;

import com.google.common.base.Strings;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;

/**
 * @author sriram, jstroot
 */
public class NotificationWindow extends WindowBase {

    private NotificationView.Presenter presenter;
    private NotificationView.NotificationViewAppearance notificationViewAppearance;

    @Inject
    NotificationWindow(NotificationView.Presenter presenter,
                       NotificationView.NotificationViewAppearance appearance) {
        this.presenter = presenter;
        this.notificationViewAppearance = appearance;
        setHeading(appearance.notifications());
        ensureDebugId(DeModule.WindowIds.NOTIFICATION);

        setMinHeight(appearance.windowMinHeight());
        setMinHeight(appearance.windowMinWidth());
        setBodyStyle("background-color: white;");
    }

    @Override
    public <C extends WindowConfig> void show(C windowConfig, String tag,
                                              boolean isMaximizable) {
        super.show(windowConfig, tag, isMaximizable);
        NotifyWindowConfig notifyWindowConfig = (NotifyWindowConfig) windowConfig;
        filterByCategory(notifyWindowConfig);
    }

    @Override
    public <C extends WindowConfig> void update(C config) {
        NotifyWindowConfig notifyWindowConfig = (NotifyWindowConfig)config;
        filterByCategory(notifyWindowConfig);
    }

    protected void filterByCategory(NotifyWindowConfig notifyWindowConfig) {
        if (notifyWindowConfig != null) {
            presenter.go(this,
                         DeModule.WindowIds.NOTIFICATION,
                         NotificationCategory.fromTypeString(notifyWindowConfig.getFilter()));
        } else {
            presenter.go(this, DeModule.WindowIds.NOTIFICATION, NotificationCategory.ALL);
        }
    }

    @Override
    public WindowConfig getWindowConfig() {
        return ConfigFactory.notifyWindowConfig(presenter.getCurrentCategory());
    }

    @Override
    public String getWindowType() {
        return WindowType.NOTIFICATIONS.toString();
    }

    @Override
    public FastMap<String> getAdditionalWindowStates() {
        return null;
    }

    @Override
    public void restoreWindowState() {
        if (getStateId().equals(ws.getTag())) {
            super.restoreWindowState();
            String width = ws.getWidth();
            String height = ws.getHeight();
            setSize((Strings.isNullOrEmpty(width)) ? notificationViewAppearance.windowWidth() : width,
                    (Strings.isNullOrEmpty(height)) ?
                    notificationViewAppearance.windowHeight() :
                    height);
        }
    }
}
