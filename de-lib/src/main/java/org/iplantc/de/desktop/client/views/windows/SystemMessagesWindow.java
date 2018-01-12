package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.SystemMessagesWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.systemMessages.client.presenter.MessagesPresenter;
import org.iplantc.de.systemMessages.client.view.MessagesView;
import org.iplantc.de.systemMessages.shared.SystemMessages;

import com.google.common.base.Strings;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

/**
 * The window for displaying all active system messages.
 * @author jstroot
 */
public final class SystemMessagesWindow extends WindowBase {

    private MessagesPresenter presenter;


    @Inject
    SystemMessagesWindow(final IplantDisplayStrings displayStrings,
                         final UserInfo userInfo,
                         final MessagesView.MessagesAppearance appearance) {
        this.userInfo = userInfo;
        setHeading(displayStrings.systemMessagesLabel());
        WindowState ws = getWindowStateFromLocalStorage();
        String width = ws.getWidth();
        String height = ws.getHeight();
        setSize((Strings.isNullOrEmpty(width)) ?computeDefaultWidth(appearance) + "" : width,
                (Strings.isNullOrEmpty(height)) ? computeDefaultHeight(appearance) + "" : height);
        setMinHeight(Integer.parseInt(appearance.windowHeight()));
        setMinWidth(Integer.parseInt(appearance.windowWidth()));
    }

    @Override
    public <C extends WindowConfig> void show(C windowConfig, String tag,
                                              boolean isMaximizable) {
        this.presenter = new MessagesPresenter(((SystemMessagesWindowConfig)windowConfig).getSelectedMessage());
        presenter.go(this);
        super.show(windowConfig, tag, isMaximizable);
        ensureDebugId(DeModule.WindowIds.SYSTEM_MESSAGES);
    }

    private static int computeDefaultHeight(MessagesView.MessagesAppearance appearance) {
        return Math.max(Integer.parseInt(appearance.windowHeight()), Window.getClientHeight() / 3);
    }

    private static int computeDefaultWidth(MessagesView.MessagesAppearance appearance) {
        return Math.max(Integer.parseInt(appearance.windowWidth()), Window.getClientWidth() / 3);
    }


    @Override
    public WindowConfig getWindowConfig() {
        final String selMsg = presenter.getSelectedMessageId();
        return ConfigFactory.systemMessagesWindowConfig(selMsg);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID + SystemMessages.Ids.VIEW);
    }

    @Override
    public String getWindowType() {
        return WindowType.SYSTEM_MESSAGES.toString();
    }

    @Override
    public void hide() {
        presenter.stop();
        super.hide();
    }
}
