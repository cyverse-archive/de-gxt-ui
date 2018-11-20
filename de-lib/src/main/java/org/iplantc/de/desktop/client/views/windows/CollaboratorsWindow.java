package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.commons.client.widgets.ContextualHelpToolButton;
import org.iplantc.de.desktop.shared.DeModule;

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;

/**
 * @author jstroot
 */
public class CollaboratorsWindow extends WindowBase {

    public static final String COLLABORATION = "#collaboration";
    private final ManageCollaboratorsView.Presenter presenter;
    private ManageCollaboratorsView.Appearance appearance;

    @Inject
    CollaboratorsWindow(final ManageCollaboratorsView.Presenter presenter,
                        ManageCollaboratorsView.Appearance appearance) {
        this.presenter = presenter;
        this.appearance = appearance;

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
        super.show(windowConfig, tag, isMaximizable);
        presenter.go();
        add(presenter.getView());

        ensureDebugId(DeModule.WindowIds.COLLABORATORS_WINDOW);
    }


    @Override
    public WindowConfig getWindowConfig() {
        return ConfigFactory.collaboratorsWindowConfig();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID);
        btnHelp.ensureDebugId(baseID + CollaboratorsModule.Ids.HELP_BTN);
    }

    @Override
    public String getWindowType() {
        return WindowType.COLLABORATORS.toString();
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
