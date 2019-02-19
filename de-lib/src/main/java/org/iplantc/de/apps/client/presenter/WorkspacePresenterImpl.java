package org.iplantc.de.apps.client.presenter;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.apps.client.WorkspaceView;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;

/**
 * @author aramsey
 */
public class WorkspacePresenterImpl implements WorkspaceView.Presenter {

    private WorkspaceView view;
    private AppCategoriesView.Presenter categoriesPresenter;
    private CommunitiesView.Presenter communitiesPresenter;

    @Inject
    public WorkspacePresenterImpl(final WorkspaceView view,
                                  final AppCategoriesView.Presenter categoriesPresenter,
                                  final CommunitiesView.Presenter communitiesPresenter) {
        this.view = view;
        this.categoriesPresenter = categoriesPresenter;
        this.communitiesPresenter = communitiesPresenter;
    }


    @Override
    public void go(HasId selectedAppCategory,
                   HasId selectedCommunity,
                   boolean selectDefaultCategory,
                   DETabPanel tabPanel) {
        List<Tree> widgetList = Lists.newArrayList(categoriesPresenter.getWorkspaceView().getTree(),
                                                   communitiesPresenter.getView().getTree());

        view.go(tabPanel, widgetList);
        categoriesPresenter.go(selectedAppCategory, selectDefaultCategory, tabPanel);
        communitiesPresenter.go(selectedCommunity, tabPanel);
    }

    @Override
    public AppCategoriesView.Presenter getCategoriesPresenter() {
        return categoriesPresenter;
    }

    @Override
    public CommunitiesView.Presenter getCommunitiesPresenter() {
        return communitiesPresenter;
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.setViewDebugId(baseId);
    }
}
