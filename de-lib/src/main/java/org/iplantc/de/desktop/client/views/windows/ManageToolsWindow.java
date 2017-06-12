package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.inject.Inject;

/**
 * Created by sriram on 5/5/17.
 */
public class ManageToolsWindow extends IplantWindowBase {


    ManageToolsView.Presenter toolsPresenter;

    @Inject
    public ManageToolsWindow(ManageToolsView.Presenter toolsPresenter,
                             final IplantDisplayStrings displayStrings) {
        this.toolsPresenter = toolsPresenter;
        setSize("800px", "600px");
        setHeading("Manage Tools");
    }

    @Override
    public WindowState getWindowState() {
        return createWindowState(ConfigFactory.manageToolsWindowConfig());
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


}
