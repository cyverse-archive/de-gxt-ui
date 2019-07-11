package org.iplantc.de.admin.desktop.client.toolAdmin.gin;

import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.admin.desktop.client.toolAdmin.gin.factory.ToolAdminViewFactory;
import org.iplantc.de.admin.desktop.client.toolAdmin.presenter.ToolAdminPresenterImpl;
import org.iplantc.de.admin.desktop.client.toolAdmin.service.ToolAdminServiceFacade;
import org.iplantc.de.admin.desktop.client.toolAdmin.service.impl.ToolAdminServiceFacadeImpl;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.ToolAdminViewImpl;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs.DeleteToolDialog;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs.OverwriteToolDialog;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.subviews.ToolPublicAppListWindow;
import org.iplantc.de.tools.client.gin.factory.EditToolViewFactory;
import org.iplantc.de.tools.client.views.manage.EditToolView;
import org.iplantc.de.tools.client.views.manage.EditToolViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

/**
 * @author jstroot
 */
public class ToolAdminGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new GinFactoryModuleBuilder().implement(ToolAdminView.class, ToolAdminViewImpl.class)
                                             .build(ToolAdminViewFactory.class));
        bind(ToolPublicAppListWindow.class);
        bind(OverwriteToolDialog.class);
        bind(DeleteToolDialog.class);
        bind(ToolAdminView.Presenter.class).to(ToolAdminPresenterImpl.class);
        bind(ToolAdminServiceFacade.class).to(ToolAdminServiceFacadeImpl.class);
        install(new GinFactoryModuleBuilder().implement(EditToolView.class,
                                                        EditToolViewImpl.class)
                                            .build(EditToolViewFactory.class));
    }
}

