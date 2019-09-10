package org.iplantc.de.apps.client.views;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsToolbarView;
import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.apps.client.WorkspaceView;
import org.iplantc.de.apps.shared.AppsModule.Ids;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;

/**
 * @author jstroot
 */
public class AppsViewImpl extends Composite implements AppsView {
    @UiTemplate("AppsView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, AppsViewImpl> {
    }

    @UiField(provided = true) final AppsToolbarView toolBar;
    @UiField DETabPanel categoryTabs;
    @UiField
    ContentPanel westPanel;
    AppCategoriesView.Presenter categoriesPresenter;
    CommunitiesView.Presenter communitiesPresenter;
    OntologyHierarchiesView.Presenter hierarchiesPresenter;
    AppsListView.Presenter gridPresenter;

    @UiField
    SimpleContainer container;

    @UiField
    BorderLayoutContainer.BorderLayoutData westData;

    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @Inject
    protected AppsViewImpl(@Assisted final AppCategoriesView.Presenter categoriesPresenter,
                           @Assisted final CommunitiesView.Presenter communitiesPresenter,
                           @Assisted final OntologyHierarchiesView.Presenter hierarchiesPresenter,
                           @Assisted final AppsListView.Presenter gridPresenter,
                           @Assisted final AppsToolbarView.Presenter toolbarPresenter) {
        this.categoriesPresenter = categoriesPresenter;
        this.communitiesPresenter = communitiesPresenter;
        this.hierarchiesPresenter = hierarchiesPresenter;
        this.gridPresenter = gridPresenter;
        this.toolBar = toolbarPresenter.getView();

        initWidget(uiBinder.createAndBindUi(this));

        categoryTabs.addSelectionHandler(event -> {
            Widget selectedItem = event.getSelectedItem();
            deselectOtherTabTrees(selectedItem);
            selectFirstTreeNode(selectedItem);
        });

        gridPresenter.go(container);
    }

    void selectFirstTreeNode(Widget selectedItem) {
        if (selectedItem instanceof WorkspaceView) {
            ((WorkspaceView)selectedItem).selectFirstItem();
        } else {
            List<AppCategory> items = ((Tree)selectedItem).getStore().getRootItems();
            if (items != null && items.size() > 0) {
                ((Tree)selectedItem).getSelectionModel().select(items.get(0), false);
            }
        }
    }

    void deselectOtherTabTrees(Widget selectedItem) {
        for (Widget currentItem : categoryTabs) {
            if (currentItem != selectedItem) {
                if (currentItem instanceof WorkspaceView) {
                    ((WorkspaceView)currentItem).deselectAll();
                } else {
                    ((Tree)currentItem).getSelectionModel().deselectAll();
                }
            }
        }
    }

    @Override
    public String getWestPanelWidth() {
        return westData.getSize() + "";
    }

    @Override
    public void setWestPanelWidth(String width) {
        westData.setSize(Double.parseDouble(width));
    }

    @Override
    public DETabPanel getCategoryTabPanel() {
        return categoryTabs;
    }

    @Override
    public void hideAppMenu() {
        toolBar.hideAppMenu();
    }

    @Override
    public void hideWorkflowMenu() {
        toolBar.hideWorkflowMenu();
    }

    public void clearTabPanel() {
        categoryTabs.disableEvents();
        int tabCount = categoryTabs.getWidgetCount();
        for (int i = 0 ; i < tabCount ; i++) {
            categoryTabs.close(categoryTabs.getWidget(0));
        }
        categoryTabs.enableEvents();
    }

    @Override
    public boolean isNavPanelCollapsed() {
        return westPanel.isCollapsed();
    }

    @Override
    public void setNavPanelCollapsed(boolean collapsed) {
        if (collapsed) {
            Scheduler.get().scheduleDeferred(() -> westPanel.collapse());
        } else {
            westPanel.expand();
        }
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        toolBar.asWidget().ensureDebugId(baseID + Ids.MENU_BAR);
        gridPresenter.setViewDebugId(baseID);
        categoriesPresenter.setViewDebugId(baseID);
        hierarchiesPresenter.setViewDebugId(baseID);
        communitiesPresenter.setViewDebugId(baseID);
    }

}
