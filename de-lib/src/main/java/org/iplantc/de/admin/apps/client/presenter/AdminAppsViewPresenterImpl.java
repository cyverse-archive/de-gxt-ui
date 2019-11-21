package org.iplantc.de.admin.apps.client.presenter;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.apps.client.AdminAppsToolbarView;
import org.iplantc.de.admin.apps.client.AdminAppsView;
import org.iplantc.de.admin.apps.client.AdminCategoriesView;
import org.iplantc.de.admin.apps.client.gin.factory.AdminAppsViewFactory;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.services.AppServiceFacade;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

/**
 * Presenter class for the Belphegor <code>AppsView</code>.
 * 
 * The belphegor uses a different {@link AppServiceFacade} implementation than the one used in the
 * Discovery Environment. Through the use of deferred binding, the different {@link AppServiceFacade}
 * implementations are resolved, enabling the ability to reuse code.
 * 
 * <b> There are two places in the {@link org.iplantc.de.apps.client.presenter.AppsViewPresenterImpl}
 * where this deferred binding takes place; in the
 * {@link org.iplantc.de.apps.client.presenter.categories.proxy.AppCategoryProxy}.
 * 
 * 
 * @author jstroot
 * 
 */
public class AdminAppsViewPresenterImpl implements AdminAppsView.AdminPresenter {

    private final AdminCategoriesView.Presenter categoriesPresenter;
    private final AdminAppsView view;


    @Inject
    AdminAppsViewPresenterImpl(final AdminAppsViewFactory viewFactory,
                               final AdminCategoriesView.Presenter categoriesPresenter,
                               final AdminAppsToolbarView.Presenter toolbarPresenter,
                               final AdminAppsGridView.Presenter gridPresenter) {
        this.categoriesPresenter = categoriesPresenter;
        this.view = viewFactory.create(categoriesPresenter,
                                       toolbarPresenter,
                                       gridPresenter);

        categoriesPresenter.getView().addAppCategorySelectedEventHandler(gridPresenter);
        categoriesPresenter.getView().addAppCategorySelectedEventHandler(gridPresenter.getView());
        categoriesPresenter.getView().addAppCategorySelectedEventHandler(toolbarPresenter.getView());

        gridPresenter.addAppSelectionChangedEventHandler(toolbarPresenter.getView());

        toolbarPresenter.getView().addAddCategorySelectedHandler(categoriesPresenter);
        toolbarPresenter.getView().addRenameCategorySelectedHandler(categoriesPresenter);
        toolbarPresenter.getView().addDeleteCategorySelectedHandler(categoriesPresenter);
        toolbarPresenter.getView().addDeleteAppsSelectedHandler(gridPresenter);
        toolbarPresenter.getView().addRestoreAppSelectedHandler(gridPresenter);
        toolbarPresenter.getView().addCategorizeAppSelectedHandler(categoriesPresenter);
        toolbarPresenter.getView().addMoveCategorySelectedHandler(categoriesPresenter);
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(categoriesPresenter);
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(gridPresenter);
        toolbarPresenter.getView().addBeforeAppSearchEventHandler(gridPresenter.getView());
    }

    @Override
    public void go(final HasOneWidget container,
                   final HasId selectedAppCategory) {
        categoriesPresenter.go(selectedAppCategory);
        container.setWidget(view);
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + Belphegor.AppIds.VIEW);
    }

}
