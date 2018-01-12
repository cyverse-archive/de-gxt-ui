package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.collaborators.client.CollaborationView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.views.window.configs.CollaborationWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.commons.client.widgets.ContextualHelpToolButton;
import org.iplantc.de.desktop.shared.DeModule;

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

/**
 * @author jstroot
 */
public class CollaborationWindow extends WindowBase {

    public static final String COLLABORATION = "#collaboration";
    private final CollaborationView.Presenter presenter;
    private ManageCollaboratorsView.Appearance appearance;

    @Inject
    CollaborationWindow(final CollaborationView.Presenter presenter,
                        ManageCollaboratorsView.Appearance appearance,
                        UserInfo userInfo) {
        this.presenter = presenter;
        this.appearance = appearance;
        this.userInfo = userInfo;


        WindowState ws = getWindowStateFromLocalStorage();
        String width = ws.getWidth();
        String height = ws.getHeight();
        setSize((Strings.isNullOrEmpty(width)) ? appearance.windowWidth() : width,
                (Strings.isNullOrEmpty(height)) ? appearance.windowHeight() : height);
        setMinWidth(appearance.windowMinWidth());
        setMinHeight(appearance.windowMinHeight());
        setHeading(appearance.windowHeading());

        ContextualHelpToolButton contextualHelpToolButton = new ContextualHelpToolButton();
        contextualHelpToolButton.setHelp(new HTML(appearance.collaboratorsHelp()));
        btnHelp = contextualHelpToolButton;
        getHeader().insertTool(btnHelp, 0);
    }

    @Override
    public <C extends WindowConfig> void show(final C windowConfig,
                                              final String tag,
                                              final boolean isMaximizable) {
        final CollaborationWindowConfig collabWindowConfig = (CollaborationWindowConfig)windowConfig;
        presenter.go(this, collabWindowConfig);
        super.show(windowConfig, tag, isMaximizable);

        ensureDebugId(DeModule.WindowIds.COLLABORATION_WINDOW);
    }


    @Override
    public WindowConfig getWindowConfig() {
        return ConfigFactory.collaborationWindowConfig();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID);
        btnHelp.ensureDebugId(baseID + CollaboratorsModule.Ids.HELP_BTN);
    }

    @Override
    public String getWindowType() {
        return WindowType.COLLABORATION.toString();
    }

    @Override
    public void hide() {
        super.hide();
    }
}
