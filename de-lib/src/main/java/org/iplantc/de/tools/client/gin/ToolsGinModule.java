package org.iplantc.de.tools.client.gin;

import org.iplantc.de.tools.client.gin.factory.EditToolViewFactory;
import org.iplantc.de.tools.client.gin.factory.ToolInfoViewFactory;
import org.iplantc.de.tools.client.gin.factory.ToolSharingPresenterFactory;
import org.iplantc.de.tools.client.presenter.ManageToolsViewPresenter;
import org.iplantc.de.tools.client.presenter.ToolSharingPresenterImpl;
import org.iplantc.de.tools.client.views.cells.ToolInfoCell;
import org.iplantc.de.tools.client.views.manage.EditToolView;
import org.iplantc.de.tools.client.views.manage.EditToolViewImpl;
import org.iplantc.de.tools.client.views.manage.ManageToolsToolbarView;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;
import org.iplantc.de.tools.client.views.manage.ManageToolsViewImpl;
import org.iplantc.de.tools.client.views.manage.ManageToolsViewToolbarImpl;
import org.iplantc.de.tools.client.views.manage.ToolInfoView;
import org.iplantc.de.tools.client.views.manage.ToolInfoViewImpl;
import org.iplantc.de.tools.client.views.manage.ToolSharingPresenter;
import org.iplantc.de.tools.client.views.manage.ToolSharingView;
import org.iplantc.de.tools.client.views.manage.ToolSharingViewImpl;
import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;
import org.iplantc.de.tools.client.views.requests.NewToolRequestFormViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

public class ToolsGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(NewToolRequestFormView.class).to(NewToolRequestFormViewImpl.class);
        install(new GinFactoryModuleBuilder().implement(EditToolView.class,
                                                        EditToolViewImpl.class)
                                             .build(EditToolViewFactory.class));
        bind(ToolInfoCell.class);
        bind(ManageToolsToolbarView.class).to(ManageToolsViewToolbarImpl.class);
        bind(ManageToolsView.class).to(ManageToolsViewImpl.class);
        bind(ManageToolsView.Presenter.class).to(ManageToolsViewPresenter.class);
        bind(ToolSharingView.class).to(ToolSharingViewImpl.class);
        install(new GinFactoryModuleBuilder().implement(ToolInfoView.class, ToolInfoViewImpl.class)
                                             .build(ToolInfoViewFactory.class));
        install(new GinFactoryModuleBuilder().implement(ToolSharingPresenter.class,
                                                        ToolSharingPresenterImpl.class)
                                             .build(ToolSharingPresenterFactory.class));
    }
}
