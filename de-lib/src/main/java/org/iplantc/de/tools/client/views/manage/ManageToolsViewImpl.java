package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.tools.client.ReactToolViews;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * Created by sriram, aramsey on 4/21/17.
 */
public class ManageToolsViewImpl extends Composite implements ManageToolsView {

    HTMLPanel htmlPanel;
    ReactToolViews.ManageToolsProps currentProps;

    @Inject
    public ManageToolsViewImpl(@Assisted ReactToolViews.ManageToolsProps props) {
        currentProps = props;
        htmlPanel = new HTMLPanel("<div></div>");
    }

    @Override
    public Widget asWidget() {
        return htmlPanel;
    }

    @Override
    public Splittable getCurrentToolList() {
        return currentProps.toolList;
    }

    @Override
    public void loadTools(Splittable tools) {
        currentProps.toolList = tools;
        render();
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
    public void setBaseId(String baseId) {
        currentProps.parentId = baseId;
        render();
    }

    void render() {
        CyVerseReactComponents.render(ReactToolViews.ManageTools, currentProps, htmlPanel.getElement());
    }
}
