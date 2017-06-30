package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.presenter.ManageCollaboratorsPresenter;
import org.iplantc.de.commons.client.views.window.configs.CollaborationWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.shared.DeModule;

import com.google.inject.Inject;

/**
 * @author jstroot
 */
public class CollaborationWindow extends IplantWindowBase {

    public static final String COLLABORATION = "#collaboration";
    private final ManageCollaboratorsPresenter presenter;
    private ManageCollaboratorsView.Appearance appearance;

    @Inject
    CollaborationWindow(final ManageCollaboratorsPresenter presenter, ManageCollaboratorsView.Appearance appearance) {
        this.presenter = presenter;
        this.appearance = appearance;

        // This must be set before we render view
        ensureDebugId(DeModule.WindowIds.COLLABORATION_WINDOW);
        setSize(appearance.windowWidth(), appearance.windowHeight());
        setMinWidth(appearance.windowMinWidth());
        setHeading(appearance.windowHeading());
    }

    @Override
    public <C extends WindowConfig> void show(final C windowConfig,
                                              final String tag,
                                              final boolean isMaximizable) {
        final CollaborationWindowConfig collabWindowConfig = (CollaborationWindowConfig)windowConfig;
        presenter.go(this, ManageCollaboratorsView.MODE.MANAGE);
        super.show(windowConfig, tag, isMaximizable);
    }

    @Override
    public WindowState getWindowState() {
        CollaborationWindowConfig config = ConfigFactory.collaborationWindowConfig();
        return createWindowState(config);
    }
}
