package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.collaborators.client.CollaborationView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.views.window.configs.CollaborationWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.commons.client.widgets.ContextualHelpToolButton;
import org.iplantc.de.desktop.shared.DeModule;

import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

/**
 * @author jstroot
 */
public class CollaborationWindow extends IplantWindowBase {

    public static final String COLLABORATION = "#collaboration";
    private final CollaborationView.Presenter presenter;
    private ManageCollaboratorsView.Appearance appearance;

    @Inject
    CollaborationWindow(final CollaborationView.Presenter presenter,
                        ManageCollaboratorsView.Appearance appearance) {
        this.presenter = presenter;
        this.appearance = appearance;

        // This must be set before we render view
        setSize(appearance.windowWidth(), appearance.windowHeight());
        setMinWidth(appearance.windowMinWidth());
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
    public WindowState getWindowState() {
        CollaborationWindowConfig config = ConfigFactory.collaborationWindowConfig();
        return createWindowState(config);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID);
        btnHelp.ensureDebugId(baseID + CollaboratorsModule.Ids.HELP_BTN);
    }
}
