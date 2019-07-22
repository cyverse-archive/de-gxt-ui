package org.iplantc.de.admin.apps.client.views.editor;

import org.iplantc.de.admin.apps.client.ReactAppsAdmin;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author aramsey
 */
public class AppEditor extends Composite {

    private final HTMLPanel panel;
    private ReactAppsAdmin.AdminAppDetailsProps currentProps;

    @Inject
    public AppEditor() {
        panel = new HTMLPanel("<div></div>");
    }

    public void setBaseProps(ReactAppsAdmin.AdminAppDetailsProps baseProps) {
        currentProps = baseProps;
    }

    public void edit(Splittable app) {
        currentProps.open = true;
        currentProps.app = app;
        render();
    }

    public void close() {
        currentProps.open = false;
        render();
    }

    public void render() {
        CyVerseReactComponents.render(ReactAppsAdmin.AdminAppDetails, currentProps, panel.getElement());
    }
}
