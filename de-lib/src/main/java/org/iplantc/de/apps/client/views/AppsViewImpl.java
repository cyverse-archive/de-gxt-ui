package org.iplantc.de.apps.client.views;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.AppNavigationView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsToolbarView;
import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.apps.shared.AppsModule.Ids;
import org.iplantc.de.client.models.apps.AppCategory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
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
    @UiField(provided = true) AppNavigationView appNavigationView;
    @UiField
    ContentPanel westPanel;
    AppCategoriesView.Presenter categoriesPresenter;
    OntologyHierarchiesView.Presenter hierarchiesPresenter;
    AppsListView.Presenter gridPresenter;

    @UiField CardLayoutContainer cardContainer;

    @UiField
    BorderLayoutContainer.BorderLayoutData westData;

    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @Inject
    protected AppsViewImpl(@Assisted final AppCategoriesView.Presenter categoriesPresenter,
                           @Assisted final OntologyHierarchiesView.Presenter hierarchiesPresenter,
                           @Assisted final AppsListView.Presenter gridPresenter,
                           @Assisted final AppsToolbarView.Presenter toolbarPresenter,
                           AppNavigationView appNavigationView) {
        this.categoriesPresenter = categoriesPresenter;
        this.hierarchiesPresenter = hierarchiesPresenter;
        this.gridPresenter = gridPresenter;
        this.toolBar = toolbarPresenter.getView();
        this.appNavigationView = appNavigationView;

        initWidget(uiBinder.createAndBindUi(this));

        appNavigationView.addSelectionHandler(event -> {
            Widget selectedItem = event.getSelectedItem();
            for (Widget currentItem : appNavigationView.getWidgets()) {
                if (currentItem != selectedItem) {
                    ((Tree)currentItem).getSelectionModel().deselectAll();
                }
            }
            List<AppCategory> items = ((Tree)selectedItem).getStore().getRootItems();
            if (items != null && items.size() > 0) {
                ((Tree)selectedItem).getSelectionModel().select(items.get(0), false);
            }
        });

        gridPresenter.go(cardContainer);
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
    public AppNavigationView getAppNavigationView() {
        return appNavigationView;
    }

    @Override
    public void hideAppMenu() {
        toolBar.hideAppMenu();
    }

    @Override
    public void hideWorkflowMenu() {
        toolBar.hideWorkflowMenu();
    }

    public void resetAppNavigationView() {
        appNavigationView.clear();
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
    }

}
