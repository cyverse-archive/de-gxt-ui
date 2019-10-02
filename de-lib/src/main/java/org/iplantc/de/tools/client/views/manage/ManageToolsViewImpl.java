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
 * Created by sriram, aramsey on 09/01/19.
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
        unmask();
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

    @Override
    public void setListingConfig(Boolean isPublic,
                                 String searchTerm,
                                 String order,
                                 String orderBy,
                                 int rowsPerPage,
                                 int page) {
        currentProps.isPublic = isPublic;
        currentProps.searchTerm = searchTerm;
        currentProps.order = order;
        currentProps.orderBy = orderBy;
        currentProps.rowsPerPage = rowsPerPage;
        currentProps.page = page;
        mask();
    }

    public Boolean isPublic() {
        return currentProps.isPublic;
    }

    @Override
    public String getSearchTerm() {
        return currentProps.searchTerm;
    }

    @Override
    public String getOrder() {
        return currentProps.order;
    }

    @Override
    public String getOrderBy() {
        return currentProps.orderBy;
    }

    @Override
    public int getRowsPerPage() {
        return currentProps.rowsPerPage;
    }

    @Override
    public int getPage() {
        return currentProps.page;
    }

    void render() {
        CyVerseReactComponents.render(ReactToolViews.ManageTools, currentProps, htmlPanel.getElement());
    }
}
