package org.iplantc.de.desktop.client.views.windows;

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

import com.sencha.gxt.core.shared.FastMap;

/**
 * The window for displaying all active system messages.
 * @author jstroot
 */
public final class SystemMessagesWindow extends WindowBase {

    private final MessagesView.MessagesAppearance messageAppearance;
    private MessagesPresenter presenter;


    @Inject
    SystemMessagesWindow(final IplantDisplayStrings displayStrings,
                         final MessagesView.MessagesAppearance appearance) {
        this.messageAppearance = appearance;
        setHeading(displayStrings.systemMessagesLabel());

        setMinHeight(Integer.parseInt(appearance.windowHeight()));
        setMinWidth(Integer.parseInt(appearance.windowWidth()));
    }

    @Override
    public <C extends WindowConfig> void show(C windowConfig, String tag,
                                              boolean isMaximizable) {
        super.show(windowConfig, tag, isMaximizable);
        this.presenter = new MessagesPresenter(((SystemMessagesWindowConfig)windowConfig).getSelectedMessage());
        presenter.go(this);
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
    public FastMap<String> getAdditionalWindowStates() {
        return null;
    }

    @Override
    public void restoreWindowState() {
        if (getStateId().equals(ws.getTag())) {
            super.restoreWindowState();
            String width = ws.getWidth();
            String height = ws.getHeight();
            setSize((Strings.isNullOrEmpty(width)) ? computeDefaultWidth(messageAppearance) + "" : width,
                    (Strings.isNullOrEmpty(height)) ?
                    computeDefaultHeight(messageAppearance) + "" :
                    height);
        }

    }

    @Override
    public void hide() {
        presenter.stop();
        super.hide();
    }
}
