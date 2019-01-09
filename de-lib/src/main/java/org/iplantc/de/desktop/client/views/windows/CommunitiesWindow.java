package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.commons.client.widgets.ContextualHelpToolButton;
import org.iplantc.de.communities.client.CommunitiesView;
import org.iplantc.de.communities.shared.CommunitiesModule;
import org.iplantc.de.desktop.shared.DeModule;

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;

/**
 * @author aramsey
 */
public class CommunitiesWindow extends WindowBase {

    public static final String COMMUNITIES = "#communities";
    private final CommunitiesView.Presenter presenter;
    private CommunitiesView.Appearance appearance;

    @Inject
    CommunitiesWindow(final CommunitiesView.Presenter presenter,
                      CommunitiesView.Appearance appearance) {
        this.presenter = presenter;
        this.appearance = appearance;

        setMinWidth(appearance.windowMinWidth());
        setMinHeight(appearance.windowMinHeight());
        setHeading(appearance.windowHeading());

        ContextualHelpToolButton contextualHelpToolButton = new ContextualHelpToolButton();
        contextualHelpToolButton.setHelp(new HTML(appearance.communitiesHelp()));
        btnHelp = contextualHelpToolButton;
        getHeader().insertTool(btnHelp, 0);
    }

    @Override
    public <C extends WindowConfig> void show(final C windowConfig,
                                              final String tag,
                                              final boolean isMaximizable) {
        super.show(windowConfig, tag, isMaximizable);
        presenter.go(this, DeModule.WindowIds.COMMUNITIES_WINDOW + CommunitiesModule.Ids.VIEW);

        ensureDebugId(DeModule.WindowIds.COMMUNITIES_WINDOW);
    }


    @Override
    public WindowConfig getWindowConfig() {
        return ConfigFactory.communitiesWindowConfig();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        btnHelp.ensureDebugId(baseID + CommunitiesModule.Ids.HELP_BTN);
    }

    @Override
    public String getWindowType() {
        return WindowType.COMMUNITIES.toString();
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
            setSize((Strings.isNullOrEmpty(width)) ? appearance.windowWidth() : width,
                    (Strings.isNullOrEmpty(height)) ? appearance.windowHeight() : height);
        }
    }
}
