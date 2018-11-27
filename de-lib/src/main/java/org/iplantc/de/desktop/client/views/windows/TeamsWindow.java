package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.commons.client.widgets.ContextualHelpToolButton;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.shared.Teams;

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;

/**
 * @author aramsey
 */
public class TeamsWindow extends WindowBase {

    public static final String TEAMS = "#teams";
    private final TeamsView.Presenter presenter;
    private TeamsView.TeamsViewAppearance appearance;

    @Inject
    TeamsWindow(final TeamsView.Presenter presenter,
                TeamsView.TeamsViewAppearance appearance) {
        this.presenter = presenter;
        this.appearance = appearance;

        setMinWidth(appearance.windowMinWidth());
        setMinHeight(appearance.windowMinHeight());
        setHeading(appearance.windowHeading());

        ContextualHelpToolButton contextualHelpToolButton = new ContextualHelpToolButton();
        contextualHelpToolButton.setHelp(new HTML(appearance.teamsHelp()));
        btnHelp = contextualHelpToolButton;
        getHeader().insertTool(btnHelp, 0);
    }

    @Override
    public <C extends WindowConfig> void show(final C windowConfig,
                                              final String tag,
                                              final boolean isMaximizable) {
        super.show(windowConfig, tag, isMaximizable);
        presenter.go();
        add(presenter.getView());

        ensureDebugId(DeModule.WindowIds.TEAMS_WINDOW);
    }


    @Override
    public WindowConfig getWindowConfig() {
        return ConfigFactory.teamsWindowConfig();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID);
        btnHelp.ensureDebugId(baseID + Teams.Ids.HELP_BTN);
    }

    @Override
    public String getWindowType() {
        return WindowType.TEAMS.toString();
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
