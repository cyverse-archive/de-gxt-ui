package org.iplantc.de.apps.client.views;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.WorkspaceView;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;

/**
 * @author aramsey
 */
public class WorkspaceViewImpl extends HTMLPanel implements WorkspaceView {

    private String baseID;
    private AppCategoriesView.AppCategoriesAppearance appearance;
    List<Tree> trees;

    @Inject
    public WorkspaceViewImpl(AppCategoriesView.AppCategoriesAppearance appearance) {
        super("<div></div>");
        trees = Lists.newArrayList();
        this.appearance = appearance;
    }

    public void go(DETabPanel deTabPanel, List<Tree> widgets) {
        clear();
        trees.clear();

        widgets.forEach(this::add);
        trees.addAll(widgets);

        deTabPanel.add(this, new TabItemConfig(appearance.workspaceTab()), baseID + AppsModule.Ids.WORKSPACE_TAB);
    }

    @Override
    public void deselectAll() {
        trees.forEach(tree -> {
            tree.getSelectionModel().deselectAll();
        });
    }

    @Override
    public void selectFirstItem() {
        if (trees != null) {
            Tree firstTree = trees.get(0);
            List rootItems = firstTree.getStore().getRootItems();
            if (rootItems != null && rootItems.size() > 0) {
                firstTree.getSelectionModel().select(rootItems.get(0), false);
            }
        }
    }

    @Override
    public void setViewDebugId(String baseID) {
        this.baseID = baseID;
    }
}
