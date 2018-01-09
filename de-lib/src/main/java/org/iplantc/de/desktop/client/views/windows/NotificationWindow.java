package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.NotifyWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.notifications.client.views.NotificationView;

import com.google.common.base.Strings;
import com.google.inject.Inject;

/**
 * @author sriram, jstroot
 */
public class NotificationWindow extends IplantWindowBase {

    private NotificationView.Presenter presenter;
    private NotificationView.NotificationViewAppearance appearance;

    @Inject
    NotificationWindow(NotificationView.Presenter presenter,
                       NotificationView.NotificationViewAppearance appearance,
                       UserInfo userInfo) {
        this.presenter = presenter;
        this.appearance = appearance;
        this.userInfo = userInfo;
        setHeading(appearance.notifications());
        ensureDebugId(DeModule.WindowIds.NOTIFICATION);
        String width = getSavedWidth(WindowType.NOTIFICATIONS.toString());
        String height = getSavedHeight(WindowType.NOTIFICATIONS.toString());
        setSize((Strings.isNullOrEmpty(width)) ? appearance.windowWidth() : width,
                (Strings.isNullOrEmpty(height)) ? appearance.windowHeight() : height);
        setMinHeight(appearance.windowMinHeight());
        setMinHeight(appearance.windowMinWidth());
    }

    @Override
    public <C extends WindowConfig> void show(C windowConfig, String tag,
                                              boolean isMaximizable) {
        NotifyWindowConfig notifyWindowConfig = (NotifyWindowConfig) windowConfig;

        presenter.go(this);
        if (notifyWindowConfig != null) {
            presenter.filterBy(notifyWindowConfig.getSortCategory());
        }
        super.show(windowConfig, tag, isMaximizable);
    }

    @Override
    public <C extends WindowConfig> void update(C config) {
        NotifyWindowConfig notifyWindowConfig = (NotifyWindowConfig) config;
        if (notifyWindowConfig != null) {
            presenter.filterBy(notifyWindowConfig.getSortCategory());
        }
    }

    @Override
    public WindowState getWindowState() {
        NotifyWindowConfig config = ConfigFactory.notifyWindowConfig(presenter.getCurrentCategory());
        return createWindowState(config);
    }

    @Override
    public void hide() {
        saveHeight(WindowType.NOTIFICATIONS.toString());
        saveWidth(WindowType.NOTIFICATIONS.toString());
        super.hide();
    }

}
