package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.common.base.Strings;
import com.google.inject.Inject;

/**
 * Created by sriram on 5/5/17.
 */
public class ManageToolsWindow extends WindowBase {


    ManageToolsView.Presenter toolsPresenter;

    @Inject
    public ManageToolsWindow(ManageToolsView.Presenter toolsPresenter,
                             final IplantDisplayStrings displayStrings,
                             final UserInfo userInfo,
                             final ManageToolsView.ManageToolsViewAppearance appearance) {
        this.toolsPresenter = toolsPresenter;
        this.userInfo = userInfo;
        WindowState ws = getWindowStateFromLocalStorage();
        String width = ws.getWidth();
        String height = ws.getHeight();
        setSize((Strings.isNullOrEmpty(width)) ? appearance.windowWidth() : width,
                (Strings.isNullOrEmpty(height)) ? appearance.windowHeight() : height);

        setMinHeight(appearance.windowMinHeight());
        setMinWidth(appearance.windowMinWidth());
        setHeading(displayStrings.manageTools());
        ensureDebugId(DeModule.WindowIds.MANAGE_TOOLS_WINDOW);
    }


    @Override
    public WindowConfig getWindowConfig() {
        return ConfigFactory.manageToolsWindowConfig();
    }

    @Override
    public <C extends WindowConfig> void show(final C windowConfig,
                                              final String tag,
                                              final boolean isMaximizable) {
        toolsPresenter.go(this);
        super.show(windowConfig, tag, isMaximizable);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        toolsPresenter.setViewDebugId(baseID + ToolsModule.ToolIds.TOOLS_VIEW);
    }

    @Override
    public String getWindowType() {
        return WindowType.MANAGETOOLS.toString();
    }

    @Override
    public void hide() {
        super.hide();
    }


}
