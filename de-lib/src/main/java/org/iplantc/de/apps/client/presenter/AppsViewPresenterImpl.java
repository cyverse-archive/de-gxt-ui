package org.iplantc.de.apps.client.presenter;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsToolbarView;
import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.apps.client.events.selection.RefreshAppsSelectedEvent;
import org.iplantc.de.apps.client.gin.factory.AppsViewFactory;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.util.OntologyUtil;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.TabPanel;

import java.util.List;

/**
 * The presenter for the AppsView.
 *
 * @author jstroot
 */
public class AppsViewPresenterImpl implements AppsView.Presenter,
                                              RefreshAppsSelectedEvent.RefreshAppsSelectedEventHandler {

    protected final AppsView view;
    private final AppCategoriesView.Presenter categoriesPresenter;
    private final AppsListView.Presenter appsListPresenter;
    private final OntologyHierarchiesView.Presenter hierarchiesPresenter;
    OntologyUtil ontologyUtil;

    @Inject
    protected AppsViewPresenterImpl(final AppsViewFactory viewFactory,
                                    final AppCategoriesView.Presenter categoriesPresenter,
                                    final AppsListView.Presenter appsListPresenter,
                                    final AppsToolbarView.Presenter toolbarPresenter,
                                    final OntologyHierarchiesView.Presenter hierarchiesPresenter) {
        this.categoriesPresenter = categoriesPresenter;
        this.appsListPresenter = appsListPresenter;
        this.hierarchiesPresenter = hierarchiesPresenter;
        this.view = viewFactory.create(categoriesPresenter,
                                       hierarchiesPresenter, appsListPresenter,
                                       toolbarPresenter);
        this.ontologyUtil = OntologyUtil.getInstance();

        categoriesPresenter.addAppCategorySelectedEventHandler(appsListPresenter);
        categoriesPresenter.addAppCategorySelectedEventHandler(toolbarPresenter.getView());

        hierarchiesPresenter.addOntologyHierarchySelectionChangedEventHandler(appsListPresenter);
        hierarchiesPresenter.addOntologyHierarchySelectionChangedEventHandler(toolbarPresenter.getView());
        hierarchiesPresenter.addSelectedHierarchyNotFoundHandler(categoriesPresenter);

        appsListPresenter.addAppSelectionChangedEventHandler(toolbarPresenter.getView());
        appsListPresenter.addAppInfoSelectedEventHandler(hierarchiesPresenter);

        toolbarPresenter.getView().addDeleteAppsSelectedHandler(appsListPresenter);
        toolbarPresenter.getView().addCopyAppSelectedHandler(categoriesPresenter);
        toolbarPresenter.getView().addCopyWorkflowSelectedHandler(categoriesPresenter);
        toolbarPresenter.getView().addRunAppSelectedHandler(appsListPresenter);
        toolbarPresenter.getView().addBeforeAppSearchEventHandler(appsListPresenter);
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(categoriesPresenter);
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(hierarchiesPresenter);
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(appsListPresenter);
        toolbarPresenter.getView().addSwapViewButtonClickedEventHandler(appsListPresenter);
        toolbarPresenter.getView().addRefreshAppsSelectedEventHandler(this);
    }

    @Override
    public List<DragSource> getAppsDragSources() {
        return appsListPresenter.getAppsDragSources();
    }

    @Override
    public App getSelectedApp() {
        return appsListPresenter.getSelectedApp();
    }

    @Override
    public AppCategory getSelectedAppCategory() {
        return categoriesPresenter.getSelectedAppCategory();
    }

    @Override
    public void go(final HasOneWidget container,
                   final HasId selectedAppCategory,
                   final HasId selectedApp) {
        DETabPanel tabPanel = view.getCategoryTabPanel();
        if (isEmpty(tabPanel)) {
            categoriesPresenter.go(selectedAppCategory, true, tabPanel);
            hierarchiesPresenter.go(null, tabPanel);
        }
        container.setWidget(view);
    }

    boolean isEmpty(TabPanel tabPanel) {
        return tabPanel.getWidgetCount() == 0;
    }

    @Override
    public AppsView.Presenter hideAppMenu() {
        view.hideAppMenu();
        return this;
    }

    @Override
    public AppsView.Presenter hideWorkflowMenu() {
        view.hideWorkflowMenu();
        return this;
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId);
    }

    @Override
    public void onRefreshAppsSelected(RefreshAppsSelectedEvent event) {
        AppCategory selectedAppCategory = categoriesPresenter.getSelectedAppCategory();
        OntologyHierarchy selectedHierarchy = hierarchiesPresenter.getSelectedHierarchy();
        boolean useDefaultSelection = selectedHierarchy == null;

        view.clearTabPanel();
        DETabPanel categoryTabPanel = view.getCategoryTabPanel();

        categoriesPresenter.go(selectedAppCategory, useDefaultSelection, categoryTabPanel);
        hierarchiesPresenter.go(selectedHierarchy, categoryTabPanel);
    }
}
