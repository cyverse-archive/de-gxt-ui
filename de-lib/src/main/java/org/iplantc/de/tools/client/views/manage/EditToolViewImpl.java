package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.tools.client.ReactToolViews;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author aramsey
 */
public class EditToolViewImpl extends Composite implements EditToolView {

    private final HTMLPanel panel;
    private final ReactToolViews.EditToolProps currentProps;

    @Inject
    public EditToolViewImpl(@Assisted ReactToolViews.EditToolProps baseProps,
                            DEProperties deProperties) {
        this.currentProps = baseProps;
        currentProps.maxCPUCore = deProperties.getToolsMaxCPULimit();
        currentProps.maxMemory = deProperties.getToolsMaxMemLimit();
        currentProps.maxDiskSpace = deProperties.getToolsMaxDiskLimit();
        
        panel = new HTMLPanel("<div></div>");
    }

    @Override

    public void edit(Splittable tool) {
        currentProps.open = true;
        currentProps.loading = false;
        currentProps.tool = tool;
        render();
    }

    @Override
    public void setToolTypes(String[] toolTypes) {
        currentProps.toolTypes = toolTypes;
    }

    @Override
    public void mask() {
        currentProps.loading = true;
        render();
    }

    @Override
    public void unmask() {
        currentProps.loading = false;
        render();
    }

    @Override
    public void close() {
        currentProps.loading = false;
        currentProps.open = false;
        render();
    }

    private void render() {
        CyVerseReactComponents.render(ReactToolViews.EditTool, currentProps, panel.getElement());
    }
}
